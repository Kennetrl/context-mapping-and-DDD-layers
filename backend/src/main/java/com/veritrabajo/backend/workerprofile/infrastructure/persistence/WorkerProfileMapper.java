package com.veritrabajo.backend.workerprofile.infrastructure.persistence;

import com.veritrabajo.backend.workerprofile.domain.model.AuthUserId;
import com.veritrabajo.backend.workerprofile.domain.model.Occupation;
import com.veritrabajo.backend.workerprofile.domain.model.Occupation.ExpertiseLevel;
import com.veritrabajo.backend.workerprofile.domain.model.RawDescription;
import com.veritrabajo.backend.workerprofile.domain.model.TechnicalSkill;
import com.veritrabajo.backend.workerprofile.domain.model.WorkerId;
import com.veritrabajo.backend.workerprofile.domain.model.WorkerProfile;
import com.veritrabajo.backend.workerprofile.domain.model.WorkerProfileSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for bidirectional conversion between the WorkerProfile
 * aggregate (domain) and WorkerProfileEntity (infrastructure/persistence).
 * Keeps mapping and serialization logic out of the repository implementation.
 */
public final class WorkerProfileMapper {

    private static final String OCCUPATION_DELIMITER = "\\|";
    private static final String OCCUPATION_SEPARATOR = "|";
    private static final int OCCUPATION_PARTS = 2;

    private WorkerProfileMapper() {
        // utility class
    }

    /**
     * Converts a persistence entity to the domain aggregate.
     */
    public static WorkerProfile toDomain(WorkerProfileEntity entity) {
        WorkerProfile profile = WorkerProfile.restore(new WorkerProfileSnapshot(
                WorkerId.fromString(entity.getId()),
                AuthUserId.of(entity.getAuthUserId()),
                entity.getFullName(),
                entity.getPhoneNumber()
        ));
        if (entity.getRawDescription() != null) {
            profile.assignRawDescription(
                    RawDescription.of(entity.getRawDescription())
            );
        }
        deserializeOccupations(profile, entity.getOccupations());
        deserializeSkills(profile, entity.getTechnicalSkills());
        return profile;
    }

    /**
     * Converts the domain aggregate to a persistence entity.
     */
    public static WorkerProfileEntity toEntity(WorkerProfile profile) {
        WorkerProfileEntity entity = new WorkerProfileEntity();
        updateEntity(entity, profile);
        return entity;
    }

    /**
     * Updates a persistence entity from the domain aggregate.
     * Used for both create and update operations.
     */
    public static void updateEntity(WorkerProfileEntity target, WorkerProfile source) {
        target.setId(source.getId().asString());
        target.setAuthUserId(source.getAuthUserId().value());
        target.setFullName(source.getFullName());
        target.setPhoneNumber(source.getPhoneNumber());
        if (source.getRawDescription() != null) {
            target.setRawDescription(source.getRawDescription().getText());
        }
        target.setOccupations(serializeOccupations(source));
        target.setTechnicalSkills(serializeSkills(source));
    }

    private static List<String> serializeOccupations(WorkerProfile profile) {
        return new ArrayList<>(profile.getOccupations()
                .stream()
                .map(o -> o.getTradeName() + OCCUPATION_SEPARATOR + o.getLevel().name())
                .toList());
    }

    private static List<String> serializeSkills(WorkerProfile profile) {
        return new ArrayList<>(profile.getTechnicalSkills()
                .stream()
                .map(TechnicalSkill::getSkillName)
                .toList());
    }

    private static void deserializeOccupations(WorkerProfile profile, List<String> data) {
        if (data == null) {
            return;
        }
        for (String occupationData : data) {
            String[] parts = occupationData.split(OCCUPATION_DELIMITER);
            if (parts.length == OCCUPATION_PARTS) {
                ExpertiseLevel level = parseLevel(parts[1]);
                profile.addOccupation(Occupation.of(parts[0], level));
            }
        }
    }

    private static void deserializeSkills(WorkerProfile profile, List<String> skillNames) {
        if (skillNames == null) {
            return;
        }
        for (String skillName : skillNames) {
            profile.addTechnicalSkill(TechnicalSkill.of(skillName));
        }
    }

    private static ExpertiseLevel parseLevel(String levelStr) {
        try {
            return ExpertiseLevel.valueOf(levelStr);
        } catch (IllegalArgumentException e) {
            return ExpertiseLevel.INTERMEDIATE;
        }
    }
}
