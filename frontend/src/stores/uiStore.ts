import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import { Notification, ModalState, UIState } from '@/types';
import { logger } from '@/utils/logger';

interface UIStore extends UIState {
  // Actions
  setSidebarOpen: (open: boolean) => void;
  toggleSidebar: () => void;
  setTheme: (theme: 'light' | 'dark') => void;
  setLoading: (loading: boolean) => void;

  // Notification actions
  addNotification: (notification: Omit<Notification, 'id'>) => void;
  removeNotification: (id: string) => void;
  clearNotifications: () => void;

  // Modal actions
  openModal: (modal: Omit<ModalState, 'isOpen'>) => void;
  closeModal: (id: string) => void;
  closeAllModals: () => void;

  // Utility actions
  reset: () => void;
}

const initialState: UIState = {
  sidebarOpen: true,
  theme: 'light',
  loading: false,
  notifications: [],
  modals: []
};

export const useUIStore = create<UIStore>()(
  devtools(
    persist(
      (set, get) => ({
        ...initialState,

        // Sidebar actions
        setSidebarOpen: (open: boolean) => {
          set({ sidebarOpen: open });
          logger.debug('Sidebar state changed', { open });
        },

        toggleSidebar: () => {
          set((state) => ({ sidebarOpen: !state.sidebarOpen }));
          logger.debug('Sidebar toggled', { newState: !get().sidebarOpen });
        },

        // Theme actions
        setTheme: (theme: 'light' | 'dark') => {
          set({ theme });
          // Update document class for Tailwind dark mode
          document.documentElement.classList.toggle('dark', theme === 'dark');
          logger.debug('Theme changed', { theme });
        },

        // Loading actions
        setLoading: (loading: boolean) => {
          set({ loading });
          if (loading) {
            logger.debug('Loading state activated');
          }
        },

        // Notification actions
        addNotification: (notificationData) => {
          const notification: Notification = {
            id: `notification-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
            ...notificationData
          };

          set((state) => ({
            notifications: [...state.notifications, notification]
          }));

          logger.info('Notification added', {
            id: notification.id,
            type: notification.type,
            title: notification.title
          });

          // Auto-remove notification after duration
          if (notification.duration !== 0) {
            const duration = notification.duration || 5000; // Default 5 seconds
            setTimeout(() => {
              get().removeNotification(notification.id);
            }, duration);
          }
        },

        removeNotification: (id: string) => {
          set((state) => ({
            notifications: state.notifications.filter(n => n.id !== id)
          }));

          logger.debug('Notification removed', { id });
        },

        clearNotifications: () => {
          set({ notifications: [] });
          logger.debug('All notifications cleared');
        },

        // Modal actions
        openModal: (modalData) => {
          const modal: ModalState = {
            ...modalData,
            isOpen: true
          };

          set((state) => ({
            modals: [...state.modals, modal]
          }));

          logger.info('Modal opened', {
            id: modal.id,
            type: modal.type
          });
        },

        closeModal: (id: string) => {
          set((state) => ({
            modals: state.modals.map(modal =>
              modal.id === id ? { ...modal, isOpen: false } : modal
            )
          }));

          // Remove modal after animation
          setTimeout(() => {
            set((state) => ({
              modals: state.modals.filter(modal => modal.id !== id)
            }));
          }, 300); // Match CSS transition duration

          logger.debug('Modal closed', { id });
        },

        closeAllModals: () => {
          set((state) => ({
            modals: state.modals.map(modal => ({ ...modal, isOpen: false }))
          }));

          // Remove all modals after animation
          setTimeout(() => {
            set({ modals: [] });
          }, 300);

          logger.debug('All modals closed');
        },

        // Utility actions
        reset: () => {
          set(initialState);
          logger.debug('UI state reset to initial state');
        }
      }),
      {
        name: 'ui-storage',
        partialize: (state) => ({
          sidebarOpen: state.sidebarOpen,
          theme: state.theme
        })
      }
    ),
    {
      name: 'ui-store'
    }
  )
);

// Selectors for common UI state
export const useSidebarOpen = () => useUIStore((state) => state.sidebarOpen);
export const useTheme = () => useUIStore((state) => state.theme);
export const useLoading = () => useUIStore((state) => state.loading);
export const useNotifications = () => useUIStore((state) => state.notifications);
export const useModals = () => useUIStore((state) => state.modals);

// Helper hooks
export const useUIActions = () => useUIStore((state) => ({
  setSidebarOpen: state.setSidebarOpen,
  toggleSidebar: state.toggleSidebar,
  setTheme: state.setTheme,
  setLoading: state.setLoading,
  addNotification: state.addNotification,
  removeNotification: state.removeNotification,
  clearNotifications: state.clearNotifications,
  openModal: state.openModal,
  closeModal: state.closeModal,
  closeAllModals: state.closeAllModals,
  reset: state.reset
}));

// Convenience hooks for common notifications
export const useNotificationActions = () => {
  const { addNotification } = useUIActions();

  return {
    showSuccess: (title: string, message?: string) =>
      addNotification({ type: 'success', title, message }),

    showError: (title: string, message?: string) =>
      addNotification({ type: 'error', title, message }),

    showWarning: (title: string, message?: string) =>
      addNotification({ type: 'warning', title, message }),

    showInfo: (title: string, message?: string) =>
      addNotification({ type: 'info', title, message })
  };
};

// Convenience hooks for common modals
export const useModalActions = () => {
  const { openModal, closeModal } = useUIActions();

  return {
    openConfirmModal: (title: string, message: string, onConfirm: () => void) =>
      openModal({
        id: `confirm-${Date.now()}`,
        type: 'confirm',
        props: { title, message, onConfirm }
      }),

    openFormModal: (title: string, formComponent: React.ComponentType, props?: any) =>
      openModal({
        id: `form-${Date.now()}`,
        type: 'form',
        props: { title, formComponent, ...props }
      }),

    closeModal
  };
};
