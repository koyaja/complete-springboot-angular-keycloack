import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

/**
 * Guard d'authentification pour protéger les routes privées
 * Vérifie si l'utilisateur est connecté à Keycloak
 * Redirige vers la page de login si nécessaire
 *
 * @example
 * // Dans app.routes.ts
 * {
 *   path: 'dashboard',
 *   component: DashboardComponent,
 *   canActivate: [authGuard]
 * }
 */
export const authGuard: CanActivateFn = async (route, state) => {
  const keycloak = inject(KeycloakService);
  const router = inject(Router);

  try {
    // Vérifier si l'utilisateur est connecté
    const isLoggedIn = await keycloak.isLoggedIn();

    if (!isLoggedIn) {
      console.log('🔒 AuthGuard: Utilisateur non connecté, redirection vers Keycloak');

      // Rediriger vers la page de connexion Keycloak
      // Après la connexion, l'utilisateur sera redirigé vers l'URL demandée
      await keycloak.login({
        redirectUri: window.location.origin + state.url
      });

      return false;
    }

    console.log('✅ AuthGuard: Utilisateur authentifié, accès autorisé');
    return true;

  } catch (error) {
    console.error('❌ AuthGuard: Erreur lors de la vérification', error);

    // En cas d'erreur, rediriger vers la page d'accueil
    router.navigate(['/']);
    return false;
  }
};
