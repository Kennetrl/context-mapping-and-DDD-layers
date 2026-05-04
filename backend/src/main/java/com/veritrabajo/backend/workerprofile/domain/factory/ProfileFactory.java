package com.veritrabajo.backend.workerprofile.domain.factory;

import com.veritrabajo.backend.workerprofile.domain.event.ProfileProfessionalized;
import com.veritrabajo.backend.workerprofile.domain.model.AnalysisResult;
import com.veritrabajo.backend.workerprofile.domain.model.Occupation;
import com.veritrabajo.backend.workerprofile.domain.model.RawDescription;
import com.veritrabajo.backend.workerprofile.domain.model.TechnicalSkill;
import com.veritrabajo.backend.workerprofile.domain.model.WorkerProfile;
import com.veritrabajo.backend.workerprofile.domain.service.IAAnalysisService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Factory responsable de crear un WorkerProfile completo.
 * Orquesta la creación del agregado raíz, el análisis con IA
 * para extraer oficios y habilidades, y la publicación del evento
 * de dominio ProfileProfessionalized al finalizar.
 */
@Component
public class ProfileFactory {

    private final IAAnalysisService analysisService;
    private final ApplicationEventPublisher eventPublisher;

    // Inyección por constructor (buena práctica en Spring)
    public ProfileFactory(
            IAAnalysisService analysisService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.analysisService = analysisService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Crea un WorkerProfile completo a partir de los datos básicos
     * y el texto libre de la descripción del trabajador.
     * La IA extrae automáticamente los oficios y habilidades técnicas.
     *
     * @param fullName    nombre completo del trabajador
     * @param phoneNumber teléfono de contacto
     * @param rawText     descripción libre de su experiencia
     * @return perfil del trabajador con oficios y habilidades identificados
     */
    public WorkerProfile createFromDescription(
            String fullName,
            String phoneNumber,
            String rawText
    ) {
        WorkerProfile profile = WorkerProfile.create(fullName, phoneNumber);
        RawDescription description = RawDescription.of(rawText);
        profile.assignRawDescription(description);
        AnalysisResult result = analysisService.analyze(description);
        applyAnalysisToProfile(profile, result);
        publishProfessionalizedEvent(profile);
        return profile;
    }

    private void applyAnalysisToProfile(WorkerProfile profile, AnalysisResult result) {
        for (Occupation occupation : result.getOccupations()) {
            profile.addOccupation(occupation);
        }
        for (TechnicalSkill skill : result.getTechnicalSkills()) {
            profile.addTechnicalSkill(skill);
        }
    }

    private void publishProfessionalizedEvent(WorkerProfile profile) {
        ProfileProfessionalized event = ProfileProfessionalized.of(
                profile.getId(),
                profile.getFullName()
        );
        eventPublisher.publishEvent(event);
    }
}
