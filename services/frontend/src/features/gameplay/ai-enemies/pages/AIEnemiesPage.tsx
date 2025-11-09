import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Box,
  Typography,
  Button,
  Stack,
  Alert,
  CircularProgress,
  Divider,
  Tabs,
  Tab,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { EnemyCard } from '../components/EnemyCard'
import {
  useGetEnemyTypes,
  useGetBosses,
} from '@/api/generated/ai-enemies/ai-enemies/ai-enemies'

export const AIEnemiesPage: React.FC = () => {
  const navigate = useNavigate()
  const [tab, setTab] = useState(0)

  const { data: enemiesData, isLoading: loadingEnemies, error: enemiesError } = useGetEnemyTypes()
  const { data: bossesData, isLoading: loadingBosses, error: bossesError } = useGetBosses()

  const leftPanel = (
    <Stack spacing={2}>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/game')}
        fullWidth
        variant="outlined"
        size="small"
        sx={{ fontSize: '0.75rem' }}
      >
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">
        Бестиарий
      </Typography>
      <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
        AI враги и боссы
      </Typography>
      <Divider />
      <Tabs value={tab} onChange={(_, v) => setTab(v)} orientation="vertical">
        <Tab label="Враги" sx={{ fontSize: '0.75rem', alignItems: 'flex-start' }} />
        <Tab label="Боссы" sx={{ fontSize: '0.75rem', alignItems: 'flex-start' }} />
      </Tabs>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Информация
      </Typography>
      <Divider />
      <Typography variant="body2" fontSize="0.75rem" color="text.secondary">
        {tab === 0 ? '10+ типов врагов с AI тактиками' : '3 босса с уникальными механиками'}
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        {tab === 0 ? 'Типы врагов' : 'Боссы'}
      </Typography>
      <Divider />

      {(enemiesError || bossesError) && (
        <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
          Ошибка загрузки
        </Alert>
      )}

      {(loadingEnemies || loadingBosses) && <CircularProgress size={32} />}

      {tab === 0 && !loadingEnemies && enemiesData?.enemy_types && (
        <Box sx={{ overflowY: 'auto', flex: 1 }}>
          <Stack spacing={1.5}>
            {enemiesData.enemy_types.map((enemy: any) => (
              <EnemyCard key={enemy.id} enemy={enemy} />
            ))}
          </Stack>
        </Box>
      )}

      {tab === 1 && !loadingBosses && bossesData?.bosses && (
        <Box sx={{ overflowY: 'auto', flex: 1 }}>
          <Stack spacing={1.5}>
            {bossesData.bosses.map((boss: any) => (
              <EnemyCard key={boss.id} enemy={boss} />
            ))}
          </Stack>
        </Box>
      )}
    </Stack>
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      {centerContent}
    </GameLayout>
  )
}

export default AIEnemiesPage

