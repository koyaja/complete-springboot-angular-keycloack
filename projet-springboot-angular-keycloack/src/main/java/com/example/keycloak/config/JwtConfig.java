package com.example.keycloak.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration personnalisée pour JWT Decoder
 * Permet de gérer les problèmes d'audience avec Keycloak
 * 
 * Vidéo 6 : Configuration flexible pour différents setups Keycloak
 */
@Configuration
public class JwtConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.audiences:#{null}}")
    private String audiences;

    /**
     * Configuration personnalisée du JWT Decoder
     * Permet de désactiver la validation d'audience si nécessaire
     * 
     * @return JwtDecoder configuré pour Keycloak
     */
    @Bean
    @ConditionalOnProperty(
        name = "spring.security.oauth2.resourceserver.jwt.audiences",
        matchIfMissing = true
    )
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
        
        // Créer une liste de validateurs
        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
        
        // Toujours valider l'issuer
        validators.add(new JwtIssuerValidator(issuerUri));
        
        // Toujours valider le timestamp
        validators.add(new JwtTimestampValidator());
        
        // Valider l'audience seulement si configurée
        if (audiences != null && !audiences.trim().isEmpty()) {
            // Si l'audience est configurée, l'ajouter aux validateurs
            // Note: Dans un cas réel, vous pourriez créer un AudienceValidator personnalisé
            validators.add(token -> {
                List<String> tokenAudiences = token.getAudience();
                if (tokenAudiences != null && tokenAudiences.contains(audiences)) {
                    return OAuth2TokenValidatorResult.success();
                }
                // Si pas d'audience dans le token, on accepte quand même
                // (pour gérer les différentes configurations Keycloak)
                if (tokenAudiences == null || tokenAudiences.isEmpty()) {
                    return OAuth2TokenValidatorResult.success();
                }
                OAuth2Error error = new OAuth2Error("invalid_audience", 
                    "The required audience is missing: " + audiences, null);
                return OAuth2TokenValidatorResult.failure(error);
            });
        }
        
        // Combiner tous les validateurs
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(validators);
        jwtDecoder.setJwtValidator(validator);
        
        return jwtDecoder;
    }
}