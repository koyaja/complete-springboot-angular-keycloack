import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Observable } from 'rxjs';
import { AppKeycloakService } from '../../../core/services/keycloak.service';
import { UserProfile } from '../../models/user.model';
import { ROLES } from '../../../core/constants/roles.constants';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <header class="header">
      <div class="container">
        <div class="header-content">
          <!-- Logo -->
          <div class="logo">
            <h1>Angular + Keycloak</h1>
          </div>
          
          <!-- Navigation -->
          <nav class="nav">
            <a routerLink="/home" routerLinkActive="active" class="nav-link">Accueil</a>
            @if (isAuthenticated) {
              <a routerLink="/dashboard" routerLinkActive="active" class="nav-link">Dashboard</a>
            }
            @if (isAdmin) {
              <a routerLink="/admin" routerLinkActive="active" class="nav-link admin-link">Admin</a>
            }
          </nav>
          
          <!-- User section -->
          <div class="user-section">
            @if (isAuthenticated) {
              <div class="user-info" (click)="toggleDropdown()">
                @if (userProfile$ | async; as profile) {
                  <div class="user-avatar">{{ profile.initials }}</div>
                  <span class="username">{{ profile.fullName }}</span>
                }
                <svg class="dropdown-icon" width="12" height="12" viewBox="0 0 12 12">
                  <path d="M6 8L2 4h8L6 8z" fill="currentColor"/>
                </svg>
              </div>
              
              @if (showDropdown) {
                <div class="dropdown">
                  <button class="dropdown-item" (click)="logout()">
                    Se déconnecter
                  </button>
                </div>
              }
            } @else {
              <button class="login-btn" (click)="login()">
                Se connecter
              </button>
            }
          </div>
        </div>
      </div>
    </header>
  `,
  styles: [`
    .header {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 1rem 0;
      box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }
    
    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 1rem;
    }
    
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    
    .logo h1 {
      margin: 0;
      font-size: 1.5rem;
      font-weight: 600;
    }
    
    .nav {
      display: flex;
      gap: 2rem;
    }
    
    .nav-link {
      color: white;
      text-decoration: none;
      font-weight: 500;
      transition: opacity 0.2s;
      padding: 0.5rem 1rem;
      border-radius: 0.25rem;
    }

    .nav-link:hover {
      opacity: 0.8;
      background-color: rgba(255,255,255,0.1);
    }

    .nav-link.active {
      background-color: rgba(255,255,255,0.2);
      font-weight: 600;
    }

    .nav-link.admin-link {
      background: rgba(220, 38, 38, 0.2);
      border: 1px solid rgba(220, 38, 38, 0.3);
    }

    .nav-link.admin-link:hover {
      background: rgba(220, 38, 38, 0.3);
    }

    .nav-link.admin-link.active {
      background: rgba(220, 38, 38, 0.4);
    }
    
    .user-section {
      position: relative;
    }
    
    .user-info {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      cursor: pointer;
      padding: 0.5rem;
      border-radius: 0.5rem;
      transition: background-color 0.2s;
    }
    
    .user-info:hover {
      background-color: rgba(255,255,255,0.1);
    }
    
    .user-avatar {
      width: 2rem;
      height: 2rem;
      border-radius: 50%;
      background: rgba(255,255,255,0.2);
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 600;
      font-size: 0.8rem;
    }
    
    .username {
      font-weight: 500;
    }
    
    .dropdown-icon {
      transition: transform 0.2s;
    }
    
    .dropdown {
      position: absolute;
      top: 100%;
      right: 0;
      background: white;
      border-radius: 0.5rem;
      box-shadow: 0 4px 6px rgba(0,0,0,0.1);
      min-width: 150px;
      z-index: 1000;
      margin-top: 0.5rem;
    }
    
    .dropdown-item {
      width: 100%;
      padding: 0.75rem 1rem;
      border: none;
      background: none;
      text-align: left;
      cursor: pointer;
      color: #374151;
      font-weight: 500;
      transition: background-color 0.2s;
    }
    
    .dropdown-item:hover {
      background-color: #f3f4f6;
    }
    
    .login-btn {
      background: rgba(255,255,255,0.2);
      color: white;
      border: 1px solid rgba(255,255,255,0.3);
      padding: 0.5rem 1rem;
      border-radius: 0.5rem;
      cursor: pointer;
      font-weight: 500;
      transition: all 0.2s;
    }
    
    .login-btn:hover {
      background: rgba(255,255,255,0.3);
      transform: translateY(-1px);
    }
    
    @media (max-width: 768px) {
      .header-content {
        flex-direction: column;
        gap: 1rem;
      }
      
      .nav {
        gap: 1rem;
      }
    }
  `]
})
export class HeaderComponent implements OnInit {
  isAuthenticated = false;
  isAdmin = false;
  userProfile$: Observable<UserProfile | null>;
  showDropdown = false;

  constructor(private keycloakService: AppKeycloakService) {
    this.userProfile$ = this.keycloakService.getUserProfile();
  }

  ngOnInit(): void {
    this.isAuthenticated = this.keycloakService.isAuthenticated();
    this.isAdmin = this.keycloakService.hasRole(ROLES.ADMIN);
  }

  login(): void {
    this.keycloakService.login();
  }

  logout(): void {
    this.keycloakService.logout();
  }

  toggleDropdown(): void {
    this.showDropdown = !this.showDropdown;
  }
}