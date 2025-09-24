package com.example.keycloak.controller;

import com.example.keycloak.config.JwtAuthenticationConverter;
import com.example.keycloak.service.JwtService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

//import javax.annotation.security.RolesAllowed;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller démontrant les fonctionnalités avancées de Spring Security
 * Vidéo 7 : Endpoints sécurisés avancés
 * 
 * Ce controller illustre :
 * - @PreAuthorize avec expressions SpEL
 * - @RolesAllowed avec JSR-250
 * - Validation de rôles spécifiques
 * - Extraction et conversion des authorities
 */
@RestController
@RequestMapping("/api/advanced")
@CrossOrigin(origins = "http://localhost:4200")
public class AdvancedSecurityController {

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;

    /**
     * Endpoint nécessitant le rôle ADMIN spécifique
     * Utilise @PreAuthorize pour une validation fine
     */
    @GetMapping("/admin-only")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> adminOnly(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Accès réservé aux administrateurs");
        response.put("user", jwtService.extractUsername(jwt));
        response.put("roles", jwtService.extractRealmRoles(jwt));
        response.put("access_level", "ADMIN");
        response.put("timestamp", Instant.now().toString());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint avec validation de rôles multiples
     * Accessible aux ADMIN ou MANAGER
     */
    @GetMapping("/management")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Map<String, Object>> managementArea(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Zone de gestion - Accès ADMIN ou MANAGER");
        response.put("user_info", jwtService.extractUserInfo(jwt));
        response.put("allowed_roles", new String[]{"ADMIN", "MANAGER"});
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint avec validation du nom d'utilisateur
     * L'utilisateur ne peut accéder qu'à ses propres données
     */
    @GetMapping("/profile/{username}")
    @PreAuthorize("#username == authentication.name or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> userProfile(
            @PathVariable String username, 
            @AuthenticationPrincipal Jwt jwt) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profil utilisateur - Accès autorisé");
        response.put("requested_user", username);
        response.put("current_user", jwt.getClaimAsString("preferred_username"));
        response.put("is_admin", jwtService.isAdmin(jwt));
        response.put("profile_data", Map.of(
            "username", username,
            "last_login", Instant.now().minusSeconds(3600).toString(),
            "status", "active"
        ));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint utilisant @RolesAllowed (JSR-250)
     * Alternative à @PreAuthorize pour la compatibilité Java EE
     */
    @GetMapping("/reports")
    @RolesAllowed({"ADMIN", "MANAGER", "ANALYST"})
    public ResponseEntity<Map<String, Object>> reports(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Accès aux rapports - Rôles multiples acceptés");
        response.put("user", jwtService.extractUsername(jwt));
        response.put("available_reports", new String[]{
            "User Activity Report",
            "System Performance Report", 
            "Security Audit Report"
        });
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint avec expression SpEL complexe
     * Validation basée sur l'heure et le rôle
     */
    @GetMapping("/maintenance")
    @PreAuthorize("hasRole('ADMIN') and @customSecurityService.isMaintenanceHour()")
    public ResponseEntity<Map<String, Object>> maintenanceMode(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Mode maintenance - Accès pendant heures autorisées");
        response.put("admin_user", jwtService.extractUsername(jwt));
        response.put("maintenance_window", "02:00-06:00 UTC");
        response.put("current_time", Instant.now().toString());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de debug pour analyser l'authentification
     * Montre les rôles extraits et convertis
     */
    @GetMapping("/debug-auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> debugAuthentication(
            Authentication authentication, 
            @AuthenticationPrincipal Jwt jwt) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Debug des informations d'authentification");
        
        // Informations JWT brutes
        response.put("jwt_claims", Map.of(
            "iss", jwt.getIssuer().toString(),
            "sub", jwt.getSubject(),
            "exp", jwt.getExpiresAt().toString(),
            "iat", jwt.getIssuedAt().toString(),
            "preferred_username", jwt.getClaimAsString("preferred_username")
        ));
        
        // Rôles extraits par notre service
        response.put("extracted_roles", jwtService.extractRealmRoles(jwt));
        
        // Authorities Spring Security
        response.put("spring_authorities", authentication.getAuthorities());
        
        // Résumé complet via notre converter
        response.put("roles_summary", jwtAuthenticationConverter.getRolesSummary(jwt));
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint avec validation personnalisée
     * Exemple d'expression SpEL avancée
     */
    @PostMapping("/sensitive-operation")
    @PreAuthorize("hasRole('ADMIN') and @jwtService.isAdmin(#jwt) and !@jwtService.isTokenExpiringSoon(#jwt, 300)")
    public ResponseEntity<Map<String, Object>> sensitiveOperation(
            @RequestBody Map<String, Object> operationData,
            @AuthenticationPrincipal Jwt jwt) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Opération sensible exécutée");
        response.put("admin", jwtService.extractUsername(jwt));
        response.put("operation", operationData.get("operation"));
        response.put("validation_passed", true);
        response.put("security_checks", Map.of(
            "admin_role", true,
            "token_fresh", !jwtService.isTokenExpiringSoon(jwt, 300),
            "operation_logged", true
        ));
        
        return ResponseEntity.ok(response);
    }
}