package com.example.keycloak;

import com.example.keycloak.controller.DemoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests unitaires pour la configuration Spring Security
 * Vidéo 6 : Validation de l'intégration OAuth2/JWT
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test : Les endpoints publics sont accessibles sans authentification
     */
    @Test
    public void testPublicEndpoint_ShouldBeAccessible_WithoutAuth() throws Exception {
        mockMvc.perform(get("/api/public/hello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Endpoint public - Aucune authentification requise"))
                .andExpect(jsonPath("$.security").value("PUBLIC"));
    }

    /**
     * Test : Health check accessible sans authentification
     */
    @Test
    public void testHealthEndpoint_ShouldBeAccessible_WithoutAuth() throws Exception {
        mockMvc.perform(get("/api/public/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.keycloak_integration").value(true));
    }

    /**
     * Test : Les endpoints protégés nécessitent une authentification
     */
    @Test
    public void testProtectedEndpoint_ShouldReturn401_WithoutAuth() throws Exception {
        mockMvc.perform(get("/api/private/user"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Test : Les endpoints protégés sont accessibles avec JWT
     */
    @Test
    public void testProtectedEndpoint_ShouldReturn200_WithJWT() throws Exception {
        mockMvc.perform(get("/api/private/user")
                .with(jwt().jwt(jwt -> jwt
                        .claim("preferred_username", "testuser")
                        .claim("email", "test@example.com")
                        .claim("name", "Test User")
                        .claim("realm_access", java.util.Map.of("roles", java.util.List.of("USER")))
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Endpoint protégé - Authentification réussie !"));
    }

    /**
     * Test : L'endpoint admin nécessite le rôle ADMIN
     */
    @Test
    public void testAdminEndpoint_ShouldReturn403_WithUserRole() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard")
                .with(jwt().jwt(jwt -> jwt
                        .claim("preferred_username", "user1")
                        .claim("realm_access", java.util.Map.of("roles", java.util.List.of("USER")))
                )))
                .andExpect(status().isForbidden());
    }

    /**
     * Test : L'endpoint admin est accessible avec le rôle ADMIN
     */
    @Test
    public void testAdminEndpoint_ShouldReturn200_WithAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard")
                .with(jwt().jwt(jwt -> jwt
                        .claim("preferred_username", "admin1")
                        .claim("realm_access", java.util.Map.of("roles", java.util.List.of("ADMIN", "USER")))
                ).authorities(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_level").value("ADMIN"));
    }

    /**
     * Test : CORS est configuré correctement
     */
    @Test
    public void testCorsConfiguration_ShouldAcceptAngularOrigin() throws Exception {
        mockMvc.perform(post("/api/public/cors-test")
                .header("Origin", "http://localhost:4200")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"test\": \"data\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("CORS configuré avec succès"))
                .andExpect(jsonPath("$.cors_origin").value("http://localhost:4200"));
    }

    /**
     * Test : Les détails d'authentification sont accessibles
     */
    @Test
    @WithMockUser(username = "testuser", authorities = {"ROLE_USER"})
    public void testAuthDetails_ShouldReturnAuthenticationInfo() throws Exception {
        mockMvc.perform(get("/api/private/auth-details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_authenticated").value(true))
                .andExpect(jsonPath("$.principal_name").value("testuser"));
    }

    /**
     * Test : Actuator health endpoint est accessible
     */
    @Test
    public void testActuatorHealth_ShouldBeAccessible_WithoutAuth() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }
}