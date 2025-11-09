import { useGetInitialState, useGetTutorialSteps } from '@/api/generated/game/initial-state/initial-state'
import { useGameState, useTutorialState } from '../../hooks/useGameState'

export function useInitialState() {
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)
  const tutorial = useTutorialState()

  const initialStateQuery = useGetInitialState(
    { characterId: selectedCharacterId || '' },
    {
      query: {
        enabled: Boolean(selectedCharacterId),
        staleTime: 1000 * 60,
      },
    }
  )

  const tutorialQuery = useGetTutorialSteps(
    { characterId: selectedCharacterId || '' },
    {
      query: {
        enabled: Boolean(selectedCharacterId && tutorial.enabled && !tutorial.completed),
        staleTime: 1000 * 60,
      },
    }
  )

  return {
    characterId: selectedCharacterId,
    tutorial,
    initialStateQuery,
    tutorialQuery,
  }
}

