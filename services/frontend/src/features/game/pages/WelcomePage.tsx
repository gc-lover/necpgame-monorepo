/**
 * Страница приветствия перед началом игры
 */
import { useEffect, useMemo, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { Alert, Box, CircularProgress, Grid, Typography } from '@mui/material'
import { useGetWelcomeScreen } from '@/api/generated/game/game-start/game-start'
import { useGameStart } from '../hooks/useGameStart'
import { useGameState } from '../hooks/useGameState'
import { Header } from '@/shared/components/layout/Header'
import {
  CharacterSummaryCard,
  SessionResumeCard,
  StartActionsCard,
  StartContextCard,
  WelcomeHeroCard,
} from '../start/components'

export function WelcomePage() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const characterId = searchParams.get('characterId')

  const setSelectedCharacter = useGameState((state) => state.setSelectedCharacter)
  const setTutorialEnabled = useGameState((state) => state.setTutorialEnabled)
  const gameSessionId = useGameState((state) => state.gameSessionId)

  const { data, isLoading, error } = useGetWelcomeScreen(
    { characterId: characterId || '' },
    {
      query: {
        enabled: !!characterId,
      },
    }
  )

  const { startGame, isLoading: isStarting, isError: startHasError, error: startMutationError } = useGameStart()
  const [startError, setStartError] = useState<string | null>(null)

  useEffect(() => {
    if (characterId) {
      setSelectedCharacter(characterId)
    }
  }, [characterId, setSelectedCharacter])

  useEffect(() => {
    if (!characterId) {
      navigate('/characters')
    }
  }, [characterId, navigate])

  const hasPreviousSession = useMemo(() => Boolean(gameSessionId), [gameSessionId])

  useEffect(() => {
    if (startHasError && startMutationError) {
      const message =
        (startMutationError as { message?: string }).message ?? 'Не удалось начать игру. Попробуйте снова.'
      setStartError(message)
    }
  }, [startHasError, startMutationError])

  const handleStartGame = (skipTutorial: boolean) => {
    if (!characterId) {
      return
    }

    setStartError(null)
    setTutorialEnabled(!skipTutorial)
    startGame(characterId, skipTutorial)
  }

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1 }}>
          <CircularProgress size={60} />
        </Box>
      </Box>
    )
  }

  if (error || !data) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1, p: 3 }}>
          <Alert severity="error" sx={{ maxWidth: 600 }}>
            <Typography variant="h6">Ошибка загрузки</Typography>
            <Typography variant="body2">
              {error?.message || 'Не удалось загрузить приветственный экран'}
            </Typography>
          </Alert>
        </Box>
      </Box>
    )
  }

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
      <Header />
      <Box
        sx={{
          flex: 1,
          overflow: 'hidden',
          px: { xs: 2, md: 4 },
          py: { xs: 2, md: 3 },
        }}
      >
        <Grid
          container
          spacing={2}
          sx={{
            height: { md: '100%' },
            overflow: 'hidden auto',
            pb: { xs: 2, md: 0 },
          }}
        >
          <Grid item xs={12} md={6} sx={{ display: 'flex' }}>
            <WelcomeHeroCard message={data.message} subtitle={data.subtitle} />
          </Grid>
          <Grid item xs={12} md={3} sx={{ display: 'flex' }}>
            <CharacterSummaryCard character={data.character} startingLocation={data.startingLocation} />
          </Grid>
          <Grid item xs={12} md={3} sx={{ display: 'flex' }}>
            <StartActionsCard
              buttons={data.buttons}
              onAction={handleStartGame}
              isLoading={isStarting}
              error={startError}
            />
          </Grid>
          <Grid item xs={12} md={6} sx={{ display: 'flex' }}>
            <StartContextCard />
          </Grid>
          <Grid item xs={12} md={6} sx={{ display: 'flex' }}>
            <SessionResumeCard
              hasSession={hasPreviousSession}
              onResume={() => hasPreviousSession && navigate('/game/play')}
            />
          </Grid>
        </Grid>
      </Box>
    </Box>
  )
}

