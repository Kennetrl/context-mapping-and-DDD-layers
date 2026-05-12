package com.veritrabajo.backend.workerprofile.infrastructure.persistence;

import com.veritrabajo.backend.workerprofile.domain.model.AuthUserId;
import com.veritrabajo.backend.workerprofile.domain.model.WorkerId;
import com.veritrabajo.backend.workerprofile.domain.model.WorkerProfile;
import com.veritrabajo.backend.workerprofile.domain.port.WorkerProfileRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaWorkerProfileRepository implements WorkerProfileRepository {

    private final SpringWorkerProfileRepository springRepository;

    public JpaWorkerProfileRepository(
            SpringWorkerProfileRepository springRepository
    ) {
        this.springRepository = springRepository;
    }

    @Override
    public WorkerProfile save(WorkerProfile profile) {
        WorkerProfileEntity entity = springRepository.findById(profile.getId().asString())
                .orElseGet(WorkerProfileEntity::new);
        WorkerProfileMapper.updateEntity(entity, profile);
        WorkerProfileEntity saved = springRepository.save(entity);
        return WorkerProfileMapper.toDomain(saved);
    }

    @Override
    public Optional<WorkerProfile> findById(WorkerId id) {
        return springRepository.findById(id.asString())
                .map(WorkerProfileMapper::toDomain);
    }

    @Override
    public Optional<WorkerProfile> findByAuthUserId(AuthUserId authUserId) {
        return springRepository.findByAuthUserId(authUserId.value())
                .map(WorkerProfileMapper::toDomain);
    }

    @Override
    public boolean existsByAuthUserId(AuthUserId authUserId) {
        return springRepository.existsByAuthUserId(authUserId.value());
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return springRepository.existsByPhoneNumber(phoneNumber);
    }
}
