import { Directive, Input, TemplateRef, ViewContainerRef, inject, OnInit, OnDestroy } from '@angular/core';
import { PermissionService } from '../../core/services/permission.service';

/**
 * Directive structurelle *hasRole
 * Affiche ou masque un élément en fonction du rôle de l'utilisateur
 *
 * @example
 * <!-- Afficher si l'utilisateur a le rôle 'admin' -->
 * <button *hasRole="'admin'">Supprimer</button>
 *
 * <!-- Avec un rôle depuis une variable -->
 * <div *hasRole="requiredRole">Contenu protégé</div>
 */
@Directive({
  selector: '[hasRole]',
  standalone: true
})
export class HasRoleDirective implements OnInit, OnDestroy {
  private templateRef = inject(TemplateRef<any>);
  private viewContainer = inject(ViewContainerRef);
  private permissionService = inject(PermissionService);

  private role: string = '';
  private isVisible = false;

  @Input()
  set hasRole(role: string) {
    this.role = role;
    this.updateView();
  }

  ngOnInit(): void {
    this.updateView();
  }

  ngOnDestroy(): void {
    this.viewContainer.clear();
  }

  private updateView(): void {
    const hasPermission = this.permissionService.hasRole(this.role);

    if (hasPermission && !this.isVisible) {
      // Utilisateur a le rôle → Afficher l'élément
      this.viewContainer.createEmbeddedView(this.templateRef);
      this.isVisible = true;
    } else if (!hasPermission && this.isVisible) {
      // Utilisateur n'a pas le rôle → Masquer l'élément
      this.viewContainer.clear();
      this.isVisible = false;
    }
  }
}

/**
 * Directive structurelle *hasAnyRole
 * Affiche si l'utilisateur a AU MOINS UN des rôles spécifiés
 *
 * @example
 * <button *hasAnyRole="['admin', 'manager']">Gérer</button>
 */
@Directive({
  selector: '[hasAnyRole]',
  standalone: true
})
export class HasAnyRoleDirective implements OnInit, OnDestroy {
  private templateRef = inject(TemplateRef<any>);
  private viewContainer = inject(ViewContainerRef);
  private permissionService = inject(PermissionService);

  private roles: string[] = [];
  private isVisible = false;

  @Input()
  set hasAnyRole(roles: string[]) {
    this.roles = roles;
    this.updateView();
  }

  ngOnInit(): void {
    this.updateView();
  }

  ngOnDestroy(): void {
    this.viewContainer.clear();
  }

  private updateView(): void {
    const hasPermission = this.permissionService.hasAnyRole(this.roles);

    if (hasPermission && !this.isVisible) {
      this.viewContainer.createEmbeddedView(this.templateRef);
      this.isVisible = true;
    } else if (!hasPermission && this.isVisible) {
      this.viewContainer.clear();
      this.isVisible = false;
    }
  }
}

/**
 * Directive structurelle *hasAllRoles
 * Affiche si l'utilisateur a TOUS les rôles spécifiés
 *
 * @example
 * <button *hasAllRoles="['admin', 'verified']">Action sensible</button>
 */
@Directive({
  selector: '[hasAllRoles]',
  standalone: true
})
export class HasAllRolesDirective implements OnInit, OnDestroy {
  private templateRef = inject(TemplateRef<any>);
  private viewContainer = inject(ViewContainerRef);
  private permissionService = inject(PermissionService);

  private roles: string[] = [];
  private isVisible = false;

  @Input()
  set hasAllRoles(roles: string[]) {
    this.roles = roles;
    this.updateView();
  }

  ngOnInit(): void {
    this.updateView();
  }

  ngOnDestroy(): void {
    this.viewContainer.clear();
  }

  private updateView(): void {
    const hasPermission = this.permissionService.hasAllRoles(this.roles);

    if (hasPermission && !this.isVisible) {
      this.viewContainer.createEmbeddedView(this.templateRef);
      this.isVisible = true;
    } else if (!hasPermission && this.isVisible) {
      this.viewContainer.clear();
      this.isVisible = false;
    }
  }
}
