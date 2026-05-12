package com.veritrabajo.backend.identityaccess.application.service;

import com.veritrabajo.backend.identityaccess.application.dto.AuthResponse;
import com.veritrabajo.backend.identityaccess.application.dto.AuthUserView;
import com.veritrabajo.backend.identityaccess.application.dto.LoginRequest;
import com.veritrabajo.backend.identityaccess.application.dto.RegisterAuthRequest;
import com.veritrabajo.backend.identityaccess.domain.event.UserRegistered;
import com.veritrabajo.backend.identityaccess.domain.model.AuthUser;
import com.veritrabajo.backend.identityaccess.domain.model.Email;
import com.veritrabajo.backend.identityaccess.domain.model.Password;
import com.veritrabajo.backend.identityaccess.domain.model.Role;
import com.veritrabajo.backend.identityaccess.domain.port.AuthUserRepository;
import com.veritrabajo.backend.identityaccess.infrastructure.security.JwtProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final ApplicationEventPublisher eventPublisher;

    public AuthService(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            ApplicationEventPublisher eventPublisher
    ) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public AuthResponse register(RegisterAuthRequest request) {
        Email email = Email.of(request.getEmail());
        Password password = Password.of(request.getPassword());
        Role role = Role.from(request.getRole());

        if (authUserRepository.existsByEmail(email.value())) {
            throw new IllegalStateException("Email is already registered");
        }

        String passwordHash = passwordEncoder.encode(password.value());
        AuthUser created = authUserRepository.save(AuthUser.register(email, passwordHash, role));

        UserRegistered domainEvent = created.registrationCompleted();
        eventPublisher.publishEvent(domainEvent);

        List<String> roles = created.getRoles().stream().map(Role::name).toList();
        String token = jwtProvider.generateToken(created.getId(), created.getEmail(), roles);
        return AuthResponse.from(toView(created, roles), token);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Email email = Email.of(request.getEmail());
        Password password = Password.of(request.getPassword());

        AuthUser user = authUserRepository.findByEmail(email.value())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!user.verifyPassword(password.value(), passwordEncoder::matches)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        List<String> roles = user.getRoles().stream().map(Role::name).toList();
        String token = jwtProvider.generateToken(user.getId(), user.getEmail(), roles);
        return AuthResponse.from(toView(user, roles), token);
    }

    private AuthUserView toView(
            AuthUser user,
            List<String> roles
    ) {
        return new AuthUserView(user.getId(), user.getEmail(), roles);
    }
}
