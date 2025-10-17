/**
 * Constantes pour les rôles Keycloak de l'application
 * Ces rôles correspondent aux rôles configurés dans Keycloak
 *
 * Utilisation:
 * - Dans les guards: roleGuard([ROLES.ADMIN])
 * - Dans les composants: keycloak.getUserRoles().includes(ROLES.ADMIN)
 * - Dans les templates: *ngIf="hasRole(ROLES.ADMIN)"
 */
export const ROLES = {
  /**
   * Rôle administrateur - Accès complet à l'application
   * Peut gérer les utilisateurs, les configurations, etc.
   */
  ADMIN: 'ADMIN',

  /**
   * Rôle manager - Accès aux fonctionnalités de gestion
   * Peut gérer son équipe et consulter les rapports
   */
  MANAGER: 'MANAGER',

  /**
   * Rôle utilisateur standard - Accès aux fonctionnalités de base
   * Peut consulter et modifier ses propres données
   */
  USER: 'USER',

  /**
   * Rôle pour les clients de l'application demo
   * Spécifique au realm "demo" et au client "angular-app"
   */
  CLIENT_DEMO_APP_USER: 'CLIENT_DEMO_APP_USER',

  /**
   * Rôle manager spécifique au client demo
   */
  CLIENT_DEMO_APP_MANAGER: 'CLIENT_DEMO_APP_MANAGER',

  /**
   * Rôle admin spécifique au client demo
   */
  CLIENT_DEMO_APP_ADMIN: 'CLIENT_DEMO_APP_ADMIN',

} as const;

/**
 * Type TypeScript pour les rôles
 * Assure la sécurité des types lors de l'utilisation des rôles
 */
export type Role = typeof ROLES[keyof typeof ROLES];

/**
 * Groupes de rôles pour faciliter les vérifications
 * Utilisez ces groupes pour vérifier plusieurs rôles à la fois
 */
export const ROLE_GROUPS = {
  /**
   * Tous les rôles d'administration (Admin + Manager)
   */
  ADMINS: [ROLES.ADMIN, ROLES.MANAGER, ROLES.CLIENT_DEMO_APP_ADMIN, ROLES.CLIENT_DEMO_APP_MANAGER],

  /**
   * Tous les utilisateurs authentifiés
   */
  ALL_USERS: [ROLES.USER, ROLES.MANAGER, ROLES.ADMIN],

  /**
   * Rôles spécifiques au client demo
   */
  DEMO_CLIENT: [ROLES.CLIENT_DEMO_APP_USER, ROLES.CLIENT_DEMO_APP_MANAGER, ROLES.CLIENT_DEMO_APP_ADMIN],
} as const;

/**
 * Helper function pour vérifier si un utilisateur a au moins un rôle admin
 * @param userRoles - Liste des rôles de l'utilisateur
 * @returns true si l'utilisateur a un rôle admin
 *
 * @example
 * const isAdmin = hasAdminRole(keycloak.getUserRoles());
 */
export function hasAdminRole(userRoles: string[]): boolean {
  return ROLE_GROUPS.ADMINS.some(role => userRoles.includes(role));
}

/**
 * Helper function pour vérifier si un utilisateur a un rôle spécifique
 * @param userRoles - Liste des rôles de l'utilisateur
 * @param role - Rôle à vérifier
 * @returns true si l'utilisateur possède le rôle
 *
 * @example
 * const canManage = hasRole(keycloak.getUserRoles(), ROLES.MANAGER);
 */
export function hasRole(userRoles: string[], role: Role): boolean {
  return userRoles.includes(role);
}

/**
 * Helper function pour vérifier si un utilisateur a au moins un des rôles spécifiés
 * @param userRoles - Liste des rôles de l'utilisateur
 * @param requiredRoles - Rôles requis (au moins un)
 * @returns true si l'utilisateur possède au moins un des rôles
 *
 * @example
 * const canAccess = hasAnyRole(keycloak.getUserRoles(), [ROLES.ADMIN, ROLES.MANAGER]);
 */
export function hasAnyRole(userRoles: string[], requiredRoles: Role[]): boolean {
  return requiredRoles.some(role => userRoles.includes(role));
}

/**
 * Helper function pour vérifier si un utilisateur a tous les rôles spécifiés
 * @param userRoles - Liste des rôles de l'utilisateur
 * @param requiredRoles - Rôles requis (tous obligatoires)
 * @returns true si l'utilisateur possède tous les rôles
 *
 * @example
 * const hasFullAccess = hasAllRoles(keycloak.getUserRoles(), [ROLES.ADMIN, ROLES.MANAGER]);
 */
export function hasAllRoles(userRoles: string[], requiredRoles: Role[]): boolean {
  return requiredRoles.every(role => userRoles.includes(role));
}
