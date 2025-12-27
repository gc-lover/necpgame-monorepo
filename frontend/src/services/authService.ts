import { apiClient, setAuthTokens, clearAuthTokens, getAccessToken } from './api';
import {
  User,
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  AuthTokens,
  Permission
} from '@/types';
import { logger } from '@/utils/logger';

export class AuthService {
  private static readonly BASE_URL = '/api/v1/auth';

  /**
   * Login user with email and password
   */
  static async login(credentials: LoginRequest): Promise<LoginResponse> {
    try {
      logger.info('Attempting user login', { email: credentials.email });

      const response = await apiClient.post<LoginResponse>(
        `${this.BASE_URL}/login`,
        credentials
      );

      const loginData = response.data.data;

      // Store tokens and user data
      setAuthTokens({
        accessToken: loginData.accessToken,
        refreshToken: loginData.refreshToken
      });

      localStorage.setItem('user', JSON.stringify(loginData.user));

      logger.info('User login successful', {
        userId: loginData.user.id,
        email: loginData.user.email
      });

      return loginData;
    } catch (error) {
      logger.error('User login failed', {
        email: credentials.email,
        error: error instanceof Error ? error.message : 'Unknown error'
      });
      throw error;
    }
  }

  /**
   * Register new user
   */
  static async register(userData: RegisterRequest): Promise<User> {
    try {
      logger.info('Attempting user registration', { email: userData.email });

      const response = await apiClient.post<User>(
        `${this.BASE_URL}/register`,
        userData
      );

      const user = response.data.data;

      logger.info('User registration successful', {
        userId: user.id,
        email: user.email
      });

      return user;
    } catch (error) {
      logger.error('User registration failed', {
        email: userData.email,
        error: error instanceof Error ? error.message : 'Unknown error'
      });
      throw error;
    }
  }

  /**
   * Logout current user
   */
  static async logout(): Promise<void> {
    try {
      const token = getAccessToken();
      if (token) {
        // Call logout endpoint to invalidate server-side tokens
        await apiClient.post(`${this.BASE_URL}/logout`);
      }
    } catch (error) {
      logger.warn('Server logout failed, clearing local tokens anyway', { error });
    } finally {
      // Always clear local tokens
      clearAuthTokens();
      localStorage.removeItem('user');

      logger.info('User logout completed');
    }
  }

  /**
   * Refresh access token
   */
  static async refreshToken(): Promise<AuthTokens> {
    try {
      logger.debug('Refreshing access token');

      const response = await apiClient.post<AuthTokens>(
        `${this.BASE_URL}/refresh`
      );

      const tokens = response.data.data;

      // Update stored tokens
      setAuthTokens(tokens);

      logger.debug('Access token refreshed successfully');

      return tokens;
    } catch (error) {
      logger.error('Token refresh failed', { error });
      throw error;
    }
  }

  /**
   * Get current user profile
   */
  static async getCurrentUser(): Promise<User> {
    try {
      const response = await apiClient.get<User>(`${this.BASE_URL}/me`);
      const user = response.data.data;

      // Update stored user data
      localStorage.setItem('user', JSON.stringify(user));

      return user;
    } catch (error) {
      logger.error('Failed to get current user', { error });
      throw error;
    }
  }

  /**
   * Get user permissions
   */
  static async getUserPermissions(userId?: string): Promise<Permission[]> {
    try {
      const url = userId
        ? `/api/v1/users/${userId}/permissions`
        : `${this.BASE_URL}/permissions`;

      const response = await apiClient.get<Permission[]>(url);
      return response.data.data;
    } catch (error) {
      logger.error('Failed to get user permissions', {
        userId,
        error: error instanceof Error ? error.message : 'Unknown error'
      });
      throw error;
    }
  }

  /**
   * Check if user has specific permission
   */
  static async checkPermission(permission: string, resource?: string): Promise<boolean> {
    try {
      const response = await apiClient.post<boolean>(
        `${this.BASE_URL}/check-permission`,
        { permission, resource }
      );
      return response.data.data;
    } catch (error) {
      logger.error('Permission check failed', {
        permission,
        resource,
        error: error instanceof Error ? error.message : 'Unknown error'
      });
      return false;
    }
  }

  /**
   * Update user profile
   */
  static async updateProfile(userData: Partial<User>): Promise<User> {
    try {
      const response = await apiClient.put<User>(
        `${this.BASE_URL}/profile`,
        userData
      );

      const updatedUser = response.data.data;

      // Update stored user data
      localStorage.setItem('user', JSON.stringify(updatedUser));

      logger.info('User profile updated', { userId: updatedUser.id });

      return updatedUser;
    } catch (error) {
      logger.error('Profile update failed', { error });
      throw error;
    }
  }

  /**
   * Change user password
   */
  static async changePassword(currentPassword: string, newPassword: string): Promise<void> {
    try {
      await apiClient.put(`${this.BASE_URL}/password`, {
        currentPassword,
        newPassword
      });

      logger.info('Password changed successfully');
    } catch (error) {
      logger.error('Password change failed', { error });
      throw error;
    }
  }

  /**
   * Request password reset
   */
  static async requestPasswordReset(email: string): Promise<void> {
    try {
      await apiClient.post(`${this.BASE_URL}/password-reset`, { email });

      logger.info('Password reset requested', { email });
    } catch (error) {
      logger.error('Password reset request failed', { email, error });
      throw error;
    }
  }

  /**
   * Reset password with token
   */
  static async resetPassword(token: string, newPassword: string): Promise<void> {
    try {
      await apiClient.post(`${this.BASE_URL}/password-reset/confirm`, {
        token,
        newPassword
      });

      logger.info('Password reset completed');
    } catch (error) {
      logger.error('Password reset failed', { error });
      throw error;
    }
  }

  /**
   * Verify email address
   */
  static async verifyEmail(token: string): Promise<void> {
    try {
      await apiClient.post(`${this.BASE_URL}/verify-email`, { token });

      logger.info('Email verification completed');
    } catch (error) {
      logger.error('Email verification failed', { error });
      throw error;
    }
  }

  /**
   * Get stored user from localStorage
   */
  static getStoredUser(): User | null {
    try {
      const userJson = localStorage.getItem('user');
      return userJson ? JSON.parse(userJson) : null;
    } catch (error) {
      logger.error('Failed to parse stored user', { error });
      return null;
    }
  }

  /**
   * Check if user is authenticated
   */
  static isAuthenticated(): boolean {
    const token = getAccessToken();
    const user = this.getStoredUser();
    return !!(token && user && user.isActive);
  }

  /**
   * Get stored auth tokens
   */
  static getStoredTokens(): AuthTokens | null {
    const accessToken = getAccessToken();
    const refreshToken = localStorage.getItem('refreshToken');

    if (!accessToken || !refreshToken) {
      return null;
    }

    return { accessToken, refreshToken };
  }
}

// Export singleton instance
export const authService = AuthService;
