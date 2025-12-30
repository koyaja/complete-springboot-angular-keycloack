import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError, switchMap, from } from 'rxjs';
import { AppKeycloakService } from '../services/keycloak.service';
import { Router } from '@angular/router';

/**
 * HTTP Interceptor pour ajouter automatiquement le token JWT aux requêtes
 *
 * Fonctionnalités:
 * - Injection automatique du Bearer token dans le header Authorization
 * - Gestion des erreurs 401 (Unauthorized) avec redirection vers login
 * - Gestion des erreurs 403 (Forbidden) avec redirection vers access-denied
 * - Refresh automatique du token avant expiration
 *
 * @param req - La requête HTTP interceptée
 * @param next - Le handler pour passer à l'intercepteur suivant
 * @returns Observable de la réponse HTTP
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService = inject(AppKeycloakService);
  const router = inject(Router);

  // Ne pas ajouter le token pour les requêtes vers des assets ou Keycloak
  const excludedUrls = ['/assets', '/realms', 'keycloak'];
  const shouldExclude = excludedUrls.some(url => req.url.includes(url));

  if (shouldExclude || !keycloakService.isAuthenticated()) {
    return next(req);
  }

  // Récupérer le token JWT de Keycloak (convertir Promise en Observable)
  return from(keycloakService.getToken()).pipe(
    switchMap(token => {
      if (!token) {
        return next(req);
      }

      // Cloner la requête et ajouter le header Authorization avec le Bearer token
      const clonedReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });

      return next(clonedReq);
    }),
    catchError((error: HttpErrorResponse) => {
      console.error('HTTP Error:', error);

      switch (error.status) {
        case 401:
          // Token expiré ou invalide - Rediriger vers login
          console.warn('Token expiré ou invalide (401). Redirection vers login...');
          keycloakService.login();
          break;

        case 403:
          // Accès refusé - L'utilisateur n'a pas les permissions
          console.warn('Accès refusé (403). Redirection vers access-denied...');
          router.navigate(['/access-denied']);
          break;

        case 500:
          // Erreur serveur
          console.error('Erreur serveur (500)');
          break;

        default:
          console.error(`Erreur HTTP ${error.status}: ${error.message}`);
      }

      return throwError(() => error);
    })
  );
};
