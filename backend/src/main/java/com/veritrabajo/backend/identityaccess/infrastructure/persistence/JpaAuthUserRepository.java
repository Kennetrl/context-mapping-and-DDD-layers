package com.veritrabajo.backend.identityaccess.infrastructure.persistence;

import com.veritrabajo.backend.identityaccess.domain.model.AuthUser;
import com.veritrabajo.backend.identityaccess.domain.port.AuthUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaAuthUserRepository implements AuthUserRepository {

    private final SpringDataAuthUserRepository springRepository;

    public JpaAuthUserRepository(SpringDataAuthUserRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public AuthUser save(AuthUser user) {
        AuthUserEntity entity = springRepository.findById(user.getId())
                .orElseGet(AuthUserEntity::new);
        AuthUserMapper.updateEntity(entity, user);
        AuthUserEntity saved = springRepository.save(entity);
        return AuthUserMapper.toDomain(saved);
    }

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return springRepository.findByEmail(email)
                .map(AuthUserMapper::toDomain);
    }

    @Override
    public Optional<AuthUser> findById(String id) {
        return springRepository.findById(id)
                .map(AuthUserMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springRepository.existsByEmail(email);
    }
}
