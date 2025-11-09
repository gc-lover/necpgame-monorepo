import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Alert,
  Box,
  CircularProgress,
  Stack,
  Typography,
} from '@mui/material'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout } from '@/shared/ui/layout'
import { ActionButtons } from '../../components'
import { InitialStateContent, InitialStateSidebar } from '../components'
import { useInitialState } from '../hooks/useInitialState'
import { useGameState } from '../../hooks/useGameState'
import type { GameAction } from '@/api/generated/game/models'

export function InitialStatePage() {
  const navigate = useNavigate()
  const completeTutorial = useGameState((state) => state.completeTutorial)
  const { characterId, initialStateQuery, tutorialQuery } = useInitialState()

  useEffect(() => {
    if (!characterId) {
      navigate('/characters', { replace: true })
    }
  }, [characterId, navigate])

  const handleActionClick = (action: GameAction) => {
    switch (action.id) {
      case 'inventory':
        navigate('/game/inventory')
        break
      case 'move':
        navigate('/game/locations')
        break
      case 'talk-to-npc':
        navigate('/game/npcs')
        break
      case 'look-around':
        navigate('/game/play')
        break
      default:
        break
    }
  }

  if (!characterId) {
    return null
  }

  if (initialStateQuery.isLoading) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box
          sx={{
            flex: 1,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          <CircularProgress size={56} />
        </Box>
      </Box>
    )
  }

  if (initialStateQuery.isError || !initialStateQuery.data) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box
          sx={{
            flex: 1,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            px: 3,
          }}
        >
          <Alert severity="error" sx={{ maxWidth: 520, width: '100%' }}>
            Не удалось загрузить начальное состояние. Попробуйте обновить страницу.
          </Alert>
        </Box>
      </Box>
    )
  }

  const initialState = initialStateQuery.data

  const leftPanel = (
    <Stack spacing={3}>
      <ActionButtons actions={initialState.availableActions} onActionClick={handleActionClick} />
    </Stack>
  )

  const rightPanel = (
    <InitialStateSidebar
      quest={initialState.firstQuest}
      tutorial={tutorialQuery.data}
      onCompleteTutorial={completeTutorial}
      onSkipTutorial={completeTutorial}
    />
  )

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
      <Header />
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h4" sx={{ fontSize: '1.5rem', fontWeight: 700, mb: 1 }}>
              Стартовая локация
            </Typography>
            <Typography variant="body2" sx={{ color: 'text.secondary' }}>
              Проверьте окружение, познакомьтесь с NPC и возьмите первый квест, чтобы начать приключение.
            </Typography>
          </Box>
          <InitialStateContent location={initialState.location} npcs={initialState.availableNPCs} />
        </Stack>
      </GameLayout>
    </Box>
  )
}

