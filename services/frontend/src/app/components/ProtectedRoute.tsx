/**
 * Компонент защищенного роута
 * 
 * Проверяет наличие выбранного персонажа перед доступом к игровым страницам
 */
import { Navigate } from 'react-router-dom'
import { useGameState } from '@/features/game/hooks/useGameState'

interface ProtectedRouteProps {
  children: React.ReactNode
  requireCharacter?: boolean
}

export function ProtectedRoute({ children, requireCharacter = true }: ProtectedRouteProps) {
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)

  // Если требуется персонаж и он не выбран - редирект на выбор персонажа
  if (requireCharacter && !selectedCharacterId) {
    return <Navigate to="/characters" replace />
  }

  return <>{children}</>
}

