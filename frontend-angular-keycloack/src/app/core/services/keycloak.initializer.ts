import { KeycloakService } from 'keycloak-angular';
import { environment } from '../../../environments/environment';

/**
 * Fonction d'initialisation Keycloak
 * Exécutée avant le bootstrap de l'application Angular
 */
export function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {
  return (): Promise<boolean> =>
    keycloak.init({
      config: {
        url: environment.keycloak.url,
        realm: environment.keycloak.realm,
        clientId: environment.keycloak.clientId
      },
      initOptions: {
        // Ne redirige pas automatiquement vers la page de connexion
        // Permet d'avoir des pages publiques
        onLoad: 'check-sso',
        
        // Silent check SSO pour éviter les redirections intempestives
        silentCheckSsoRedirectUri: window.location.origin + '/assets/silent-check-sso.html',
        
        // Vérification périodique du statut de connexion
        checkLoginIframe: true,
        
        // Flow recommandé pour les SPAs (avec PKCE)
        flow: 'standard'
      },
      
      // Configuration de l'interceptor HTTP automatique
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer',
      
      // URLs à exclure de l'ajout automatique du token
      bearerExcludedUrls: [
        '/assets',
        '/clients',
        environment.keycloak.url
      ]
    }).then(authenticated => {
      console.log('Keycloak initialized. User authenticated:', authenticated);
      return authenticated;
    }).catch(error => {
      console.error('Keycloak initialization failed:', error);
      return false;
    });
}