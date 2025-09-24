export interface User {
  id: string;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  roles: string[];
  isEmailVerified?: boolean;
}

export interface UserProfile extends User {
  fullName: string;
  initials: string;
  hasAdminRole: boolean;
  hasUserRole: boolean;
}

export interface KeycloakProfile {
  sub?: string;
  name?: string;
  given_name?: string;
  family_name?: string;
  preferred_username?: string;
  email?: string;
  email_verified?: boolean;
  realm_access?: {
    roles: string[];
  };
  resource_access?: {
    [key: string]: {
      roles: string[];
    };
  };
}