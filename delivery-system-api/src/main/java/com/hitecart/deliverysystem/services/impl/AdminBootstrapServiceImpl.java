package com.hitecart.deliverysystem.services.impl;


import com.hitecart.deliverysystem.dao.RoleRepository;
import com.hitecart.deliverysystem.dao.UserRepository;
import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.User;
import com.hitecart.deliverysystem.model.enums.RoleType;
import com.hitecart.deliverysystem.services.AdminBootstrapService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminBootstrapServiceImpl implements AdminBootstrapService {

    private static final int SYSTEM_ACTOR_ID = 0;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createFirstAdmin(String email, String firstname, String lastname, char[] rawPassword) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        Role adminRole = roleRepository
                .findByNameForUpdate(RoleType.ROLE_ADMIN)
                .orElseThrow(()->new IllegalStateException(
                        "Le role ROLE_ADMIN doit etre initialisié avant le bootstrap."
                ));
        if(userRepository.existByRole(RoleType.ROLE_ADMIN)){
            throw new IllegalStateException(
                    "Un administrateur exixte deja. Bootstrap refisé."
            );
        }
        if(Boolean.TRUE.equals(
                userRepository.existsByEmailIgnoreCase(normalizedEmail)
        )){
            throw new IllegalStateException(
                    "Cette addresse email est deja utilisé"
            );
        }
        if(rawPassword == null || rawPassword.length <8){
            throw new IllegalStateException(
                    "Le mot de passe doit contenir au moins 8 caractères"
            );
        }
        User admin = User.builder()
                .email(normalizedEmail)
                .firstname(firstname.trim())
                .lastname(lastname.trim())
                .password(passwordEncoder.encode(new String(rawPassword)))
                .roles(List.of(adminRole))
                .enabled(true)
                .accountLocked(false)
                .createdBy(SYSTEM_ACTOR_ID)
                .build();
    }
}
