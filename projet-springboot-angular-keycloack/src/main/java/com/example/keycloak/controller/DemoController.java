package com.example.keycloak.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller REST de démonstration avec endpoints sécurisés
 * Vidéo 6 : Test de la configuration Spring Security
 * 
 * Cette classe contient des endpoints publics et protégés
 * pour valider l'intégration OAuth2/JWT avec Keycloak
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class DemoController {

    /**
     * Endpoint public de test
     * Accessible sans authentification
     * 
     * @return Message de bienvenue avec statut et timestamp
     */
    @GetMapping("/public/hello")
    public ResponseEntity<Map<String, Object>> publicEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Endpoint public - Aucune authentification requise");
        response.put("status", "OK");
        response.put("timestamp", Instant.now().toEpochMilli());
        response.put("version", "2.0.0");
        response.put("security", "PUBLIC");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de santé/health check
     * Accessible sans authentification
     * 
     * @return Statut de l'application avec informations de service
     */
    @GetMapping("/public/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "keycloak-demo");
        health.put("port", 8081);
        health.put("keycloak_integration", true);
        health.put("security_enabled", true);
        health.put("timestamp", Instant.now().toString());
        
        return ResponseEntity.ok(health);
    }

    /**
     * Endpoint protégé nécessitant une authentification JWT
     * Retourne les informations de l'utilisateur authentifié
     * 
     * @param jwt Token JWT de l'utilisateur authentifié
     * @return Informations extraites du token JWT
     */
    @GetMapping("/private/user")
    public ResponseEntity<Map<String, Object>> userInfo(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Endpoint protégé - Authentification réussie !");
        response.put("username", jwt.getClaimAsString("preferred_username"));
        response.put("email", jwt.getClaimAsString("email"));
        response.put("name", jwt.getClaimAsString("name"));
        
        // Extraction des rôles depuis realm_access
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            response.put("roles", realmAccess.get("roles"));
        }
        
        response.put("token_issued_at", jwt.getIssuedAt());
        response.put("token_expires_at", jwt.getExpiresAt());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint protégé avec détails de l'authentification
     * Montre comment accéder aux détails de sécurité Spring
     * 
     * @param authentication Objet Authentication Spring Security
     * @return Détails de l'authentification courante
     */
    @GetMapping("/private/auth-details")
    public ResponseEntity<Map<String, Object>> authenticationDetails(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("is_authenticated", authentication.isAuthenticated());
        response.put("principal_name", authentication.getName());
        response.put("authorities", authentication.getAuthorities().stream()
            .map(auth -> auth.getAuthority())
            .collect(Collectors.toList()));
        response.put("credentials", "JWT Token (hidden)");
        response.put("details", authentication.getDetails());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint d'administration (nécessite le rôle ADMIN)
     * Sera développé en détail dans la vidéo 7
     * 
     * @param jwt Token JWT avec rôle ADMIN
     * @return Message de confirmation pour les admins
     */
    @GetMapping("/admin/dashboard")
    public ResponseEntity<Map<String, Object>> adminDashboard(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bienvenue dans l'espace d'administration");
        response.put("admin_user", jwt.getClaimAsString("preferred_username"));
        response.put("access_level", "ADMIN");
        response.put("note", "Endpoint réservé aux administrateurs (vidéo 7)");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de test pour valider la configuration CORS
     * Utilisé pour tester les requêtes depuis Angular
     * 
     * @return Confirmation que CORS est configuré
     */
    @PostMapping("/public/cors-test")
    public ResponseEntity<Map<String, Object>> corsTest(@RequestBody Map<String, Object> payload) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "CORS configuré avec succès");
        response.put("received_data", payload);
        response.put("cors_origin", "http://localhost:4200");
        response.put("timestamp", Instant.now().toString());
        
        return ResponseEntity.ok(response);
    }
}