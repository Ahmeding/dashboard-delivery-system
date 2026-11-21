package com.hitecart.deliverysystem.runner;

import com.hitecart.deliverysystem.services.AdminBootstrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AdminBootstrapRunner implements CommandLineRunner {
    private final AdminBootstrapService bootstrapService;

    @Override
    public void run(String... args) {
        if (!Arrays.asList(args).contains("--bootstrap-admin")) {
            return;
        }

        Console console = System.console();
        if (console == null) {
            throw new IllegalStateException(
                    "Lance le bootstrap depuis un terminal interactif."
            );
        }

        String email = console.readLine("E-mail du premier administrateur : ");
        String firstname = console.readLine("Prénom : ");
        String lastname = console.readLine("Nom : ");
        char[] password = console.readPassword("Mot de passe : ");
        char[] confirmation = console.readPassword("Confirmer le mot de passe : ");

        try {
            if (!Arrays.equals(password, confirmation)) {
                throw new IllegalArgumentException(
                        "Les deux mots de passe ne correspondent pas."
                );
            }

            bootstrapService.createFirstAdmin(
                    email, firstname, lastname, password
            );

            console.printf("Premier administrateur créé.%n");
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(confirmation, '\0');
        }
    }
}
