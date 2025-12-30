import { Injectable, inject } from '@angular/core';
import { AppKeycloakService } from './keycloak.service';
import { User } from '../../shared/models/user.model';

/**
 * User Service
 * Gère les informations de l'utilisateur connecté
 */
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private keycloak = inject(AppKeycloakService);

  /**
   * Récupère l'utilisateur actuellement connecté
   * @returns User object ou null si non connecté
   */
  async getCurrentUser(): Promise<User | null> {
    const isLoggedIn = this.keycloak.isAuthenticated();

    if (!isLoggedIn) {
      return null;
    }

    try {
      // Use getUserInfo which returns an Observable
      return await new Promise<User | null>((resolve) => {
        this.keycloak.getUserInfo().subscribe({
          next: (user) => resolve(user),
          error: (error) => {
            console.error('Erreur lors de la récupération du profil utilisateur:', error);
            resolve(null);
          }
        });
      });
    } catch (error) {
      console.error('Erreur lors de la récupération du profil utilisateur:', error);
      return null;
    }
  }

  /**
   * Récupère les rôles de l'utilisateur
   * @returns Liste des rôles
   */
  getUserRoles(): string[] {
    return this.keycloak.getUserRoles();
  }

  /**
   * Vérifie si l'utilisateur possède un rôle spécifique
   * @param role Rôle à vérifier
   * @returns true si l'utilisateur possède le rôle
   */
  hasRole(role: string): boolean {
    return this.getUserRoles().includes(role);
  }

  /**
   * Vérifie si l'utilisateur possède au moins un des rôles fournis
   * @param roles Liste de rôles à vérifier
   * @returns true si l'utilisateur possède au moins un rôle
   */
  hasAnyRole(roles: string[]): boolean {
    const userRoles = this.getUserRoles();
    return roles.some(role => userRoles.includes(role));
  }

  /**
   * Vérifie si l'utilisateur possède tous les rôles fournis
   * @param roles Liste de rôles à vérifier
   * @returns true si l'utilisateur possède tous les rôles
   */
  hasAllRoles(roles: string[]): boolean {
    const userRoles = this.getUserRoles();
    return roles.every(role => userRoles.includes(role));
  }
}
