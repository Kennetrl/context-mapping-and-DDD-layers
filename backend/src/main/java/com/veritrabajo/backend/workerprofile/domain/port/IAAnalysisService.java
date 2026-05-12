package com.veritrabajo.backend.workerprofile.domain.port;

import com.veritrabajo.backend.workerprofile.domain.model.AnalysisResult;
import com.veritrabajo.backend.workerprofile.domain.model.RawDescription;

/**
 * Port (driven adapter contract) for AI-based profile analysis.
 * This is an interface defining an infrastructure capability, so it belongs
 * in domain/port rather than domain/service (which is for concrete stateless
 * domain logic classes).
 */
public interface IAAnalysisService {

    AnalysisResult analyze(RawDescription description);
}
