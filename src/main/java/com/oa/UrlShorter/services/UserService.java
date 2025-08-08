package com.oa.UrlShorter.services;

import com.oa.UrlShorter.DTOs.CreateUserCmd;
import com.oa.UrlShorter.models.Role;
import com.oa.UrlShorter.models.User;
import com.oa.UrlShorter.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createUser(CreateUserCmd cmd) {
        if (userRepository.existsByEmail(cmd.email())) {
            throw new RuntimeException("Email already exists");
        }
        var user = new User();
        user.setEmail(cmd.email());
        user.setPassword(passwordEncoder.encode(cmd.password()));
        user.setName(cmd.name());
        user.setRole(Role.ROLE_USER);
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }
}
