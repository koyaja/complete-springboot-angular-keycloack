package com.example.keycloak.dto;

/**
 * Data Transfer Object pour les utilisateurs
 * 
 * Cette classe représente un utilisateur dans notre API.
 * Elle sera enrichie avec les informations Keycloak dans les vidéos suivantes.
 */
public class UserDto {
    
    private String id;
    private String username;
    private String email;
    private String fullName;

    /**
     * Constructeur par défaut
     */
    public UserDto() {
    }

    /**
     * Constructeur avec tous les paramètres
     * 
     * @param id ID unique de l'utilisateur
     * @param username Nom d'utilisateur
     * @param email Adresse email
     * @param fullName Nom complet
     */
    public UserDto(String id, String username, String email, String fullName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters et Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDto userDto = (UserDto) o;

        return id != null ? id.equals(userDto.id) : userDto.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}