import { useCallback, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Alert, Box, CircularProgress, Grid, Typography } from '@mui/material'
import {
  useGetCharacterStatus,
  useGetCharacterStats,
  useGetCharacterSkills,
} from '@/api/generated/character-status/characters/characters'
import {
  StatusOverview,
  CharacterStatsDisplay,
  SkillsListDisplay,
  StatusAdjustmentCard,
} from '../components'
import { useGameState } from '@/features/game/hooks/useGameState'
import { Header } from '@/shared/components/layout/Header'

export function CharacterStatusPage() {
  const navigate = useNavigate()
  const characterId = useGameState((state) => state.selectedCharacterId) || ''

  useEffect(() => {
    if (!characterId) {
      navigate('/characters')
    }
  }, [characterId, navigate])

  const statusQuery = useGetCharacterStatus(characterId, { query: { enabled: !!characterId } })
  const statsQuery = useGetCharacterStats(characterId, { query: { enabled: !!characterId } })
  const skillsQuery = useGetCharacterSkills(characterId, { query: { enabled: !!characterId } })

  const isLoading = statusQuery.isLoading || statsQuery.isLoading || skillsQuery.isLoading
  const hasCriticalError = Boolean(statusQuery.error)
  const skills = skillsQuery.data?.skills ?? []
  const { refetch: refetchStatus } = statusQuery
  const { refetch: refetchStats } = statsQuery
  const { refetch: refetchSkills } = skillsQuery

  const refetchAll = useCallback(() => {
    void refetchStatus()
    void refetchStats()
    void refetchSkills()
  }, [refetchStatus, refetchStats, refetchSkills])

  if (!characterId) {
    return null
  }

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <CircularProgress size={48} />
        </Box>
      </Box>
    )
  }

  if (hasCriticalError) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', p: 3 }}>
          <Alert severity="error" variant="outlined" sx={{ maxWidth: 480 }}>
            {(statusQuery.error as Error)?.message || 'Не удалось загрузить статус персонажа'}
          </Alert>
        </Box>
      </Box>
    )
  }

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      <Header />
      <Box
        sx={{
          flex: 1,
          overflow: 'hidden',
          px: { xs: 2, md: 4 },
          py: { xs: 2, md: 3 },
        }}
      >
        <Box sx={{ mb: 2 }}>
          <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 700, fontSize: '1.1rem' }}>
            Статус персонажа
          </Typography>
          <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.7rem' }}>
            Следите за состоянием, характеристиками и навыками персонажа в режиме одного экрана.
          </Typography>
        </Box>

        <Grid container spacing={2} sx={{ height: { md: '100%' }, overflow: 'hidden auto', pb: { xs: 2, md: 0 } }}>
          <Grid item xs={12} md={4}>
            {statusQuery.data && <StatusOverview status={statusQuery.data} />}
          </Grid>
          <Grid item xs={12} md={4}>
            <StatusAdjustmentCard characterId={characterId} onUpdated={refetchAll} />
          </Grid>
          <Grid item xs={12} md={4}>
            {statsQuery.data && <CharacterStatsDisplay stats={statsQuery.data} />}
          </Grid>
          <Grid item xs={12}>
            <SkillsListDisplay skills={skills} />
          </Grid>
        </Grid>
      </Box>
    </Box>
  )
}

