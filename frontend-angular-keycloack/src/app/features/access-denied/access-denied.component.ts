import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

/**
 * Composant affiché lorsqu'un utilisateur tente d'accéder à une ressource
 * pour laquelle il n'a pas les permissions nécessaires
 *
 * Affiche:
 * - Un message d'erreur clair
 * - Les rôles requis
 * - Les rôles actuels de l'utilisateur
 * - Un bouton pour retourner à l'accueil
 */
@Component({
  selector: 'app-access-denied',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="access-denied-container">
      <div class="access-denied-card">
        <!-- Icon -->
        <div class="icon-container">
          <svg xmlns="http://www.w3.org/2000/svg" width="120" height="120" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10"></circle>
            <line x1="4.93" y1="4.93" x2="19.07" y2="19.07"></line>
          </svg>
        </div>

        <!-- Title -->
        <h1>Accès Refusé</h1>

        <!-- Description -->
        <p class="description">
          Vous n'avez pas les permissions nécessaires pour accéder à cette page.
        </p>

        <!-- Required Roles -->
        <div class="info-section" *ngIf="requiredRoles.length > 0">
          <h3>🔑 Rôles requis :</h3>
          <div class="roles-container">
            <span class="role-badge required" *ngFor="let role of requiredRoles">
              {{ role }}
            </span>
          </div>
        </div>

        <!-- User Roles -->
        <div class="info-section" *ngIf="userRoles.length > 0">
          <h3>👤 Vos rôles actuels :</h3>
          <div class="roles-container">
            <span class="role-badge current" *ngFor="let role of userRoles">
              {{ role }}
            </span>
          </div>
        </div>

        <!-- User Info -->
        <div class="info-section" *ngIf="userName">
          <p class="user-info">
            Connecté en tant que : <strong>{{ userName }}</strong>
          </p>
        </div>

        <!-- Attempted URL -->
        <div class="info-section" *ngIf="attemptedUrl">
          <p class="url-info">
            URL tentée : <code>{{ attemptedUrl }}</code>
          </p>
        </div>

        <!-- Actions -->
        <div class="actions">
          <button class="btn btn-primary" (click)="goToHome()">
            🏠 Retour à l'accueil
          </button>
          <button class="btn btn-secondary" (click)="goBack()">
            ← Retour
          </button>
        </div>

        <!-- Help Text -->
        <div class="help-text">
          <p>
            Si vous pensez que c'est une erreur, veuillez contacter votre administrateur.
          </p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .access-denied-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      padding: 20px;
    }

    .access-denied-card {
      background: white;
      border-radius: 20px;
      padding: 40px;
      max-width: 600px;
      width: 100%;
      box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
      text-align: center;
    }

    .icon-container {
      margin-bottom: 30px;
    }

    .icon-container svg {
      color: #ef4444;
      filter: drop-shadow(0 4px 8px rgba(239, 68, 68, 0.3));
    }

    h1 {
      font-size: 2.5em;
      color: #1f2937;
      margin-bottom: 20px;
      font-weight: 900;
    }

    .description {
      font-size: 1.1em;
      color: #6b7280;
      margin-bottom: 30px;
      line-height: 1.6;
    }

    .info-section {
      margin: 25px 0;
      padding: 20px;
      background: #f9fafb;
      border-radius: 12px;
      border: 1px solid #e5e7eb;
    }

    .info-section h3 {
      font-size: 1em;
      color: #374151;
      margin-bottom: 15px;
      font-weight: 700;
    }

    .roles-container {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;
      justify-content: center;
    }

    .role-badge {
      padding: 8px 16px;
      border-radius: 20px;
      font-size: 0.9em;
      font-weight: 600;
      display: inline-block;
    }

    .role-badge.required {
      background: #fee2e2;
      color: #991b1b;
      border: 2px solid #fca5a5;
    }

    .role-badge.current {
      background: #dbeafe;
      color: #1e40af;
      border: 2px solid #93c5fd;
    }

    .user-info, .url-info {
      font-size: 0.95em;
      color: #4b5563;
      margin: 0;
    }

    .user-info strong {
      color: #1f2937;
    }

    code {
      background: #e5e7eb;
      padding: 4px 8px;
      border-radius: 4px;
      font-family: 'Courier New', monospace;
      font-size: 0.9em;
      color: #1f2937;
    }

    .actions {
      display: flex;
      gap: 15px;
      justify-content: center;
      margin-top: 30px;
    }

    .btn {
      padding: 12px 30px;
      border: none;
      border-radius: 10px;
      font-size: 1em;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    .btn-primary {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
    }

    .btn-primary:hover {
      transform: translateY(-2px);
      box-shadow: 0 10px 20px rgba(102, 126, 234, 0.4);
    }

    .btn-secondary {
      background: #f3f4f6;
      color: #374151;
      border: 2px solid #e5e7eb;
    }

    .btn-secondary:hover {
      background: #e5e7eb;
      transform: translateY(-2px);
    }

    .help-text {
      margin-top: 30px;
      padding-top: 20px;
      border-top: 1px solid #e5e7eb;
    }

    .help-text p {
      font-size: 0.9em;
      color: #9ca3af;
      margin: 0;
    }

    @media (max-width: 640px) {
      .access-denied-card {
        padding: 30px 20px;
      }

      h1 {
        font-size: 2em;
      }

      .actions {
        flex-direction: column;
      }

      .btn {
        width: 100%;
      }
    }
  `]
})
export class AccessDeniedComponent implements OnInit {
  requiredRoles: string[] = [];
  userRoles: string[] = [];
  userName: string = '';
  attemptedUrl: string = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private keycloak: KeycloakService
  ) {}

  async ngOnInit(): Promise<void> {
    // Récupérer les paramètres de la route
    this.route.queryParams.subscribe(params => {
      if (params['requiredRoles']) {
        this.requiredRoles = params['requiredRoles'].split(',');
      }
      if (params['url']) {
        this.attemptedUrl = params['url'];
      }
    });

    // Récupérer les informations de l'utilisateur depuis Keycloak
    try {
      const isLoggedIn = await this.keycloak.isLoggedIn();

      if (isLoggedIn) {
        // Récupérer les rôles de l'utilisateur
        this.userRoles = this.keycloak.getUserRoles();

        // Récupérer le nom d'utilisateur
        const userProfile = await this.keycloak.loadUserProfile();
        this.userName = userProfile.username || userProfile.email || 'Utilisateur';
      }
    } catch (error) {
      console.error('Erreur lors du chargement des informations utilisateur:', error);
    }
  }

  /**
   * Retour à la page d'accueil
   */
  goToHome(): void {
    this.router.navigate(['/']);
  }

  /**
   * Retour à la page précédente
   */
  goBack(): void {
    window.history.back();
  }
}
