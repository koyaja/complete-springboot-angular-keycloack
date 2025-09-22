package com.example.keycloak.service;

import com.example.keycloak.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service pour la gestion des utilisateurs
 * 
 * Cette classe contient la logique métier pour les opérations
 * utilisateur. Elle sera enrichie dans les vidéos suivantes
 * avec l'intégration Keycloak.
 */
@Service
public class UserService {

    /**
     * Récupère la liste des utilisateurs (données de test)
     * 
     * @return Liste d'utilisateurs de démonstration
     */
    public List<UserDto> getAllUsers() {
        return Arrays.asList(
            new UserDto("1", "john.doe", "john.doe@example.com", "John Doe"),
            new UserDto("2", "jane.smith", "jane.smith@example.com", "Jane Smith"),
            new UserDto("3", "admin", "admin@example.com", "Administrator")
        );
    }

    /**
     * Récupère un utilisateur par son ID
     * 
     * @param id ID de l'utilisateur
     * @return Utilisateur trouvé ou null
     */
    public UserDto getUserById(String id) {
        return getAllUsers().stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    /**
     * Récupère un utilisateur par son nom d'utilisateur
     * 
     * @param username Nom d'utilisateur
     * @return Utilisateur trouvé ou null
     */
    public UserDto getUserByUsername(String username) {
        return getAllUsers().stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }

    /**
     * Valide si un utilisateur existe
     * 
     * @param username Nom d'utilisateur à valider
     * @return true si l'utilisateur existe
     */
    public boolean userExists(String username) {
        return getUserByUsername(username) != null;
    }

    /**
     * Compte le nombre total d'utilisateurs
     * 
     * @return Nombre d'utilisateurs
     */
    public long getUserCount() {
        return getAllUsers().size();
    }
}