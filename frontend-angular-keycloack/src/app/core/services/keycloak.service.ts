import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { Observable, from, map, catchError, of } from 'rxjs';
import { User, UserProfile, KeycloakProfile } from '../../shared/models';

@Injectable({
  providedIn: 'root'
})
export class AppKeycloakService {
  
  constructor(private keycloak: KeycloakService) {}

  /**
   * Initialise Keycloak avec la configuration fournie
   */
  async init(config: any): Promise<boolean> {
    try {
      const authenticated = await this.keycloak.init({
        config,
        initOptions: {
          onLoad: 'check-sso',
          silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html',
          checkLoginIframe: true,
          flow: 'standard'
        },
        enableBearerInterceptor: true,
        bearerPrefix: 'Bearer',
        bearerExcludedUrls: ['/assets', '/clients']
      });
      
      console.log('Keycloak initialization:', authenticated ? 'success' : 'not authenticated');
      return authenticated;
    } catch (error) {
      console.error('Keycloak initialization error:', error);
      return false;
    }
  }

  /**
   * Vérifie si l'utilisateur est authentifié
   */
  isAuthenticated(): boolean {
    return this.keycloak.isLoggedIn();
  }

  /**
   * Déclenche la connexion
   */
  login(): Promise<void> {
    return this.keycloak.login();
  }

  /**
   * Déclenche la déconnexion
   */
  logout(): Promise<void> {
    return this.keycloak.logout(window.location.origin);
  }

  /**
   * Obtient le token d'accès
   */
  getToken(): Promise<string | undefined> {
    return this.keycloak.getToken();
  }

  /**
   * Force le rafraîchissement du token
   */
  refreshToken(): Promise<boolean> {
    return this.keycloak.updateToken(30);
  }

  /**
   * Récupère les informations utilisateur depuis Keycloak
   */
  getUserInfo(): Observable<User | null> {
    if (!this.isAuthenticated()) {
      return of(null);
    }

    return from(this.keycloak.loadUserProfile()).pipe(
      map(profile => this.mapToUser(profile)),
      catchError(error => {
        console.error('Error loading user profile:', error);
        return of(null);
      })
    );
  }

  /**
   * Récupère le profil utilisateur complet
   */
  getUserProfile(): Observable<UserProfile | null> {
    return this.getUserInfo().pipe(
      map(user => user ? this.mapToUserProfile(user) : null)
    );
  }

  /**
   * Vérifie si l'utilisateur a un rôle spécifique
   */
  hasRole(role: string): boolean {
    return this.keycloak.isUserInRole(role);
  }

  /**
   * Vérifie si l'utilisateur a un des rôles fournis
   */
  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.hasRole(role));
  }

  /**
   * Obtient tous les rôles de l'utilisateur
   */
  getUserRoles(): string[] {
    return this.keycloak.getUserRoles();
  }

  /**
   * Mappe le profil Keycloak vers notre modèle User
   */
  private mapToUser(profile: any): User {
    const keycloakProfile = profile as KeycloakProfile;
    
    return {
      id: keycloakProfile.sub || '',
      username: keycloakProfile.preferred_username || '',
      email: keycloakProfile.email || '',
      firstName: keycloakProfile.given_name,
      lastName: keycloakProfile.family_name,
      roles: this.getUserRoles(),
      isEmailVerified: keycloakProfile.email_verified
    };
  }

  /**
   * Mappe vers notre modèle UserProfile étendu
   */
  private mapToUserProfile(user: User): UserProfile {
    const fullName = [user.firstName, user.lastName].filter(Boolean).join(' ') || user.username;
    const initials = this.generateInitials(user.firstName, user.lastName, user.username);
    
    return {
      ...user,
      fullName,
      initials,
      hasAdminRole: this.hasRole('admin'),
      hasUserRole: this.hasRole('user')
    };
  }

  /**
   * Génère les initiales de l'utilisateur
   */
  private generateInitials(firstName?: string, lastName?: string, username?: string): string {
    if (firstName && lastName) {
      return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
    }
    if (firstName) {
      return firstName.charAt(0).toUpperCase();
    }
    if (username) {
      return username.charAt(0).toUpperCase();
    }
    return 'U';
  }
}