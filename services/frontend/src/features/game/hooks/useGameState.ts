/**
 * Хук для управления игровым состоянием
 * 
 * Централизованное управление состоянием игры:
 * - Текущая игровая сессия
 * - Выбранный персонаж
 * - Состояние туториала
 * - Состояние персонажа из API
 * - Стартовое снаряжение из API
 */
import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import type { GameCharacterState, GameStartingItem } from '@/api/generated/game/models'

interface GameState {
  // ID текущей игровой сессии
  gameSessionId: string | null
  // ID выбранного персонажа
  selectedCharacterId: string | null
  // Включен ли туториал
  tutorialEnabled: boolean
  // Текущий шаг туториала
  tutorialStep: number
  // Завершен ли туториал
  tutorialCompleted: boolean
  // Состояние персонажа из API (POST /game/start)
  characterState: GameCharacterState | null
  // Стартовое снаряжение из API (POST /game/start)
  startingEquipment: GameStartingItem[] | null
  
  // Actions
  setGameSession: (sessionId: string) => void
  setSelectedCharacter: (characterId: string) => void
  setTutorialEnabled: (enabled: boolean) => void
  setTutorialStep: (step: number) => void
  completeTutorial: () => void
  setCharacterState: (state: GameCharacterState) => void
  setStartingEquipment: (equipment: GameStartingItem[]) => void
  resetGame: () => void
}

/**
 * Zustand store для игрового состояния
 * Сохраняется в localStorage
 */
export const useGameState = create<GameState>()(
  persist(
    (set) => ({
      gameSessionId: null,
      selectedCharacterId: null,
      tutorialEnabled: true,
      tutorialStep: 0,
      tutorialCompleted: false,
      characterState: null,
      startingEquipment: null,

      setGameSession: (sessionId) => set({ gameSessionId: sessionId }),
      
      setSelectedCharacter: (characterId) => set({ selectedCharacterId: characterId }),
      
      setTutorialEnabled: (enabled) => set({ tutorialEnabled: enabled }),
      
      setTutorialStep: (step) => set({ tutorialStep: step }),
      
      completeTutorial: () => set({ tutorialCompleted: true, tutorialEnabled: false }),
      
      setCharacterState: (state) => set({ characterState: state }),
      
      setStartingEquipment: (equipment) => set({ startingEquipment: equipment }),
      
      resetGame: () =>
        set({
          gameSessionId: null,
          tutorialStep: 0,
          tutorialCompleted: false,
          characterState: null,
          startingEquipment: null,
        }),
    }),
    {
      name: 'game-state-storage',
    }
  )
)

/**
 * Хук-helper для получения текущего персонажа
 */
export function useSelectedCharacter() {
  const characterId = useGameState((state) => state.selectedCharacterId)
  return characterId
}

/**
 * Хук-helper для проверки состояния туториала
 */
export function useTutorialState() {
  const tutorialEnabled = useGameState((state) => state.tutorialEnabled)
  const tutorialStep = useGameState((state) => state.tutorialStep)
  const tutorialCompleted = useGameState((state) => state.tutorialCompleted)
  
  return {
    enabled: tutorialEnabled,
    currentStep: tutorialStep,
    completed: tutorialCompleted,
  }
}

