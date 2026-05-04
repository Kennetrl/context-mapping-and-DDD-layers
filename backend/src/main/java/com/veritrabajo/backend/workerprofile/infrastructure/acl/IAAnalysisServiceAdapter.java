package com.veritrabajo.backend.workerprofile.infrastructure.acl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.veritrabajo.backend.workerprofile.domain.model.AnalysisResult;
import com.veritrabajo.backend.workerprofile.domain.model.Occupation;
import com.veritrabajo.backend.workerprofile.domain.model.Occupation.ExpertiseLevel;
import com.veritrabajo.backend.workerprofile.domain.model.RawDescription;
import com.veritrabajo.backend.workerprofile.domain.model.TechnicalSkill;
import com.veritrabajo.backend.workerprofile.domain.service.IAAnalysisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador ACL (Anti-Corruption Layer) que implementa IAAnalysisService
 * conectándose a la API de Google Gemini.
 * Traduce la respuesta externa al modelo de dominio sin contaminarlo.
 * La API key se lee desde variables de entorno o application.properties.
 */
@Component
public class IAAnalysisServiceAdapter implements IAAnalysisService {

    // URL base de la API de Gemini
    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/"
                    + "gemini-2.0-flash:generateContent";

    // Prompt que le enviamos a Gemini para extraer skills del texto
    private static final String ANALYSIS_PROMPT =
            "Analiza el siguiente texto de experiencia laboral y responde "
                    + "ÚNICAMENTE con un JSON válido con esta estructura exacta:\n"
                    + "{\n"
                    + "  \"occupations\": [{\"tradeName\": \"string\", "
                    + "\"level\": \"BEGINNER|INTERMEDIATE|ADVANCED|EXPERT\"}],\n"
                    + "  \"technicalSkills\": [\"string\"]\n"
                    + "}\n"
                    + "Texto a analizar:\n";

    private final String apiKey;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    // La API key se inyecta desde application.properties o variable de entorno
    public IAAnalysisServiceAdapter(
            @Value("${gemini.api.key:NOT_CONFIGURED}") String apiKey,
            RestClient.Builder restClientBuilder
    ) {
        this.apiKey = apiKey;
        this.restClient = restClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Llama a Gemini con la descripción del trabajador
     * y parsea la respuesta JSON para extraer oficios y habilidades.
     */
    @Override
    public AnalysisResult analyze(RawDescription description) {
        validateApiKey();

        String requestBody = buildRequestBody(description.getText());
        String rawResponse = callGeminiApi(requestBody);

        return parseGeminiResponse(rawResponse);
    }

    /**
     * Verifica que la API key esté configurada antes de llamar a Gemini.
     */
    private void validateApiKey() {
        if ("NOT_CONFIGURED".equals(apiKey)) {
            throw new IllegalStateException(
                    "La API key de Gemini no está configurada. "
                            + "Agrega 'gemini.api.key=TU_KEY' en application.properties"
            );
        }
    }

    /**
     * Construye el body JSON que espera la API de Gemini.
     */
    private String buildRequestBody(String workerText) {
        String promptText = ANALYSIS_PROMPT + workerText;
        return "{"
                + "\"contents\": [{"
                + "\"parts\": [{\"text\": \""
                + escapeJson(promptText)
                + "\"}]"
                + "}]"
                + "}";
    }

    /**
     * Realiza la llamada HTTP a la API de Google Gemini.
     */
    private String callGeminiApi(String requestBody) {
        return restClient.post()
                .uri(GEMINI_URL + "?key=" + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(String.class);
    }

    /**
     * Parsea la respuesta de Gemini y construye el AnalysisResult
     * con los oficios y habilidades extraídos.
     */
    private AnalysisResult parseGeminiResponse(String rawResponse) {
        try {
            JsonNode parsed = extractInnerJson(rawResponse);
            List<Occupation> occupations = extractOccupations(parsed);
            List<TechnicalSkill> skills = extractSkills(parsed);
            return AnalysisResult.of(occupations, skills);
        } catch (Exception e) {
            return AnalysisResult.of(new ArrayList<>(), new ArrayList<>());
        }
    }

    private JsonNode extractInnerJson(String rawResponse) throws Exception {
        JsonNode root = objectMapper.readTree(rawResponse);
        String jsonText = root
                .path("candidates").get(0)
                .path("content")
                .path("parts").get(0)
                .path("text")
                .asText();
        String cleanJson = jsonText
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
        return objectMapper.readTree(cleanJson);
    }

    /**
     * Extrae la lista de oficios desde el JSON parseado de Gemini.
     */
    private List<Occupation> extractOccupations(JsonNode parsed) {
        List<Occupation> occupations = new ArrayList<>();
        JsonNode occupationsNode = parsed.path("occupations");

        for (JsonNode node : occupationsNode) {
            String tradeName = node.path("tradeName").asText();
            String levelStr = node.path("level").asText("INTERMEDIATE");
            ExpertiseLevel level = parseLevel(levelStr);

            if (!tradeName.isBlank()) {
                occupations.add(Occupation.of(tradeName, level));
            }
        }
        return occupations;
    }

    /**
     * Extrae la lista de habilidades técnicas desde el JSON de Gemini.
     */
    private List<TechnicalSkill> extractSkills(JsonNode parsed) {
        List<TechnicalSkill> skills = new ArrayList<>();
        JsonNode skillsNode = parsed.path("technicalSkills");

        for (JsonNode node : skillsNode) {
            String skillName = node.asText();
            if (!skillName.isBlank()) {
                skills.add(TechnicalSkill.of(skillName));
            }
        }
        return skills;
    }

    /**
     * Convierte el string del nivel al enum ExpertiseLevel.
     * Si no reconoce el valor, devuelve INTERMEDIATE por defecto.
     */
    private ExpertiseLevel parseLevel(String levelStr) {
        try {
            return ExpertiseLevel.valueOf(levelStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ExpertiseLevel.INTERMEDIATE;
        }
    }

    /**
     * Escapa caracteres especiales para que el texto sea JSON válido.
     */
    private String escapeJson(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
