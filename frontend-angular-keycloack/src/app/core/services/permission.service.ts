import { Injectable, inject } from '@angular/core';
import { AppKeycloakService } from './keycloak.service';

/**
 * Permission Service
 * Service centralisé pour la gestion des permissions et rôles
 * Inclut un cache pour optimiser les performances
 */
@Injectable({
  providedIn: 'root'
})
export class PermissionService {
  private keycloak = inject(AppKeycloakService);

  // Cache des rôles pour éviter les appels répétés
  private rolesCache: string[] | null = null;
  private cacheTimestamp: number = 0;
  private readonly CACHE_DURATION = 30000; // 30 secondes

  /**
   * Récupère les rôles de l'utilisateur (avec cache)
   * @returns Liste des rôles de l'utilisateur
   */
  getUserRoles(): string[] {
    const now = Date.now();

    // Vérifier si le cache est valide
    if (this.rolesCache && (now - this.cacheTimestamp) < this.CACHE_DURATION) {
      return this.rolesCache;
    }

    // Rafraîchir le cache
    this.rolesCache = this.keycloak.getUserRoles();
    this.cacheTimestamp = now;

    return this.rolesCache;
  }

  /**
   * Vérifie si l'utilisateur possède un rôle spécifique
   * @param role Rôle à vérifier
   * @returns true si l'utilisateur a le rôle
   */
  hasRole(role: string): boolean {
    const userRoles = this.getUserRoles();
    return userRoles.includes(role);
  }

  /**
   * Vérifie si l'utilisateur possède AU MOINS UN des rôles fournis
   * @param roles Liste des rôles à vérifier
   * @returns true si l'utilisateur a au moins un des rôles
   */
  hasAnyRole(roles: string[]): boolean {
    const userRoles = this.getUserRoles();
    return roles.some(role => userRoles.includes(role));
  }

  /**
   * Vérifie si l'utilisateur possède TOUS les rôles fournis
   * @param roles Liste des rôles à vérifier
   * @returns true si l'utilisateur a tous les rôles
   */
  hasAllRoles(roles: string[]): boolean {
    const userRoles = this.getUserRoles();
    return roles.every(role => userRoles.includes(role));
  }

  /**
   * Vérifie si l'utilisateur est authentifié
   * @returns true si l'utilisateur est connecté
   */
  isAuthenticated(): boolean {
    return this.keycloak.isAuthenticated();
  }

  /**
   * Invalide le cache des rôles
   * À appeler lors du login/logout
   */
  clearCache(): void {
    this.rolesCache = null;
    this.cacheTimestamp = 0;
  }
}
