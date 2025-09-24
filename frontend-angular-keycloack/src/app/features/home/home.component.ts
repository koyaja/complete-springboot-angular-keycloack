import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { AppKeycloakService } from '../../core/services/keycloak.service';
import { UserProfile } from '../../shared/models/user.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="home-container">
      <div class="hero-section">
        <h1>Bienvenue dans Angular + Keycloak</h1>
        <p class="subtitle">Démonstration d'intégration sécurisée avec Keycloak</p>
        
        @if (isAuthenticated) {
          <div class="user-welcome">
            @if (userProfile$ | async; as profile) {
              <div class="welcome-card">
                <div class="user-avatar">{{ profile.initials }}</div>
                <div class="user-details">
                  <h2>Bonjour {{ profile.fullName }}!</h2>
                  <p>Email: {{ profile.email }}</p>
                  <p>Nom d'utilisateur: {{ profile.username }}</p>
                  
                  @if (profile.roles.length > 0) {
                    <div class="roles">
                      <h4>Vos rôles:</h4>
                      <div class="role-badges">
                        @for (role of profile.roles; track role) {
                          <span class="role-badge">{{ role }}</span>
                        }
                      </div>
                    </div>
                  }
                </div>
              </div>
            }
          </div>
        } @else {
          <div class="login-prompt">
            <p>Connectez-vous pour accéder à votre profil</p>
            <button class="login-btn" (click)="login()">
              Se connecter avec Keycloak
            </button>
          </div>
        }
      </div>
      
      <div class="features-section">
        <h2>Fonctionnalités démontrées</h2>
        <div class="features-grid">
          <div class="feature-card">
            <h3>🔐 Authentification</h3>
            <p>Connexion sécurisée via Keycloak avec OAuth2/OIDC</p>
          </div>
          <div class="feature-card">
            <h3>🎫 JWT Tokens</h3>
            <p>Gestion automatique des tokens JWT avec refresh automatique</p>
          </div>
          <div class="feature-card">
            <h3>👥 Gestion des rôles</h3>
            <p>Attribution et vérification des rôles utilisateur</p>
          </div>
          <div class="feature-card">
            <h3>🛡️ Routes protégées</h3>
            <p>Protection des routes selon les permissions</p>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .home-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 2rem 1rem;
    }
    
    .hero-section {
      text-align: center;
      margin-bottom: 4rem;
    }
    
    .hero-section h1 {
      font-size: 3rem;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      margin-bottom: 1rem;
    }
    
    .subtitle {
      font-size: 1.2rem;
      color: #6b7280;
      margin-bottom: 2rem;
    }
    
    .user-welcome {
      margin-top: 2rem;
    }
    
    .welcome-card {
      background: white;
      border-radius: 1rem;
      padding: 2rem;
      box-shadow: 0 4px 6px rgba(0,0,0,0.1);
      display: inline-flex;
      align-items: center;
      gap: 1.5rem;
      text-align: left;
    }
    
    .user-avatar {
      width: 4rem;
      height: 4rem;
      border-radius: 50%;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-weight: 600;
      font-size: 1.5rem;
    }
    
    .user-details h2 {
      margin: 0 0 0.5rem 0;
      color: #374151;
    }
    
    .user-details p {
      margin: 0.25rem 0;
      color: #6b7280;
    }
    
    .roles {
      margin-top: 1rem;
    }
    
    .roles h4 {
      margin: 0 0 0.5rem 0;
      color: #374151;
    }
    
    .role-badges {
      display: flex;
      gap: 0.5rem;
      flex-wrap: wrap;
    }
    
    .role-badge {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 0.25rem 0.75rem;
      border-radius: 1rem;
      font-size: 0.875rem;
      font-weight: 500;
    }
    
    .login-prompt {
      margin-top: 2rem;
    }
    
    .login-btn {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      border: none;
      padding: 1rem 2rem;
      border-radius: 0.5rem;
      font-size: 1.1rem;
      font-weight: 600;
      cursor: pointer;
      transition: transform 0.2s, box-shadow 0.2s;
      margin-top: 1rem;
    }
    
    .login-btn:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 15px rgba(102, 126, 234, 0.4);
    }
    
    .features-section {
      text-align: center;
    }
    
    .features-section h2 {
      font-size: 2rem;
      color: #374151;
      margin-bottom: 2rem;
    }
    
    .features-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 2rem;
    }
    
    .feature-card {
      background: white;
      padding: 2rem;
      border-radius: 1rem;
      box-shadow: 0 4px 6px rgba(0,0,0,0.1);
      transition: transform 0.2s;
    }
    
    .feature-card:hover {
      transform: translateY(-4px);
    }
    
    .feature-card h3 {
      margin: 0 0 1rem 0;
      color: #374151;
      font-size: 1.25rem;
    }
    
    .feature-card p {
      margin: 0;
      color: #6b7280;
      line-height: 1.6;
    }
    
    @media (max-width: 768px) {
      .hero-section h1 {
        font-size: 2rem;
      }
      
      .welcome-card {
        flex-direction: column;
        text-align: center;
      }
      
      .features-grid {
        grid-template-columns: 1fr;
      }
    }
  `]
})
export class HomeComponent implements OnInit {
  isAuthenticated = false;
  userProfile$: Observable<UserProfile | null>;

  constructor(private keycloakService: AppKeycloakService) {
    this.userProfile$ = this.keycloakService.getUserProfile();
  }

  ngOnInit(): void {
    this.isAuthenticated = this.keycloakService.isAuthenticated();
  }

  login(): void {
    this.keycloakService.login();
  }
}