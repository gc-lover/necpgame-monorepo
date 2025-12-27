import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import { User, LoginRequest, RegisterRequest } from '@/types';
import { authService } from '@/services/authService';
import { logger } from '@/utils/logger';

interface AuthState {
  // State
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;

  // Actions
  login: (credentials: LoginRequest) => Promise<void>;
  register: (userData: RegisterRequest) => Promise<void>;
  logout: () => Promise<void>;
  refreshToken: () => Promise<void>;
  updateProfile: (updates: Partial<User>) => Promise<void>;
  checkAuth: () => Promise<void>;
  clearError: () => void;
  setUser: (user: User | null) => void;
}

export const useAuthStore = create<AuthState>()(
  devtools(
    persist(
      (set, get) => ({
        // Initial state
        user: null,
        isAuthenticated: false,
        isLoading: false,
        error: null,

        // Actions
        login: async (credentials: LoginRequest) => {
          set({ isLoading: true, error: null });

          try {
            logger.info('Attempting user login', { email: credentials.email });

            const loginData = await authService.login(credentials);

            set({
              user: loginData.user,
              isAuthenticated: true,
              isLoading: false,
              error: null
            });

            logger.info('User login successful', {
              userId: loginData.user.id,
              email: loginData.user.email
            });
          } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Login failed';

            set({
              user: null,
              isAuthenticated: false,
              isLoading: false,
              error: errorMessage
            });

            logger.error('User login failed', {
              email: credentials.email,
              error: errorMessage
            });

            throw error;
          }
        },

        register: async (userData: RegisterRequest) => {
          set({ isLoading: true, error: null });

          try {
            logger.info('Attempting user registration', { email: userData.email });

            const user = await authService.register(userData);

            set({
              user,
              isAuthenticated: false, // Registration doesn't automatically log in
              isLoading: false,
              error: null
            });

            logger.info('User registration successful', {
              userId: user.id,
              email: user.email
            });
          } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Registration failed';

            set({
              isLoading: false,
              error: errorMessage
            });

            logger.error('User registration failed', {
              email: userData.email,
              error: errorMessage
            });

            throw error;
          }
        },

        logout: async () => {
          set({ isLoading: true });

          try {
            await authService.logout();

            set({
              user: null,
              isAuthenticated: false,
              isLoading: false,
              error: null
            });

            logger.info('User logout successful');
          } catch (error) {
            // Even if logout fails, clear local state
            set({
              user: null,
              isAuthenticated: false,
              isLoading: false,
              error: null
            });

            logger.warn('User logout had issues but local state cleared', { error });
          }
        },

        refreshToken: async () => {
          try {
            logger.debug('Refreshing authentication token');

            const tokens = await authService.refreshToken();

            // Token refresh successful, user should still be authenticated
            set({
              isAuthenticated: true,
              error: null
            });

            logger.debug('Authentication token refreshed successfully');
          } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Token refresh failed';

            // Token refresh failed, user should be logged out
            set({
              user: null,
              isAuthenticated: false,
              error: errorMessage
            });

            logger.error('Token refresh failed, user logged out', { error: errorMessage });
          }
        },

        updateProfile: async (updates: Partial<User>) => {
          const currentUser = get().user;
          if (!currentUser) {
            throw new Error('No user logged in');
          }

          set({ isLoading: true, error: null });

          try {
            logger.info('Updating user profile', { userId: currentUser.id });

            const updatedUser = await authService.updateProfile(updates);

            set({
              user: updatedUser,
              isLoading: false,
              error: null
            });

            logger.info('User profile updated successfully', { userId: updatedUser.id });
          } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Profile update failed';

            set({
              isLoading: false,
              error: errorMessage
            });

            logger.error('Profile update failed', {
              userId: currentUser.id,
              error: errorMessage
            });

            throw error;
          }
        },

        checkAuth: async () => {
          try {
            const storedUser = authService.getStoredUser();
            const hasValidTokens = authService.isAuthenticated();

            if (storedUser && hasValidTokens) {
              // Verify token is still valid by calling getCurrentUser
              const currentUser = await authService.getCurrentUser();

              set({
                user: currentUser,
                isAuthenticated: true,
                error: null
              });

              logger.debug('Authentication check passed', { userId: currentUser.id });
            } else {
              set({
                user: null,
                isAuthenticated: false,
                error: null
              });

              logger.debug('Authentication check failed - no valid session');
            }
          } catch (error) {
            // If check fails, clear authentication
            set({
              user: null,
              isAuthenticated: false,
              error: null
            });

            logger.debug('Authentication check failed', { error });
          }
        },

        clearError: () => {
          set({ error: null });
        },

        setUser: (user: User | null) => {
          set({
            user,
            isAuthenticated: !!user,
            error: null
          });
        }
      }),
      {
        name: 'auth-storage',
        partialize: (state) => ({
          user: state.user,
          isAuthenticated: state.isAuthenticated
        })
      }
    ),
    {
      name: 'auth-store'
    }
  )
);

// Selectors for common auth state
export const useAuthUser = () => useAuthStore((state) => state.user);
export const useIsAuthenticated = () => useAuthStore((state) => state.isAuthenticated);
export const useAuthLoading = () => useAuthStore((state) => state.isLoading);
export const useAuthError = () => useAuthStore((state) => state.error);

// Helper hooks
export const useAuthActions = () => useAuthStore((state) => ({
  login: state.login,
  register: state.register,
  logout: state.logout,
  refreshToken: state.refreshToken,
  updateProfile: state.updateProfile,
  checkAuth: state.checkAuth,
  clearError: state.clearError
}));
