import { create } from 'zustand'
import { persist } from 'zustand/middleware'

/**
 * Глобальное состояние аутентификации
 */
interface AuthState {
  token: string | null
  isAuthenticated: boolean
  setToken: (token: string | null) => void
  logout: () => void
}

/**
 * Глобальное состояние выбранного персонажа
 */
interface CharacterState {
  selectedCharacterId: string | null
  setSelectedCharacterId: (id: string | null) => void
}

/**
 * Глобальное состояние UI
 */
interface UIState {
  sidebarOpen: boolean
  toggleSidebar: () => void
}

/**
 * Store для аутентификации
 */
export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      token: null,
      isAuthenticated: false,
      setToken: (token) => set({ token, isAuthenticated: !!token }),
      logout: () => set({ token: null, isAuthenticated: false }),
    }),
    { name: 'auth-storage' }
  )
)

/**
 * Store для выбранного персонажа
 */
export const useCharacterStore = create<CharacterState>((set) => ({
  selectedCharacterId: null,
  setSelectedCharacterId: (id) => set({ selectedCharacterId: id }),
}))

/**
 * Store для UI состояния
 */
export const useUIStore = create<UIState>((set) => ({
  sidebarOpen: true,
  toggleSidebar: () => set((state) => ({ sidebarOpen: !state.sidebarOpen })),
}))

