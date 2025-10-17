import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { AccessDeniedComponent } from './features/access-denied/access-denied.component';

// Import des guards
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

// Import des constantes de rôles
import { ROLES } from './core/constants/roles.constants';

export const routes: Routes = [
  // Route par défaut - Redirection vers home
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },

  // ========================================
  // ROUTES PUBLIQUES (Pas de guard)
  // ========================================

  {
    path: 'home',
    component: HomeComponent,
    data: { title: 'Accueil' }
  },

  // ========================================
  // ROUTES PROTÉGÉES - AUTHENTIFICATION REQUISE
  // Utilisent authGuard pour vérifier que l'utilisateur est connecté
  // ========================================

  // Exemple: Page Dashboard (authentification requise)
  // {
  //   path: 'dashboard',
  //   component: DashboardComponent,
  //   canActivate: [authGuard],
  //   data: { title: 'Dashboard' }
  // },

  // Exemple: Page Profil (authentification requise)
  // {
  //   path: 'profile',
  //   component: ProfileComponent,
  //   canActivate: [authGuard],
  //   data: { title: 'Mon Profil' }
  // },

  // ========================================
  // ROUTES PROTÉGÉES PAR RÔLES
  // Utilisent roleGuard pour vérifier les permissions
  // ========================================

  // Exemple: Page Admin (rôles ADMIN ou MANAGER requis)
  // {
  //   path: 'admin',
  //   component: AdminComponent,
  //   canActivate: [roleGuard([ROLES.ADMIN, ROLES.MANAGER])],
  //   data: { title: 'Administration' }
  // },

  // Exemple: Page Settings (rôle ADMIN uniquement)
  // {
  //   path: 'settings',
  //   component: SettingsComponent,
  //   canActivate: [roleGuard([ROLES.ADMIN])],
  //   data: { title: 'Paramètres' }
  // },

  // Exemple: Page avec plusieurs guards (authentification + rôle)
  // {
  //   path: 'reports',
  //   component: ReportsComponent,
  //   canActivate: [authGuard, roleGuard([ROLES.MANAGER, ROLES.ADMIN])],
  //   data: { title: 'Rapports' }
  // },

  // ========================================
  // LAZY LOADING AVEC GUARDS
  // Exemple pour charger des modules à la demande
  // ========================================

  // {
  //   path: 'admin',
  //   loadChildren: () => import('./features/admin/admin.routes').then(m => m.ADMIN_ROUTES),
  //   canActivate: [roleGuard([ROLES.ADMIN])],
  //   data: { title: 'Administration' }
  // },

  // ========================================
  // ROUTES D'ERREUR
  // ========================================

  // Page d'accès refusé
  {
    path: 'access-denied',
    component: AccessDeniedComponent,
    data: { title: 'Accès Refusé' }
  },

  // Route 404 - Redirection vers home
  {
    path: '**',
    redirectTo: '/home'
  }
];

/**
 * Configuration des routes de l'application
 *
 * Types de protection:
 * 1. authGuard - Vérifie que l'utilisateur est authentifié
 * 2. roleGuard([roles]) - Vérifie que l'utilisateur a au moins un des rôles spécifiés
 *
 * Exemple d'utilisation:
 *
 * // Route publique
 * { path: 'about', component: AboutComponent }
 *
 * // Route privée (authentification requise)
 * { path: 'profile', component: ProfileComponent, canActivate: [authGuard] }
 *
 * // Route avec rôle spécifique (au moins un des rôles)
 * { path: 'admin', component: AdminComponent, canActivate: [roleGuard([ROLES.ADMIN, ROLES.MANAGER])] }
 *
 * // Route avec rôle unique
 * { path: 'settings', component: SettingsComponent, canActivate: [roleGuard([ROLES.ADMIN])] }
 *
 * // Route avec plusieurs guards
 * { path: 'reports', component: ReportsComponent, canActivate: [authGuard, roleGuard([ROLES.MANAGER])] }
 */
