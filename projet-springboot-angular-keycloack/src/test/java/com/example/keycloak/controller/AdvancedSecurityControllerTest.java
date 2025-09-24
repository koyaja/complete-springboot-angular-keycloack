package com.example.keycloak.controller;

import com.example.keycloak.service.CustomSecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Tests d'intégration pour les endpoints sécurisés avec @PreAuthorize
 * Vidéo 8 : Validation complète de Method Security
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Advanced Security Controller Integration Tests")
class AdvancedSecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomSecurityService customSecurityService;

    // Tests pour endpoint admin-only
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin endpoint should be accessible by ADMIN role")
    void adminEndpoint_withAdminRole_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/admin-only"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Admin access granted"))
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.roles").isArray());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin endpoint should be forbidden for USER role")
    void adminEndpoint_withUserRole_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/admin-only"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Admin endpoint should return 401 without authentication")
    void adminEndpoint_withoutAuth_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/admin-only"))
                .andExpect(status().isUnauthorized());
    }

    // Tests pour endpoint admin-or-manager
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin-or-manager endpoint should be accessible by ADMIN")
    void adminOrManagerEndpoint_withAdminRole_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/admin-or-manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Admin or Manager access granted"));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("Admin-or-manager endpoint should be accessible by MANAGER")
    void adminOrManagerEndpoint_withManagerRole_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/admin-or-manager"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Admin or Manager access granted"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Admin-or-manager endpoint should be forbidden for USER")
    void adminOrManagerEndpoint_withUserRole_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/admin-or-manager"))
                .andExpect(status().isForbidden());
    }

    // Tests pour profil utilisateur avec expressions SpEL complexes
    @Test
    @WithMockUser(username = "john", roles = "USER")
    @DisplayName("User should access their own profile")
    void userProfile_ownerAccess_shouldSucceed() throws Exception {
        mockMvc.perform(get("/api/profile/john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.message").value("Profile access granted"));
    }

    @Test
    @WithMockUser(username = "john", roles = "USER")
    @DisplayName("User should not access other user's profile")
    void userProfile_otherUserAccess_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/api/profile/alice"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Admin should access any user profile")
    void userProfile_adminAccess_shouldSucceedForAny() throws Exception {
        mockMvc.perform(get("/api/profile/alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.isAdmin").value(true));
    }

    // Tests pour opérations sensibles avec service custom
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Sensitive operation should succeed when maintenance hour and admin")
    void sensitiveOperation_duringMaintenanceAsAdmin_shouldSucceed() throws Exception {
        // Given
        when(customSecurityService.isMaintenanceHour()).thenReturn(true);
        when(customSecurityService.canPerformSensitiveOperation(anyString(), anyString()))
                .thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/sensitive-operation")
                        .param("operation", "BULK_UPDATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Operation completed"))
                .andExpect(jsonPath("$.operation").value("BULK_UPDATE"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Sensitive operation should fail outside maintenance hour")
    void sensitiveOperation_outsideMaintenanceHour_shouldFail() throws Exception {
        // Given
        when(customSecurityService.isMaintenanceHour()).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/sensitive-operation")
                        .param("operation", "BULK_UPDATE"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Sensitive operation should fail for non-admin even during maintenance")
    void sensitiveOperation_asUserDuringMaintenance_shouldFail() throws Exception {
        // Given
        when(customSecurityService.isMaintenanceHour()).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/sensitive-operation")
                        .param("operation", "BULK_UPDATE"))
                .andExpect(status().isForbidden());
    }

    // Tests pour accès aux données sensibles selon l'heure
    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Admin should always access sensitive data")
    void sensitiveData_asAdmin_shouldAlwaysSucceed() throws Exception {
        when(customSecurityService.canAccessSensitiveData("ADMIN")).thenReturn(true);

        mockMvc.perform(get("/api/sensitive-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.classification").value("SENSITIVE"));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("Manager should access sensitive data during business hours")
    void sensitiveData_asManagerDuringBusinessHours_shouldSucceed() throws Exception {
        when(customSecurityService.canAccessSensitiveData("MANAGER")).thenReturn(true);

        mockMvc.perform(get("/api/sensitive-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("Manager should not access sensitive data outside business hours")
    void sensitiveData_asManagerOutsideBusinessHours_shouldFail() throws Exception {
        when(customSecurityService.canAccessSensitiveData("MANAGER")).thenReturn(false);

        mockMvc.perform(get("/api/sensitive-data"))
                .andExpect(status().isForbidden());
    }

    // Tests pour vérifier les expressions @PreAuthorize complexes
    @Test
    @WithMockUser(username = "john", roles = {"USER", "VIEWER"})
    @DisplayName("Combined roles should work with hasAnyRole")
    void multipleRoles_withHasAnyRole_shouldWork() throws Exception {
        mockMvc.perform(get("/api/viewer-or-editor"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "john", roles = "GUEST")
    @DisplayName("Insufficient roles should be rejected")
    void insufficientRoles_shouldBeRejected() throws Exception {
        mockMvc.perform(get("/api/viewer-or-editor"))
                .andExpect(status().isForbidden());
    }

    // Test CORS pour Angular
    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("CORS headers should be present for Angular frontend")
    void corsHeaders_shouldBePresentForAngular() throws Exception {
        mockMvc.perform(options("/api/protected")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().exists("Access-Control-Allow-Methods"));
    }

    // Test de validation des paramètres
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Invalid parameters should return bad request")
    void invalidParameters_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/profile/"))  // Username vide
                .andExpect(status().isNotFound());
    }
}