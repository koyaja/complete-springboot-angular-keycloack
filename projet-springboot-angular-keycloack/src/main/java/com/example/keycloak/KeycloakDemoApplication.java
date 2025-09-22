package com.example.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application principale Spring Boot pour la démonstration Keycloak
 * 
 * Cette classe est le point d'entrée de l'application.
 * Elle configure automatiquement Spring Boot avec OAuth2 Resource Server
 * pour l'intégration avec Keycloak.
 */
@SpringBootApplication
public class KeycloakDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeycloakDemoApplication.class, args);
	}

}