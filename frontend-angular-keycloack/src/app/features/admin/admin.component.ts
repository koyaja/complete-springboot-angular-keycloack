import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

interface User {
  id: number;
  username: string;
  email: string;
  roles: string[];
  status: 'active' | 'inactive';
}

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  userProfile: KeycloakProfile | null = null;
  userRoles: string[] = [];
  isLoading = true;

  // Données de démonstration pour la gestion des utilisateurs
  users: User[] = [
    { id: 1, username: 'john.doe', email: 'john@example.com', roles: ['USER'], status: 'active' },
    { id: 2, username: 'jane.smith', email: 'jane@example.com', roles: ['USER', 'MANAGER'], status: 'active' },
    { id: 3, username: 'admin.user', email: 'admin@example.com', roles: ['ADMIN', 'USER'], status: 'active' },
    { id: 4, username: 'bob.wilson', email: 'bob@example.com', roles: ['USER'], status: 'inactive' }
  ];

  // Statistiques pour le tableau de bord admin
  stats = {
    totalUsers: 124,
    activeUsers: 98,
    totalRoles: 5,
    pendingRequests: 7
  };

  constructor(private keycloakService: KeycloakService) {}

  async ngOnInit(): Promise<void> {
    try {
      // Récupérer le profil administrateur
      this.userProfile = await this.keycloakService.loadUserProfile();

      // Récupérer les rôles de l'admin
      this.userRoles = this.keycloakService.getUserRoles();

      console.log('Admin Profile:', this.userProfile);
      console.log('Admin Roles:', this.userRoles);
    } catch (error) {
      console.error('Erreur lors du chargement du profil admin:', error);
    } finally {
      this.isLoading = false;
    }
  }

  // Méthodes de gestion (pour démonstration)
  editUser(user: User): void {
    console.log('Éditer utilisateur:', user);
    alert(`Édition de l'utilisateur: ${user.username}`);
  }

  deleteUser(user: User): void {
    console.log('Supprimer utilisateur:', user);
    if (confirm(`Êtes-vous sûr de vouloir supprimer l'utilisateur ${user.username} ?`)) {
      this.users = this.users.filter(u => u.id !== user.id);
      alert('Utilisateur supprimé (démo)');
    }
  }

  toggleUserStatus(user: User): void {
    user.status = user.status === 'active' ? 'inactive' : 'active';
    console.log('Statut modifié:', user);
  }

  exportData(): void {
    alert('Export des données en cours... (fonctionnalité de démonstration)');
  }
}
