package com.example.keycloak.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Converter JWT personnalisé pour extraire les rôles Keycloak
 * Vidéo 7 : Configuration avancée des autorités Spring Security
 * 
 * Ce converter transforme les claims JWT de Keycloak en authorities Spring Security
 * Il extrait les rôles depuis realm_access et resource_access pour les convertir
 * au format attendu par Spring Security (ROLE_prefix)
 */
@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    /**
     * Convertit un JWT en AbstractAuthenticationToken avec les bonnes authorities
     * 
     * @param jwt Token JWT depuis Keycloak
     * @return JwtAuthenticationToken avec les authorities extraites
     */
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * Extrait les authorities depuis les claims JWT Keycloak
     * 
     * @param jwt Token JWT
     * @return Collection des authorities Spring Security
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Set<String> roles = new HashSet<>();
        
        // 1. Extraire les rôles du realm (realm_access.roles)
        roles.addAll(extractRealmRoles(jwt));
        
        // 2. Extraire les rôles des clients (resource_access.{client}.roles)
        roles.addAll(extractClientRoles(jwt));
        
        // 3. Convertir en authorities Spring Security avec le préfixe ROLE_
        return roles.stream()
                .map(role -> "ROLE_" + role.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Extrait les rôles du realm depuis realm_access.roles
     * 
     * @param jwt Token JWT
     * @return Set des rôles du realm
     */
    @SuppressWarnings("unchecked")
    private Set<String> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            List<String> roles = (List<String>) realmAccess.get("roles");
            return new HashSet<>(roles);
        }
        return Collections.emptySet();
    }

    /**
     * Extrait les rôles des clients depuis resource_access.{client}.roles
     * 
     * @param jwt Token JWT
     * @return Set des rôles des clients
     */
    @SuppressWarnings("unchecked")
    private Set<String> extractClientRoles(Jwt jwt) {
        Set<String> clientRoles = new HashSet<>();
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        
        if (resourceAccess != null) {
            // Parcourir tous les clients (demo-app, account, etc.)
            for (Map.Entry<String, Object> entry : resourceAccess.entrySet()) {
                String clientId = entry.getKey();
                Object clientAccess = entry.getValue();
                
                if (clientAccess instanceof Map) {
                    Map<String, Object> clientAccessMap = (Map<String, Object>) clientAccess;
                    if (clientAccessMap.containsKey("roles")) {
                        List<String> roles = (List<String>) clientAccessMap.get("roles");
                        
                        // Ajouter le préfixe client pour différencier
                        // Ex: demo-app:MANAGER devient CLIENT_DEMO_APP_MANAGER
                        roles.stream()
                            .map(role -> "CLIENT_" + clientId.toUpperCase().replace("-", "_") + "_" + role.toUpperCase())
                            .forEach(clientRoles::add);
                    }
                }
            }
        }
        
        return clientRoles;
    }

    /**
     * Méthode utilitaire pour extraire tous les rôles (realm + client)
     * Utilisée pour debug et logging
     * 
     * @param jwt Token JWT
     * @return Map avec les détails des rôles extraits
     */
    public Map<String, Object> getRolesSummary(Jwt jwt) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("realm_roles", extractRealmRoles(jwt));
        summary.put("client_roles", extractClientRoles(jwt));
        summary.put("spring_authorities", extractAuthorities(jwt).stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return summary;
    }
}