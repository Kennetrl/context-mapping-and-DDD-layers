package com.veritrabajo.backend.workerprofile.application.dto;

import com.veritrabajo.backend.workerprofile.domain.model.WorkerProfile;
import java.util.List;

public record WorkerProfileResponse(
        String id,
        String fullName,
        String phoneNumber,
        List<String> occupations,
        List<String> technicalSkills,
        int reputationScore
) {
    public static WorkerProfileResponse from(WorkerProfile profile) {
        return new WorkerProfileResponse(
                profile.getId().asString(),
                profile.getFullName(),
                profile.getPhoneNumber(),
                profile.getOccupations().stream().map(o -> o.tradeName()).toList(),
                profile.getTechnicalSkills().stream().map(s -> s.name()).toList(),
                profile.getReputation().getScore()
        );
    }
}
