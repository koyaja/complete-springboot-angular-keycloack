import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppKeycloakService } from '../../core/services/keycloak.service';
import { UserService } from '../../core/services/user.service';
import { User } from '../../shared/models/user.model';

/**
 * Profile Component
 * Affiche les informations du profil utilisateur depuis Keycloak
 */
@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  private keycloakService = inject(AppKeycloakService);
  private userService = inject(UserService);

  // Signals pour la réactivité
  user = signal<User | null>(null);
  jwtClaims = signal<any>(null);
  loading = signal<boolean>(true);
  error = signal<string | null>(null);

  // Informations du token
  tokenExpiration = signal<Date | null>(null);
  tokenExpiresIn = signal<string>('');

  async ngOnInit() {
    await this.loadUserProfile();
    this.loadJwtClaims();
    this.calculateTokenExpiration();
  }

  /**
   * Charge le profil utilisateur depuis Keycloak
   */
  private async loadUserProfile() {
    try {
      this.loading.set(true);

      // Utiliser le UserService pour récupérer les infos utilisateur
      this.keycloakService.getUserInfo().subscribe({
        next: (user) => {
          this.user.set(user);
          this.loading.set(false);
        },
        error: (err) => {
          console.error('Erreur chargement profil:', err);
          this.error.set('Impossible de charger le profil utilisateur');
          this.loading.set(false);
        }
      });
    } catch (err) {
      console.error('Erreur:', err);
      this.error.set('Une erreur est survenue');
      this.loading.set(false);
    }
  }

  /**
   * Récupère les claims du token JWT
   */
  private loadJwtClaims() {
    try {
      const tokenParsed = this.keycloakService.getTokenParsed();
      if (tokenParsed) {
        this.jwtClaims.set(tokenParsed);
      }
    } catch (err) {
      console.error('Erreur récupération claims JWT:', err);
    }
  }

  /**
   * Calcule l'expiration du token
   */
  private calculateTokenExpiration() {
    try {
      const tokenParsed = this.keycloakService.getTokenParsed();
      if (tokenParsed && tokenParsed.exp) {
        const expTimestamp = tokenParsed.exp * 1000;
        this.tokenExpiration.set(new Date(expTimestamp));

        // Calculer le temps restant
        this.updateExpiresIn();
        setInterval(() => this.updateExpiresIn(), 1000);
      }
    } catch (err) {
      console.error('Erreur calcul expiration:', err);
    }
  }

  /**
   * Met à jour l'affichage du temps restant
   */
  private updateExpiresIn() {
    const expiration = this.tokenExpiration();
    if (!expiration) return;

    const now = new Date();
    const diffMs = expiration.getTime() - now.getTime();

    if (diffMs <= 0) {
      this.tokenExpiresIn.set('Expiré');
      return;
    }

    const minutes = Math.floor(diffMs / 60000);
    const seconds = Math.floor((diffMs % 60000) / 1000);
    this.tokenExpiresIn.set(`${minutes}m ${seconds}s`);
  }

  /**
   * Rafraîchit le token
   */
  async refreshToken() {
    try {
      await this.keycloakService.refreshToken();
      this.loadJwtClaims();
      this.calculateTokenExpiration();
    } catch (err) {
      console.error('Erreur refresh token:', err);
      this.error.set('Impossible de rafraîchir le token');
    }
  }

  /**
   * Retourne les initiales de l'utilisateur
   */
  getInitials(): string {
    const u = this.user();
    if (!u) return '?';

    if (u.firstName && u.lastName) {
      return `${u.firstName.charAt(0)}${u.lastName.charAt(0)}`.toUpperCase();
    }

    return u.username?.charAt(0).toUpperCase() || '?';
  }

  /**
   * Formate les claims JWT pour l'affichage
   */
  getFormattedClaims(): string {
    const claims = this.jwtClaims();
    if (!claims) return '{}';
    return JSON.stringify(claims, null, 2);
  }
}
