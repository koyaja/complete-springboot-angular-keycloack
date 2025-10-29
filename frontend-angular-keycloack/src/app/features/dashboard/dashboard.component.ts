import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  userProfile: KeycloakProfile | null = null;
  userRoles: string[] = [];
  isLoading = true;

  constructor(private keycloakService: KeycloakService) {}

  async ngOnInit(): Promise<void> {
    try {
      // Récupérer le profil utilisateur
      this.userProfile = await this.keycloakService.loadUserProfile();

      // Récupérer les rôles
      this.userRoles = this.keycloakService.getUserRoles();

    } catch (error) {
      console.error('Erreur lors du chargement du profil:', error);
    } finally {
      this.isLoading = false;
    }
  }

  async logout(): Promise<void> {
    await this.keycloakService.logout(window.location.origin);
  }
}
