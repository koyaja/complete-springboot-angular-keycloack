package com.example.keycloak.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Configuration Method Security pour les annotations @PreAuthorize
 * Vidéo 7 : Endpoints sécurisés avancés
 * 
 * Cette configuration active les annotations de sécurité au niveau des méthodes :
 * - @PreAuthorize : Vérification avant l'exécution de la méthode
 * - @PostAuthorize : Vérification après l'exécution de la méthode  
 * - @Secured : Sécurisation basée sur les rôles
 * - @RolesAllowed : Annotation JSR-250 pour les rôles
 */
@Configuration
@EnableMethodSecurity(
    // Active les annotations @PreAuthorize et @PostAuthorize
    prePostEnabled = true,
    
    // Active les annotations @Secured  
    securedEnabled = true,
    
    // Active les annotations JSR-250 comme @RolesAllowed
    jsr250Enabled = true
)
public class MethodSecurityConfig {
    
    // Cette classe active automatiquement la Method Security
    // Les configurations de validation des rôles seront
    // gérées par les annotations directement sur les méthodes
    
    // Exemples d'utilisation :
    // @PreAuthorize("hasRole('ADMIN')")
    // @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    // @PreAuthorize("authentication.name == #username")
    // @Secured("ROLE_ADMIN")
    // @RolesAllowed({"ADMIN", "MANAGER"})
}