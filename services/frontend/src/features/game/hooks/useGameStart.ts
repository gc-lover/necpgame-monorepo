/**
 * Хук для запуска игры
 * 
 * Использует сгенерированные React Query хуки для управления состоянием запуска игры
 * Сохраняет данные из API ответа в Zustand store
 */
import { useNavigate } from 'react-router-dom'
import { useStartGame } from '@/api/generated/game/game-start/game-start'
import { useGameState } from './useGameState'
import type { GameStartRequest } from '@/api/generated/game/models'

export function useGameStart() {
  const navigate = useNavigate()
  const setGameSession = useGameState((state) => state.setGameSession)
  const setCharacterState = useGameState((state) => state.setCharacterState)
  const setStartingEquipment = useGameState((state) => state.setStartingEquipment)

  const { mutate: startGame, isPending, isError, error, data } = useStartGame()

  const handleStartGame = (
    characterId: string,
    skipTutorial: boolean = false,
    onSuccess?: (sessionId: string) => void
  ) => {
    const request: GameStartRequest = {
      characterId,
      skipTutorial,
    }

    startGame(
      { data: request },
      {
        onSuccess: (response) => {
          console.log('✓ Игра успешно запущена:', response)
          
          // Сохраняем данные из API в Zustand store (используем OpenAPI типы!)
          setGameSession(response.gameSessionId)
          setCharacterState(response.characterState)
          setStartingEquipment(response.startingEquipment)
          
          // Вызываем callback если передан
          onSuccess?.(response.gameSessionId)
          
          // Перенаправляем на игровую страницу
          navigate('/game/play')
        },
        onError: (err) => {
          console.error('✗ Ошибка запуска игры:', err)
        },
      }
    )
  }

  return {
    startGame: handleStartGame,
    isLoading: isPending,
    isError,
    error,
    data,
  }
}

