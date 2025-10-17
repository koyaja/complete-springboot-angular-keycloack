import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

/**
 * Guard de vérification des rôles pour protéger les routes sensibles
 * Factory function qui accepte une liste de rôles requis
 * Vérifie si l'utilisateur possède au moins un des rôles spécifiés
 *
 * @param requiredRoles - Liste des rôles autorisés (au moins un requis)
 * @returns CanActivateFn - Fonction guard
 *
 * @example
 * // Dans app.routes.ts
 * {
 *   path: 'admin',
 *   component: AdminComponent,
 *   canActivate: [roleGuard(['ADMIN', 'MANAGER'])]
 * }
 *
 * // Pour un seul rôle
 * {
 *   path: 'settings',
 *   component: SettingsComponent,
 *   canActivate: [roleGuard(['ADMIN'])]
 * }
 */
export const roleGuard = (requiredRoles: string[]): CanActivateFn => {
  return async (route, state) => {
    const keycloak = inject(KeycloakService);
    const router = inject(Router);

    try {
      // Étape 1: Vérifier d'abord si l'utilisateur est connecté
      const isLoggedIn = await keycloak.isLoggedIn();

      if (!isLoggedIn) {
        console.log('🔒 RoleGuard: Utilisateur non connecté, redirection vers Keycloak');

        // Rediriger vers la page de connexion Keycloak
        await keycloak.login({
          redirectUri: window.location.origin + state.url
        });

        return false;
      }

      // Étape 2: Récupérer les rôles de l'utilisateur depuis le token JWT
      const userRoles = keycloak.getUserRoles();
      console.log('👤 RoleGuard: Rôles utilisateur:', userRoles);
      console.log('🔑 RoleGuard: Rôles requis:', requiredRoles);

      // Étape 3: Vérifier si l'utilisateur a au moins un des rôles requis
      const hasRole = requiredRoles.some(role => userRoles.includes(role));

      if (!hasRole) {
        console.log('❌ RoleGuard: Accès refusé - Rôles insuffisants');

        // Rediriger vers la page d'accès refusé
        router.navigate(['/access-denied'], {
          queryParams: {
            requiredRoles: requiredRoles.join(','),
            url: state.url
          }
        });

        return false;
      }

      console.log('✅ RoleGuard: Rôles validés, accès autorisé');
      return true;

    } catch (error) {
      console.error('❌ RoleGuard: Erreur lors de la vérification des rôles', error);

      // En cas d'erreur, rediriger vers la page d'accueil
      router.navigate(['/']);
      return false;
    }
  };
};

/**
 * Helper function pour vérifier si l'utilisateur a TOUS les rôles spécifiés
 * Utilisation alternative pour des cas où tous les rôles sont obligatoires
 *
 * @param requiredRoles - Liste des rôles requis (tous obligatoires)
 * @returns CanActivateFn - Fonction guard
 *
 * @example
 * {
 *   path: 'super-admin',
 *   component: SuperAdminComponent,
 *   canActivate: [roleGuardAll(['ADMIN', 'SUPER_USER'])]
 * }
 */
export const roleGuardAll = (requiredRoles: string[]): CanActivateFn => {
  return async (route, state) => {
    const keycloak = inject(KeycloakService);
    const router = inject(Router);

    try {
      const isLoggedIn = await keycloak.isLoggedIn();

      if (!isLoggedIn) {
        await keycloak.login({
          redirectUri: window.location.origin + state.url
        });
        return false;
      }

      const userRoles = keycloak.getUserRoles();

      // Vérifier que l'utilisateur possède TOUS les rôles requis
      const hasAllRoles = requiredRoles.every(role => userRoles.includes(role));

      if (!hasAllRoles) {
        console.log('❌ RoleGuardAll: Accès refusé - Tous les rôles requis ne sont pas présents');
        router.navigate(['/access-denied']);
        return false;
      }

      return true;

    } catch (error) {
      console.error('❌ RoleGuardAll: Erreur', error);
      router.navigate(['/']);
      return false;
    }
  };
};
