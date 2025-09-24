---
marp: true
theme: default
paginate: true
backgroundColor: #1a1a2e
color: #ffffff
style: |
  .columns {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 1rem;
  }
  .columns-3 {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 1rem;
  }
  .center {
    text-align: center;
  }
  .large {
    font-size: 1.5em;
  }
  .medium {
    font-size: 1em;
  }
  .small {
    font-size: 0.8em;
  }
  .highlight {
    background: linear-gradient(45deg, #ffd700, #ffed4e);
    color: #1a1a2e;
    padding: 0.2em 0.5em;
    border-radius: 8px;
    font-weight: bold;
  }
    .tech-card {
    background: rgba(255, 255, 255, 0.1);
    border: 2px solid #ffd700;
    border-radius: 12px;
    padding: 1.2em;
    margin: 0.5em;
    text-align: center;
    min-height: 200px;
  }
  .flow-step {
    background: rgba(255, 215, 0, 0.2);
    border-left: 4px solid #ffd700;
    padding: 0.8em 1.2em;
    margin: 0.5em 0;
    border-radius: 0 8px 8px 0;
  }
  .role-box {
    background: linear-gradient(45deg, #2ed573, #7bed9f);
    color: white;
    padding: 1em;
    border-radius: 12px;
    margin: 0.5em 0;
    text-align: center;
    font-weight: bold;
  }
  .architecture-flow {
    background: rgba(255, 255, 255, 0.05);
    border: 1px solid rgba(255, 215, 0, 0.3);
    border-radius: 8px;
    padding: 1em;
    margin: 0.5em 0;
    text-align: center;
  }
  h1 {
    background: linear-gradient(45deg, #ffd700, #ffed4e);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    text-align: center;
    font-size: 1em;
    margin-bottom: 0.5em;
  }
  h2 {
    color: #ffd700;
    border-bottom: 2px solid #ffd700;
    padding-bottom: 0.3em;
  }
  .emoji {
    font-size: 1.5em;
  }
  .step-number {
    background: #ffd700;
    color: #1a1a2e;
    border-radius: 50%;
    width: 30px;
    height: 30px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
    margin-right: 0.5em;
  }
---

<!-- _class: center -->

# 🏗️ PRÉSENTATION DE L'ÉCOSYSTÈME

## Keycloak • Spring Boot • Angular

### *"Comment 3 technologies créent une authentification moderne"*

**Vidéo 2/30 • Architecture complète • Flux détaillé**

---

<!-- _class: center -->

# 🎯 NOTRE ARCHITECTURE CIBLE

## L'Écosystème Complet en Action

```
👤 UTILISATEUR
      ⬇️
🌐 FRONTEND ANGULAR
      ⬇️ OAuth 2.0
🏛️ KEYCLOAK SERVER
      ⬇️ JWT Token
🍃 BACKEND SPRING BOOT
      ⬇️
📊 DONNÉES MÉTIER
```

### 🚀 **5 COUCHES • 3 TECHNOLOGIES • 1 SOLUTION**

---

# 🏛️ KEYCLOAK : LE GARDIEN CENTRAL

## Identity & Access Management (IAM)

<div class="columns">
<div>

### 🎯 **RÔLE PRINCIPAL**
- 🔐 **Serveur d'authentification central**
- 👥 **Gestion complète des utilisateurs**
- 🎫 **Émission des tokens JWT**
- 🛡️ **Application des politiques de sécurité**

### 📋 **RESPONSABILITÉS**
- ✅ Vérification des identifiants
- ✅ Gestion des sessions
- ✅ Fourniture des tokens d'accès
- ✅ Interface d'administration

</div>
<div>

<div class="tech-card">

### 🏛️ **KEYCLOAK**
*Identity Provider*

**🔧 Ce qu'il fait :**
- Login/Logout centralisé
- Stockage sécurisé des utilisateurs
- Génération de tokens JWT
- Gestion des rôles et permissions

**🎁 Ce qu'il apporte :**
- SSO entre applications
- Standards OAuth 2.0/OIDC
- Interface d'admin intuitive
- Sécurité enterprise

</div>

</div>
</div>

---

# 🍃 SPRING BOOT : L'API SÉCURISÉE

## Backend RESTful avec Spring Security

<div class="columns">
<div>

<div class="tech-card">

### 🍃 **SPRING BOOT**
*Resource Server*

**🔧 Ce qu'il fait :**
- API REST sécurisée
- Validation automatique des JWT
- Autorisation par rôles
- Accès aux données métier

**🎁 Ce qu'il apporte :**
- Sécurité transparente
- Gestion fine des autorisations
- Performance optimisée
- Intégration native OAuth

</div>

</div>
<div>

### 🎯 **RÔLE PRINCIPAL**
- 📡 **Resource Server OAuth 2.0**
- 🛡️ **Validation automatique des tokens**
- 🎭 **Contrôle d'accès par rôles**
- 📊 **Fourniture des données métier**

### 📋 **RESPONSABILITÉS**
- ✅ Endpoints API sécurisés
- ✅ Décodage et validation JWT
- ✅ Autorisation fine
- ✅ Logging de sécurité

</div>
</div>

---

# 🅰️ ANGULAR : L'INTERFACE INTELLIGENTE

## Frontend avec Gestion OAuth Intégrée

<div class="columns">
<div>

### 🎯 **RÔLE PRINCIPAL**
- 🎨 **Interface utilisateur moderne**
- 🔐 **Client OAuth 2.0 PKCE**
- 🛣️ **Protection des routes**
- 🔄 **Gestion automatique des tokens**

### 📋 **RESPONSABILITÉS**
- ✅ Redirection vers Keycloak
- ✅ Réception des tokens
- ✅ Protection des pages
- ✅ UX d'authentification

</div>
<div>

<div class="tech-card">

### 🅰️ **ANGULAR**
*OAuth Client*

**🔧 Ce qu'il fait :**
- SPA avec routing sécurisé
- Redirection OAuth automatique
- Stockage sécurisé des tokens
- Interface responsive

**🎁 Ce qu'il apporte :**
- UX fluide et moderne
- Sécurité côté client
- Refresh automatique
- Guards de protection

</div>

</div>
</div>

---

<!-- _class: center -->

# 🔄 FLUX D'AUTHENTIFICATION DÉTAILLÉ

## De la Demande d'Accès à l'Autorisation

<div class="flow-step">
<span class="step-number">1</span><strong>Demande d'accès</strong> → L'utilisateur clique sur "Se connecter"
</div>

<div class="flow-step">
<span class="step-number">2</span><strong>Redirection OAuth</strong> → Angular redirige vers Keycloak
</div>

<div class="flow-step">
<span class="step-number">3</span><strong>Authentification</strong> → Keycloak affiche le formulaire de login
</div>

<div class="flow-step">
<span class="step-number">4</span><strong>Génération JWT</strong> → Keycloak émet le token d'accès
</div>

<div class="flow-step">
<span class="step-number">5</span><strong>Retour sécurisé</strong> → Angular reçoit le token via callback
</div>

<div class="flow-step">
<span class="step-number">6</span><strong>Appels API</strong> → Chaque requête inclut le JWT Bearer token
</div>

<div class="flow-step">
<span class="step-number">7</span><strong>Validation</strong> → Spring Boot vérifie et décode le token
</div>

<div class="flow-step">
<span class="step-number">8</span><strong>Accès autorisé</strong> → L'utilisateur accède aux données
</div>

---

# 🤝 INTERACTIONS ENTRE TECHNOLOGIES

## Comment Elles Collaborent

<div class="columns">
<div>

### 🔄 **ANGULAR ↔ KEYCLOAK**
- 📤 Redirection OAuth 2.0
- 📥 Réception des tokens
- 🔄 Refresh automatique
- 🚪 Logout centralisé

### 🔄 **ANGULAR ↔ SPRING BOOT**
- 🔑 Envoi du Bearer token
- 📡 Appels API REST
- ⚡ Gestion des erreurs 401/403
- 📊 Réception des données

</div>
<div>

### 🔄 **KEYCLOAK ↔ SPRING BOOT**
- 🏛️ Fournisseur de clés publiques
- ✅ Validation des signatures JWT
- 👤 Informations utilisateur
- 🎭 Mapping des rôles

### 🔗 **COMMUNICATION :**
- **Angular → Keycloak :** OAuth 2.0 PKCE
- **Keycloak → Angular :** JWT Access Token
- **Angular → Spring Boot :** HTTP + Bearer
- **Spring Boot ← Keycloak :** JWKS

</div>
</div>

---

# ⚙️ CONFIGURATION DES RÔLES

## Qui Fait Quoi dans l'Écosystème

<div class="columns">
<div>

<div class="role-box">

### 🏛️ **KEYCLOAK**
*Authorization Server*

**Génère :** Access Token + Refresh Token  
**Contient :** Claims utilisateur + Rôles  
**Valide :** Signatures avec clés privées  
**Expire :** Selon configuration (15min par défaut)

</div>

<div class="role-box">

### 🅰️ **ANGULAR**
*OAuth Client*

**Stocke :** Tokens en mémoire/sessionStorage  
**Ajoute :** Bearer token automatiquement  
**Protège :** Routes selon authentification  
**Rafraîchit :** Tokens avant expiration

</div>

</div>
<div>

<div class="role-box">

### 🍃 **SPRING BOOT**
*Resource Server*

**Vérifie :** Signature JWT avec clés publiques  
**Décode :** Claims et rôles utilisateur  
**Autorise :** Accès selon @PreAuthorize  
**Journalise :** Événements de sécurité

</div>

### 🔐 **SÉCURITÉ MULTICOUCHE**
- **Keycloak :** Authentification forte
- **Angular :** Protection côté client
- **Spring Boot :** Validation serveur
- **JWT :** Transport sécurisé

</div>
</div>

---

# 🚀 AVANTAGES DE CETTE ARCHITECTURE

## Pourquoi Cette Combinaison Est Gagnante

<div class="columns">
<div>

### 🔐 **SÉCURITÉ ENTERPRISE**
- Standards OAuth 2.0/OIDC
- JWT avec signatures
- Validation multicouche
- Audit et monitoring

### 📈 **SCALABILITÉ HAUTE**
- Clustering Keycloak
- Microservices Spring Boot
- SPA moderne Angular
- Cache intelligent

</div>
<div>

### 🛠️ **MAINTENANCE SIMPLE**
- Interface admin Keycloak
- Auto-configuration Spring
- Composants Angular réutilisables
- Documentation complète

### 🌐 **STANDARDS OUVERTS**
- Interopérabilité garantie
- Écosystème riche
- Support communauté
- Évolution assurée

</div>
</div>

### 🏆 **BÉNÉFICES BUSINESS :**
💰 **Coût réduit** • ⚡ **Time-to-market** • 🔒 **Sécurité renforcée** • 👥 **Équipes productives**

---

<!-- _class: center -->

# 🗺️ FEUILLE DE ROUTE DE LA FORMATION

## Votre Parcours Module par Module

<div class="architecture-flow">

### 📚 **MODULE 1 - FONDAMENTAUX** (Vidéos 3-6)
🏛️ Installation et configuration Keycloak  
👤 Gestion des utilisateurs et rôles  
🔑 Concepts OAuth 2.0 appliqués

</div>

<div class="architecture-flow">

### 🍃 **MODULE 2 - SPRING BOOT** (Vidéos 7-14)
⚙️ Configuration Spring Security + OAuth  
🛡️ Endpoints sécurisés et autorisations  
🧪 Tests d'intégration complets

</div>

<div class="architecture-flow">

### 🅰️ **MODULE 3 - ANGULAR** (Vidéos 15-24)
🔐 Client OAuth avec angular-oauth2-oidc  
🛣️ Routing et guards de sécurité  
🎨 Interface utilisateur moderne

</div>

<div class="architecture-flow">

### 🚀 **MODULE 4 - PRODUCTION** (Vidéos 25-30)
🌐 Déploiement et configuration avancée  
🔒 Sécurité et bonnes pratiques  
🐛 Debugging et troubleshooting

</div>

---

<!-- _class: center -->

# 🎯 CE QUE VOUS ALLEZ MAÎTRISER

## Compétences Acquises Après Cette Formation

<div class="columns">
<div>

### 🏛️ **KEYCLOAK EXPERT**
- ✅ Installation et configuration
- ✅ Gestion des realms et clients
- ✅ Utilisateurs, rôles et groupes
- ✅ Politiques de sécurité avancées

### 🍃 **SPRING SECURITY MAÎTRE**
- ✅ Configuration OAuth 2.0 Resource Server
- ✅ Validation et décodage JWT
- ✅ Autorisation par rôles (@PreAuthorize)
- ✅ Tests de sécurité automatisés

</div>
<div>

### 🅰️ **ANGULAR OAUTH NINJA**
- ✅ Intégration angular-oauth2-oidc
- ✅ Guards et routing sécurisé
- ✅ Gestion automatique des tokens
- ✅ UX d'authentification moderne

### 🚀 **ARCHITECTE FULL-STACK**
- ✅ Architecture SSO complète
- ✅ Standards OAuth 2.0/OIDC
- ✅ Déploiement en production
- ✅ Debugging et maintenance

</div>
</div>

### 🏆 **RÉSULTAT FINAL :** Application professionnelle avec authentification enterprise

---

<!-- _class: center -->

# 🎬 MERCI POUR VOTRE ATTENTION !

## Likez 👍 • Abonnez-vous 🔔 • Partagez 📤

### 📚 **Dans la prochaine vidéo :**
🏛️ **Installation de Keycloak avec Docker**  
🚀 **Premier démarrage et interface d'administration**  
⚙️ **Configuration initiale pour notre projet**

### 🏷️ **Tags :** #Keycloak #SpringBoot #Angular #OAuth2 #Architecture

---

<!-- _class: center -->

# 🚀 PRÊT POUR LA SUITE ?

## Vidéo 3 : Installation de Keycloak

### *Découvrez comment installer et configurer Keycloak avec Docker*

**👉 Rendez-vous dans la prochaine vidéo !**