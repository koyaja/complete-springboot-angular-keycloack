package com.example.keycloak.service;

import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * Service personnalisé pour les validations de sécurité complexes
 * Vidéo 7 : Utilisé dans les expressions SpEL @PreAuthorize
 * 
 * Ce service contient la logique métier pour des validations
 * de sécurité qui ne peuvent pas être exprimées simplement
 * avec les annotations standard
 */
@Service("customSecurityService")
public class CustomSecurityService {

    /**
     * Vérifie si nous sommes dans une fenêtre de maintenance autorisée
     * Utilisé dans @PreAuthorize("... and @customSecurityService.isMaintenanceHour()")
     * 
     * @return true si nous sommes dans la fenêtre de maintenance (02:00-06:00)
     */
    public boolean isMaintenanceHour() {
        LocalTime now = LocalTime.now();
        LocalTime maintenanceStart = LocalTime.of(2, 0);  // 02:00
        LocalTime maintenanceEnd = LocalTime.of(6, 0);    // 06:00
        
        return now.isAfter(maintenanceStart) && now.isBefore(maintenanceEnd);
    }

    /**
     * Vérifie si un utilisateur peut effectuer une opération sensible
     * Basé sur différents critères métier
     * 
     * @param username Nom d'utilisateur
     * @param operation Type d'opération
     * @return true si l'opération est autorisée
     */
    public boolean canPerformSensitiveOperation(String username, String operation) {
        // Logique métier personnalisée
        if ("DELETE_ALL_DATA".equals(operation)) {
            // Seuls certains super-admins peuvent supprimer toutes les données
            return "super-admin".equals(username);
        }
        
        if ("BULK_UPDATE".equals(operation)) {
            // Opérations en masse pendant les heures creuses seulement
            return isMaintenanceHour();
        }
        
        return true; // Autres opérations autorisées par défaut
    }

    /**
     * Vérifie si l'utilisateur a accès à un projet spécifique
     * Exemple de validation métier personnalisée
     * 
     * @param username Nom d'utilisateur
     * @param projectId ID du projet
     * @return true si l'utilisateur a accès au projet
     */
    public boolean hasProjectAccess(String username, String projectId) {
        // Dans un vrai projet, ceci interrogerait la base de données
        // Pour la démo, logique simplifiée
        if ("admin".equals(username)) {
            return true; // Les admins ont accès à tout
        }
        
        // Autres utilisateurs : accès basé sur l'ID du projet
        return projectId.startsWith("public-") || 
               projectId.contains(username); // Projets contenant le nom d'utilisateur
    }

    /**
     * Vérifie si l'utilisateur peut voir des données sensibles
     * Basé sur le rôle et l'heure de la demande
     * 
     * @param userRole Rôle de l'utilisateur
     * @return true si l'accès aux données sensibles est autorisé
     */
    public boolean canAccessSensitiveData(String userRole) {
        // Données sensibles accessibles seulement pendant les heures de bureau
        LocalTime now = LocalTime.now();
        LocalTime businessStart = LocalTime.of(8, 0);   // 08:00
        LocalTime businessEnd = LocalTime.of(18, 0);    // 18:00
        
        boolean isBusinessHour = now.isAfter(businessStart) && now.isBefore(businessEnd);
        
        // ADMIN peut toujours accéder, autres rôles seulement pendant heures de bureau
        return "ADMIN".equals(userRole) || 
               ("MANAGER".equals(userRole) && isBusinessHour);
    }
}