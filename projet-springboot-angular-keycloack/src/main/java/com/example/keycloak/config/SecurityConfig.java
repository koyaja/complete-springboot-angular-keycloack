package com.example.keycloak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuration Spring Security pour l'intégration OAuth2 avec Keycloak
 * Vidéo 6 : Configuration complète Spring Security
 * 
 * Cette classe configure :
 * - La protection des endpoints via OAuth2 Resource Server
 * - La validation automatique des tokens JWT Keycloak
 * - La configuration CORS pour le frontend Angular
 * - Les règles d'autorisation par patterns d'URL
 * 
 * @author Formation YouTube - Module 2
 * @version 2.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * Configuration de la chaîne de filtres de sécurité Spring Security 6
     * Utilise le nouveau pattern avec SecurityFilterChain Bean (Spring Security 5.7+)
     * 
     * @param http Configuration HttpSecurity
     * @return SecurityFilterChain configurée
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Configuration des règles d'autorisation
            .authorizeHttpRequests(authz -> authz
                // Endpoints publics accessibles sans authentification
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/info").permitAll()
                
                // Endpoints d'administration (préparation vidéo 7)
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Tous les autres endpoints nécessitent une authentification
                .anyRequest().authenticated()
            )
            
            // Configuration OAuth2 Resource Server avec validation JWT
            // Connexion automatique à Keycloak via issuer-uri dans application.yml
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            )
            
            // Configuration CORS pour permettre les requêtes depuis Angular
            .cors(Customizer.withDefaults())
            
            // Désactivation CSRF pour API REST stateless
            // JWT est stateless, pas besoin de protection CSRF
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
    
    /**
     * Configuration CORS pour permettre les requêtes depuis le frontend Angular
     * Configuration permissive pour le développement
     * À restreindre en production (vidéo Module 4)
     * 
     * @return CorsConfigurationSource avec les règles CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Origins autorisées (Angular sur port 4200)
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",  // Angular dev server
            "http://localhost:8081",  // Spring Boot self
            "http://localhost:8080"   // Keycloak server
        ));
        
        // Méthodes HTTP autorisées
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // Headers autorisés (incluant Authorization pour JWT)
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Headers exposés au frontend
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        
        // Autoriser les credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Durée de cache du preflight (1 heure)
        configuration.setMaxAge(3600L);
        
        // Enregistrement de la configuration pour tous les endpoints API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        source.registerCorsConfiguration("/actuator/**", configuration);
        
        return source;
    }
}