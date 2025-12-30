import { Component, inject, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AppKeycloakService } from '../../../core/services/keycloak.service';
import { UserService } from '../../../core/services/user.service';
import { User } from '../../models/user.model';

/**
 * Header Component
 * Composant de navigation principal avec gestion de l'authentification
 */
@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  private keycloak = inject(AppKeycloakService);
  private userService = inject(UserService);
  private router = inject(Router);

  // Signals Angular 20 pour la réactivité
  isLoggedIn = signal<boolean>(false);
  currentUser = signal<User | null>(null);
  showUserMenu = signal<boolean>(false);

  /**
   * Initialisation du composant
   * Vérifie l'état de connexion et charge les infos utilisateur si connecté
   */
  async ngOnInit() {
    this.isLoggedIn.set(this.keycloak.isAuthenticated());

    if (this.isLoggedIn()) {
      const user = await this.userService.getCurrentUser();
      this.currentUser.set(user);
    }
  }

  /**
   * Déclenche le processus de connexion via Keycloak
   */
  async login() {
    await this.keycloak.login();
  }

  /**
   * Déconnecte l'utilisateur et le redirige vers la page d'accueil
   */
  async logout() {
    await this.keycloak.logout();
  }

  /**
   * Toggle l'affichage du menu utilisateur
   */
  toggleUserMenu() {
    this.showUserMenu.update(value => !value);
  }

  /**
   * Navigue vers la page de profil utilisateur
   */
  navigateToProfile() {
    this.router.navigate(['/profile']);
    this.showUserMenu.set(false);
  }

  /**
   * Navigue vers la page des paramètres
   */
  navigateToSettings() {
    this.router.navigate(['/settings']);
    this.showUserMenu.set(false);
  }

  /**
   * Récupère l'initiale du prénom pour l'avatar
   */
  getUserInitial(): string {
    const user = this.currentUser();
    if (!user) return '?';

    if (user.firstName) {
      return user.firstName.charAt(0).toUpperCase();
    }

    if (user.username) {
      return user.username.charAt(0).toUpperCase();
    }

    return '?';
  }
}
