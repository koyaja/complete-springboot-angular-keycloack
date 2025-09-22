package com.example.keycloak.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service pour la gestion et extraction des données JWT
 * Vidéo 6 : Support pour les endpoints protégés
 * 
 * Ce service centralise la logique d'extraction des claims JWT
 * pour éviter la duplication de code dans les controllers
 */
@Service
public class JwtService {

    /**
     * Extrait le username du token JWT
     * 
     * @param jwt Token JWT
     * @return Username ou email selon la configuration Keycloak
     */
    public String extractUsername(Jwt jwt) {
        // Priorité : preferred_username > email > sub
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null || username.isEmpty()) {
            username = jwt.getClaimAsString("email");
        }
        if (username == null || username.isEmpty()) {
            username = jwt.getSubject();
        }
        return username;
    }

    /**
     * Extrait les rôles du realm depuis le token JWT
     * 
     * @param jwt Token JWT
     * @return Liste des rôles ou liste vide
     */
    @SuppressWarnings("unchecked")
    public List<String> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            return (List<String>) realmAccess.get("roles");
        }
        return List.of();
    }

    /**
     * Extrait les rôles du client depuis le token JWT
     * 
     * @param jwt Token JWT
     * @param clientId ID du client Keycloak
     * @return Liste des rôles client ou liste vide
     */
    @SuppressWarnings("unchecked")
    public List<String> extractClientRoles(Jwt jwt, String clientId) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey(clientId)) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(clientId);
            if (clientAccess != null && clientAccess.containsKey("roles")) {
                return (List<String>) clientAccess.get("roles");
            }
        }
        return List.of();
    }

    /**
     * Vérifie si l'utilisateur possède un rôle spécifique
     * 
     * @param jwt Token JWT
     * @param role Rôle à vérifier
     * @return true si l'utilisateur possède le rôle
     */
    public boolean hasRealmRole(Jwt jwt, String role) {
        return extractRealmRoles(jwt).contains(role);
    }

    /**
     * Vérifie si l'utilisateur possède le rôle ADMIN
     * 
     * @param jwt Token JWT
     * @return true si l'utilisateur est admin
     */
    public boolean isAdmin(Jwt jwt) {
        return hasRealmRole(jwt, "ADMIN") || hasRealmRole(jwt, "admin");
    }

    /**
     * Extrait les informations complètes de l'utilisateur
     * 
     * @param jwt Token JWT
     * @return Map contenant toutes les informations utilisateur
     */
    public Map<String, Object> extractUserInfo(Jwt jwt) {
        return Map.of(
            "username", extractUsername(jwt),
            "email", jwt.getClaimAsString("email") != null ? jwt.getClaimAsString("email") : "",
            "name", jwt.getClaimAsString("name") != null ? jwt.getClaimAsString("name") : "",
            "given_name", jwt.getClaimAsString("given_name") != null ? jwt.getClaimAsString("given_name") : "",
            "family_name", jwt.getClaimAsString("family_name") != null ? jwt.getClaimAsString("family_name") : "",
            "realm_roles", extractRealmRoles(jwt),
            "token_id", jwt.getId() != null ? jwt.getId() : "",
            "issued_at", jwt.getIssuedAt() != null ? jwt.getIssuedAt().toString() : "",
            "expires_at", jwt.getExpiresAt() != null ? jwt.getExpiresAt().toString() : ""
        );
    }

    /**
     * Vérifie si le token est proche de l'expiration
     * 
     * @param jwt Token JWT
     * @param secondsBeforeExpiration Nombre de secondes avant expiration
     * @return true si le token expire dans le délai spécifié
     */
    public boolean isTokenExpiringSoon(Jwt jwt, long secondsBeforeExpiration) {
        if (jwt.getExpiresAt() == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis() / 1000;
        long expirationTime = jwt.getExpiresAt().getEpochSecond();
        
        return (expirationTime - currentTime) <= secondsBeforeExpiration;
    }
}