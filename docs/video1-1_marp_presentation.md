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
    gap: 2rem;
  }
  .center {
    text-align: center;
  }
  .large {
    font-size: 2.2em;
  }
  .medium {
    font-size: 1.6em;
  }
  .small {
    font-size: 1.1em;
  }
  .highlight {
    background: linear-gradient(45deg, #ffd700, #ffed4e);
    color: #1a1a2e;
    padding: 0.3em 0.6em;
    border-radius: 8px;
    font-weight: bold;
  }
  .problem {
    background: linear-gradient(45deg, #ff4757, #ff6b7d);
    color: white;
    padding: 1.2em;
    border-radius: 12px;
    margin: 1em 0;
    font-size: 0.9em;
  }
  .solution {
    background: linear-gradient(45deg, #2ed573, #7bed9f);
    color: white;
    padding: 1.2em;
    border-radius: 12px;
    margin: 1em 0;
    font-size: 0.9em;
  }
  .tech-card {
    background: rgba(255, 255, 255, 0.08);
    border: 2px solid #ffd700;
    border-radius: 12px;
    padding: 1.2em;
    margin: 0.8em;
    text-align: center;
    font-size: 0.85em;
    height: auto;
    min-height: 200px;
  }
  .flow-step {
    background: rgba(255, 215, 0, 0.15);
    border-left: 4px solid #ffd700;
    padding: 0.8em 1.2em;
    margin: 0.6em 0;
    font-size: 0.95em;
  }
  .schema-box {
    background: rgba(255, 255, 255, 0.05);
    border: 2px solid #ffd700;
    border-radius: 15px;
    padding: 1.5em;
    margin: 1em auto;
    text-align: center;
    font-size: 0.9em;
    max-width: 80%;
  }
  h1 {
    background: linear-gradient(45deg, #ffd700, #ffed4e);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    text-align: center;
    font-size: 2.8em;
    margin-bottom: 0.3em;
    line-height: 1.2;
  }
  h2 {
    color: #ffd700;
    border-bottom: 2px solid #ffd700;
    padding-bottom: 0.3em;
    font-size: 1.8em;
    margin-bottom: 1em;
  }
  h3 {
    color: #ffd700;
    font-size: 1.3em;
    margin: 0.8em 0 0.5em 0;
  }
  table {
    font-size: 0.85em;
    margin: 1em auto;
    border-collapse: collapse;
  }
  th, td {
    padding: 0.8em 1em;
    border: 1px solid rgba(255, 255, 255, 0.2);
  }
  th {
    background: rgba(255, 215, 0, 0.2);
    color: #ffd700;
    font-weight: bold;
  }
  .emoji {
    font-size: 1.3em;
  }
  ul li {
    margin: 0.4em 0;
    font-size: 0.95em;
  }
---

<!-- _class: center -->

# 🔐 AUTHENTIFICATION MODERNE

## Keycloak + Spring Boot + Angular

### *"De la théorie à la pratique en 30 vidéos"*

**Série complète • Formation pratique • Code source inclus**

---

<!-- _class: center -->

# 🎯 SCHÉMA GLOBAL DE FORMATION

<div class="schema-box">

### 🏗️ **ARCHITECTURE COMPLÈTE**

```
👤 UTILISATEUR
    ⬇️ (Authentification)
🏛️ KEYCLOAK (Identity Provider)
    ⬇️ (JWT Tokens)
🅰️ ANGULAR (Frontend)  ↔️  🍃 SPRING BOOT (Backend)
    ⬇️ (Données sécurisées)
👤 UTILISATEUR CONNECTÉ
```

### 📚 **MODULES DE FORMATION**
🏗️ **Module 1 :** Keycloak Foundations  
🍃 **Module 2 :** Spring Boot Security  
🅰️ **Module 3 :** Angular Authentication  
🚀 **Module 4 :** Production Ready

</div>

---

<!-- _class: center -->

# ❌ AUTHENTIFICATION TRADITIONNELLE

## Le Cauchemar du Développeur

<div class="columns">
<div>

### 🏢 **Application 1**
- 👤 Login unique
- 🔑 Mot de passe
- 🗄️ Base Users

### 🏢 **Application 2**
- 👤 Login unique
- 🔑 Mot de passe
- 🗄️ Base Users

</div>
<div>

<div class="problem">

### 🔥 **PROBLÈMES MAJEURS**
• **Mots de passe multiples**  
→ Oublis fréquents

• **Gestion complexe**  
→ Maintenance difficile

• **Sécurité fragmentée**  
→ Failles multiples

• **UX dégradée**  
→ Utilisateurs frustrés

</div>

</div>
</div>

---

<!-- _class: center -->

# ✅ AUTHENTIFICATION MODERNE (SSO)

## Un Login, Toutes les Apps !

<div class="schema-box">

```
         🏛️ KEYCLOAK
      (Identity Provider)
            ⬇️
     🔑 UN SEUL LOGIN
            ⬇️
🏢 App 1  🏢 App 2  🏢 App 3
```

</div>

<div class="solution">

### 🚀 **AVANTAGES IMMÉDIATS**
• **Single Sign-On (SSO)** → Une seule authentification  
• **Sécurité centralisée** → Contrôle unifié  
• **Standards OAuth 2.0/OIDC** → Technologie éprouvée  
• **UX fluide** → Expérience transparente

</div>

---

# 🔄 STANDARDS DE L'INDUSTRIE

## OAuth 2.0 + OpenID Connect

<div class="columns">
<div>

### 🛡️ **OAuth 2.0**
- 🎯 Framework d'autorisation
- 🔐 Accès sécurisé aux ressources
- 🎫 Tokens d'accès temporaires
- 🌐 Standard industriel

</div>
<div>

### 🆔 **OpenID Connect**
- 📊 Couche identité sur OAuth 2.0
- 👤 Informations utilisateur sécurisées
- 🔍 Authentification standardisée
- 🏆 Utilisé par Google, Microsoft

</div>
</div>

### 💡 **Ces standards sont la base de l'authentification moderne !**

---

<!-- _class: center -->

# 🏗️ FLUX D'AUTHENTIFICATION SSO

## De la Demande à l'Accès Sécurisé

<div class="flow-step">1. 👤 <strong>Utilisateur</strong> → 🚪 Demande d'accès</div>

<div class="flow-step">2. 🌐 <strong>Frontend Angular</strong> → 🔄 Redirection auth</div>

<div class="flow-step">3. 🏛️ <strong>Keycloak Server</strong> → 🔑 Authentification</div>

<div class="flow-step">4. 🏛️ <strong>Keycloak Server</strong> → 🎫 JWT Token</div>

<div class="flow-step">5. 🖥️ <strong>Backend Spring Boot</strong> → ✅ Validation</div>

<div class="flow-step">6. 👤 <strong>Utilisateur</strong> → 📊 Accès aux données</div>

### ⚡ **FLUX EN 6 ÉTAPES SÉCURISÉES**

---

# 🏛️ POURQUOI KEYCLOAK ?

## La Solution Enterprise Open Source

| **Critère** | **Keycloak** | **Alternatives** |
|:---|:---:|:---:|
| 💰 **Coût** | ✅ Gratuit | ❌ Payant |
| 🔓 **Open Source** | ✅ Oui | ❌ Propriétaire |
| 📝 **Standards** | ✅ OAuth/OIDC | ⚠️ Partiel |
| 🎛️ **Admin UI** | ✅ Incluse | ❌ Séparée |
| 📈 **Scalabilité** | ✅ Enterprise | ⚠️ Limitée |

### 🏆 **SOUTIEN PROFESSIONNEL**
🔴 **Red Hat** → Support enterprise  
🔵 **IBM** → Intégration cloud  
🌟 **Communauté active** → 15k+ stars GitHub

---

# 🚀 NOTRE STACK TECHNIQUE

## Trio Gagnant pour l'Authentification Moderne

<div class="columns">
<div>

<div class="tech-card">

### 🏛️ **KEYCLOAK**
*Identity Provider*

• Serveur d'identité centralisé  
• Gestion complète des utilisateurs  
• Émission des tokens JWT  
• Configuration via interface web

</div>

</div>
<div>

<div class="tech-card">

### 🍃 **SPRING BOOT**
*Backend Sécurisé*

• API REST avec Spring Security  
• Validation automatique des tokens  
• Autorisation par rôles  
• Endpoints protégés

</div>

</div>
</div>

<div class="tech-card">

### 🅰️ **ANGULAR** - *Frontend Intelligent*
Interface utilisateur moderne • Gestion de l'authentification • Protection des routes • Refresh automatique des tokens

</div>

---

<!-- _class: center -->

# 📚 VOTRE PARCOURS D'APPRENTISSAGE

## 4 Modules • 30 Vidéos • Application Complète

| **Module** | **Contenu** | **Durée** | **Niveau** |
|:---|:---|:---:|:---:|
| 🏗️ **Module 1** | Fondamentaux Keycloak | 30min | 🟢 Débutant |
| 🍃 **Module 2** | Intégration Spring Boot | 40min | 🟡 Intermédiaire |
| 🅰️ **Module 3** | Frontend Angular | 50min | 🟡 Intermédiaire |
| 🚀 **Module 4** | Production & Sécurité | 30min | 🔴 Avancé |

<div class="solution">

### 🎯 **VOTRE RÉSULTAT FINAL**
🏆 Application complète avec authentification SSO professionnelle  
✅ Code prêt pour la production  
✅ Maîtrise des standards OAuth 2.0/OIDC  
✅ Compétences recherchées en entreprise

</div>

---

<!-- _class: center -->

# 🎬 MERCI POUR VOTRE ATTENTION !

## Likez 👍 • Abonnez-vous 🔔 • Partagez 📤

### 📚 **Ressources**
🔗 **Code source :** github.com/votre-repo  
📖 **Documentation :** keycloak.org  
🎥 **Playlist complète :** [lien YouTube]

### 🏷️ **Tags**
#Keycloak #SpringBoot #Angular #OAuth2 #OIDC #SSO

### 👉 **PROCHAINE VIDÉO**
**Présentation détaillée de l'écosystème**

---

<!-- _class: center -->

# 🚀 PRÊT POUR LA SUITE ?

## Vidéo 2 : Présentation de l'écosystème

### *Découvrez comment ces 3 technologies collaborent pour créer une authentification moderne*

**👉 Rendez-vous dans la prochaine vidéo !**