# 🚀 Keycloak Demo - Setup Spring Boot

## 📋 Description
Projet de démonstration pour la vidéo 5 de la formation **Keycloak + Spring Boot + Angular**.

Cette application illustre la création d'un backend Spring Boot optimisé pour l'authentification OAuth2 avec Keycloak.

## 🎯 Objectif Vidéo 5
- Créer un projet Spring Boot avec les bonnes dépendances OAuth2
- Configurer la structure de packages professionnelle
- Préparer l'intégration Keycloak (finalisée en vidéo 6)

## 🛠️ Prérequis
- **Java 17+** (obligatoire pour Spring Boot 3.x)
- **Maven 3.8+**
- **IDE** (IntelliJ IDEA ou VSCode recommandé)
- **Keycloak** configuré (vidéo 4) - optionnel pour cette vidéo

## 📦 Dépendances Principales
- **Spring Boot 3.2.x** - Framework principal
- **Spring Web** - Controllers REST
- **Spring Security** - Sécurisation de l'application
- **OAuth2 Resource Server** - Validation JWT Keycloak
- **Spring Boot DevTools** - Développement (optionnel)

## 🏗️ Structure du Projet

```
src/main/java/com/example/keycloak/
├── KeycloakDemoApplication.java     # Point d'entrée
├── config/
│   └── SecurityConfig.java         # Configuration sécurité (temporaire)
├── controller/
│   └── DemoController.java         # Endpoints REST
├── service/
│   └── UserService.java            # Logique métier
└── dto/
    └── UserDto.java                 # Objets de transfert
```

## 🚀 Démarrage

### 1. Cloner/Télécharger le projet
```bash
# Le projet est créé via Spring Initializr dans la vidéo
```

### 2. Compiler et démarrer
```bash
# Avec Maven
mvn spring-boot:run

# Ou avec votre IDE
# Clic droit -> Run 'KeycloakDemoApplication'
```

### 3. Vérifier le démarrage
L'application démarre sur **http://localhost:8081**

## 🧪 Endpoints de Test

### Endpoint Public
```
GET http://localhost:8081/api/public
```
**Réponse:**
```json
{
  "message": "Application Spring Boot démarrée avec succès !",
  "status": "OK",
  "timestamp": 1234567890,
  "version": "1.0.0"
}
```

### Endpoint Santé
```
GET http://localhost:8081/api/health
```
**Réponse:**
```json
{
  "status": "UP",
  "service": "keycloak-demo",
  "port": 8081,
  "ready_for_keycloak": true
}
```

### Endpoint Protégé (Temporaire)
```
GET http://localhost:8081/api/protected
```
**Note:** Cet endpoint sera sécurisé avec Keycloak en vidéo 6

## ⚙️ Configuration

### application.yml
```yaml
server:
  port: 8081

spring:
  application:
    name: keycloak-demo
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/demo
```

### Points Importants
- **Port 8081** : Évite les conflits avec Keycloak (port 8080)
- **issuer-uri** : Pointe vers le realm "demo" configuré en vidéo 4
- **Logs DEBUG** : Activés pour Spring Security (développement)

## 📊 Monitoring

### Actuator Endpoints
- **Health Check**: http://localhost:8081/actuator/health
- **Info**: http://localhost:8081/actuator/info
- **Metrics**: http://localhost:8081/actuator/metrics

## 🔄 Progression Série

### ✅ Vidéo 4 Terminée
- Keycloak configuré avec realm "demo"
- Client "demo-app" créé
- Utilisateurs test avec rôles

### ✅ Vidéo 5 Actuelle
- Projet Spring Boot créé
- Dépendances OAuth2 intégrées
- Structure professionnelle
- Configuration de base

### ⏳ Vidéo 6 Prochaine
- Configuration Spring Security complète
- Intégration OAuth2 Keycloak
- Endpoints sécurisés
- Validation JWT

## 🛡️ Sécurité

### Configuration Actuelle (Temporaire)
```java
// SecurityConfig.java - Vidéo 5
.anyRequest().permitAll()  // Tous les endpoints accessibles
```

### Configuration Finale (Vidéo 6)
```java
// SecurityConfig.java - Vidéo 6
.anyRequest().authenticated()  // Authentification requise
```

## 🔧 Troubleshooting

### Problèmes Courants

**Erreur Java Version**
```
Error: Java 17+ required
```
**Solution:** Installer JDK 17 ou supérieur

**Port 8081 Occupé**
```
Error: Port 8081 already in use
```
**Solution:** Changer le port dans application.yml ou arrêter l'autre process

**Maven Build Failed**
```
Error: Could not resolve dependencies
```
**Solution:** Vérifier la connexion internet et les settings Maven

## 📚 Ressources

### Documentation
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [Keycloak Documentation](https://www.keycloak.org/documentation)

### Formation YouTube
- **Vidéo 1-4** : Fondamentaux Keycloak
- **Vidéo 5** : Setup Spring Boot (actuelle)
- **Vidéo 6** : Configuration Spring Security
- **Vidéo 7-8** : Tests et intégration

## 👨‍💻 Développement

### Commandes Utiles
```bash
# Compiler sans démarrer
mvn compile

# Tester
mvn test

# Package
mvn package

# Clean install
mvn clean install
```

### Extensions IDE Recommandées
- **IntelliJ IDEA** : Spring Boot plugin
- **VSCode** : Spring Boot Extension Pack

## 🎯 Objectifs Atteints

- [x] Projet Spring Boot 3.x créé
- [x] Dépendances OAuth2 intégrées
- [x] Structure packages professionnelle
- [x] Configuration Keycloak préparée
- [x] Application fonctionnelle sur port 8081
- [x] Endpoints de test opérationnels
- [x] Prêt pour la vidéo 6 (Spring Security)

---

**🎬 Vidéo 5/30 - Module 2 : Intégration Spring Boot**
**📺 Formation : Keycloak + Spring Boot + Angular**