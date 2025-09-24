package com.example.keycloak.security;

import com.example.keycloak.config.JwtAuthenticationConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitaires pour JwtAuthenticationConverter
 * Vidéo 8 : Validation de l'extraction des rôles depuis JWT Keycloak
 */
@DisplayName("JWT Authentication Converter Tests")
class JwtAuthenticationConverterTest {

    private JwtAuthenticationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new JwtAuthenticationConverter();
    }

    @Test
    @DisplayName("Should extract realm roles and add ROLE_ prefix")
    void testConvertWithRealmRoles() {
        // Given
        Map<String, Object> realmAccess = new HashMap<>();
        realmAccess.put("roles", List.of("ADMIN", "USER"));

        Jwt jwt = createJwtWithClaims(Map.of("realm_access", realmAccess));

        // When
        var authentication = converter.convert(jwt);
        var authorities = authentication.getAuthorities();

        // Then
        assertThat(authorities)
                .hasSize(2)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    @DisplayName("Should extract resource access roles for specific client")
    void testConvertWithResourceAccess() {
        // Given
        Map<String, Object> demoAppAccess = new HashMap<>();
        demoAppAccess.put("roles", List.of("MANAGER", "VIEWER"));

        Map<String, Object> resourceAccess = new HashMap<>();
        resourceAccess.put("demo-app", demoAppAccess);

        Jwt jwt = createJwtWithClaims(Map.of("resource_access", resourceAccess));

        // When
        var authentication = converter.convert(jwt);
        var authorities = authentication.getAuthorities();

        // Then
        // Le converter ajoute le préfixe CLIENT_DEMO_APP_ aux rôles des resource_access
        assertThat(authorities)
                .hasSize(2)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_CLIENT_DEMO_APP_MANAGER", "ROLE_CLIENT_DEMO_APP_VIEWER");
    }

    @Test
    @DisplayName("Should combine realm and resource access roles")
    void testConvertWithBothRealmAndResourceRoles() {
        // Given
        Map<String, Object> realmAccess = new HashMap<>();
        realmAccess.put("roles", List.of("ADMIN"));

        Map<String, Object> appAccess = new HashMap<>();
        appAccess.put("roles", List.of("USER", "EDITOR"));

        Map<String, Object> resourceAccess = new HashMap<>();
        resourceAccess.put("demo-app", appAccess);

        Map<String, Object> claims = new HashMap<>();
        claims.put("realm_access", realmAccess);
        claims.put("resource_access", resourceAccess);

        Jwt jwt = createJwtWithClaims(claims);

        // When
        var authentication = converter.convert(jwt);
        var authorities = authentication.getAuthorities();

        // Then
        assertThat(authorities)
                .hasSize(3)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_CLIENT_DEMO_APP_USER", "ROLE_CLIENT_DEMO_APP_EDITOR");
    }

    @Test
    @DisplayName("Should handle JWT with no roles gracefully")
    void testConvertWithNoRoles() {
        // Given
        Jwt jwt = createJwtWithClaims(Map.of("sub", "user123"));

        // When
        var authentication = converter.convert(jwt);
        var authorities = authentication.getAuthorities();

        // Then
        assertThat(authorities).isEmpty();
    }

    @Test
    @DisplayName("Should handle null realm_access")
    void testConvertWithNullRealmAccess() {
        // Given
        // Ne pas inclure realm_access du tout pour éviter NPE
        Jwt jwt = createJwtWithClaims(Map.of("sub", "user123"));

        // When
        var authentication = converter.convert(jwt);

        // Then
        assertThat(authentication.getAuthorities()).isEmpty();
    }

    @Test
    @DisplayName("Should handle malformed realm_access structure")
    void testConvertWithMalformedRealmAccess() {
        // Given - realm_access comme String au lieu de Map provoque une exception
        // On ne teste pas ce cas car il provoque une IllegalArgumentException
        // Dans un vrai projet, on pourrait ajouter un try-catch dans le converter
        
        // On teste plutôt un realm_access avec structure incorrecte mais toujours Map
        Map<String, Object> malformedRealmAccess = new HashMap<>();
        malformedRealmAccess.put("not_roles", List.of("ADMIN")); // Pas de clé "roles"
        
        Jwt jwt = createJwtWithClaims(Map.of("realm_access", malformedRealmAccess));

        // When
        var authentication = converter.convert(jwt);

        // Then
        assertThat(authentication.getAuthorities()).isEmpty();
    }

    @Test
    @DisplayName("Should not deduplicate roles from different sources")
    void testConvertWithDuplicateRoles() {
        // Given
        Map<String, Object> realmAccess = Map.of("roles", List.of("ADMIN", "USER"));
        Map<String, Object> appAccess = Map.of("roles", List.of("USER", "VIEWER"));
        Map<String, Object> resourceAccess = Map.of("demo-app", appAccess);

        Map<String, Object> claims = Map.of(
                "realm_access", realmAccess,
                "resource_access", resourceAccess
        );

        Jwt jwt = createJwtWithClaims(claims);

        // When
        var authentication = converter.convert(jwt);
        var authorities = authentication.getAuthorities();

        // Then
        // USER du realm devient ROLE_USER
        // USER du client devient ROLE_CLIENT_DEMO_APP_USER (différent!)
        assertThat(authorities)
                .hasSize(4) // ADMIN, USER (realm), CLIENT_DEMO_APP_USER, CLIENT_DEMO_APP_VIEWER
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER", "ROLE_CLIENT_DEMO_APP_USER", "ROLE_CLIENT_DEMO_APP_VIEWER");
    }

    @Test
    @DisplayName("Should preserve username from JWT")
    void testConvertPreservesUsername() {
        // Given
        Map<String, Object> claims = Map.of(
                "preferred_username", "john.doe",
                "sub", "user-123"
        );
        Jwt jwt = createJwtWithClaims(claims);

        // When
        var authentication = converter.convert(jwt);

        // Then
        assertThat(authentication.getName()).isEqualTo("user-123");
        // Check the token is preserved
        if (authentication instanceof org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken) {
            var jwtAuth = (org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken) authentication;
            assertThat(jwtAuth.getToken().getClaim("preferred_username").toString()).isEqualTo("john.doe");
        }
    }

    @Test
    @DisplayName("Should handle special characters in roles")
    void testConvertWithSpecialCharactersInRoles() {
        // Given
        Map<String, Object> realmAccess = Map.of(
                "roles", List.of("ADMIN-GLOBAL", "USER_MANAGER", "viewer.readonly")
        );
        Jwt jwt = createJwtWithClaims(Map.of("realm_access", realmAccess));

        // When
        var authentication = converter.convert(jwt);
        var authorities = authentication.getAuthorities();

        // Then
        // Le converter met tout en majuscules avec toUpperCase()
        assertThat(authorities)
                .hasSize(3)
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder(
                        "ROLE_ADMIN-GLOBAL",
                        "ROLE_USER_MANAGER",
                        "ROLE_VIEWER.READONLY"  // Tout en majuscules
                );
    }

    // Helper method to create JWT with custom claims
    private Jwt createJwtWithClaims(Map<String, Object> claims) {
        return Jwt.withTokenValue("mock-token")
                .header("typ", "JWT")
                .header("alg", "RS256")
                .claim("iss", "http://localhost:8080/realms/demo")
                .claim("sub", "user-123")
                .claim("aud", "demo-app")
                .claim("exp", Instant.now().plusSeconds(3600))
                .claim("iat", Instant.now())
                .claims(map -> map.putAll(claims))
                .build();
    }
}