package com.veritrabajo.backend.workerprofile.domain.service;

import com.veritrabajo.backend.workerprofile.domain.model.AnalysisResult;
import com.veritrabajo.backend.workerprofile.domain.model.RawDescription;

/**
 * Interfaz del servicio de dominio que define el contrato
 * para analizar la descripción bruta del trabajador con IA.
 * La implementación real vive en infrastructure/acl/
 * para no contaminar el dominio con detalles técnicos externos.
 */
public interface IAAnalysisService {

    /**
     * Analiza el texto libre del trabajador y extrae
     * sus oficios y habilidades técnicas automáticamente.
     *
     * @param description la descripción bruta ingresada por el trabajador
     * @return resultado con oficios y habilidades identificadas
     */
    AnalysisResult analyze(RawDescription description);
}
