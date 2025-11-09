import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Box, Chip } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import HistoryIcon from '@mui/icons-material/History'
import GameLayout from '@/features/game/components/GameLayout'
import { WorldEventCard } from '../components/WorldEventCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useGetActiveEvents20202040,
  useGetEraSettings20202040,
  useGenerateEvent20202040,
} from '@/api/generated/events-2020-2040/era-2020-2040/era-2020-2040'

export const Events2020_2040Page: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [selectedLocationId] = useState('watson_ruins')

  const { data: activeEvents } = useGetActiveEvents20202040({})

  const { data: eraSettings } = useGetEraSettings20202040({})

  const generateEventMutation = useGenerateEvent20202040()

  const handleGenerateEvent = () => {
    if (!selectedCharacterId) return
    generateEventMutation.mutate({
      data: {
        character_id: selectedCharacterId,
        location_id: selectedLocationId,
      },
    })
  }

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="error">
        Era 2020-2040
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Разрушение и восстановление
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Настройки эпохи
      </Typography>
      {eraSettings && (
        <Box>
          <Typography variant="caption" fontSize="0.7rem">
            DC Скейлинг:
          </Typography>
          <Box display="flex" gap={0.3} mt={0.5} flexWrap="wrap">
            <Chip label="SOC 14/18/22" size="small" sx={{ fontSize: '0.6rem' }} />
            <Chip label="TECH 18/22/25" size="small" sx={{ fontSize: '0.6rem' }} />
            <Chip label="CBT 16/20/24" size="small" sx={{ fontSize: '0.6rem' }} />
          </Box>
        </Box>
      )}
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        AI Слайдеры
      </Typography>
      <Stack spacing={0.3}>
        {['Агрессия: 4', 'Экспансия: 3', 'Дипломатия: 2', 'Шпионаж: 4'].map((s, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {s}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Типы событий (d100)
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['01-15: Рад-зоны', '16-30: Спасработы', '31-45: Разбор руин', '46-60: Очаги боёв', '61-75: Сетевые "дыры"', '76-90: Переговоры', '91-00: Чёрный заслон'].map((t, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            {t}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Button onClick={handleGenerateEvent} size="small" variant="contained" fullWidth sx={{ fontSize: '0.7rem' }}>
        Сгенерировать событие
      </Button>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <HistoryIcon sx={{ fontSize: '1.5rem', color: 'error.main' }} />
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Мировые события 2020-2040
        </Typography>
      </Box>
      <Divider />
      <Alert severity="warning" sx={{ fontSize: '0.75rem' }}>
        Эпоха войны Arasaka vs Militech. Ядерные удары, рад-зоны, разрушение инфраструктуры. D&D генератор событий (d100 таблица).
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        Активных событий: {activeEvents?.events?.length || 0}
      </Typography>
      <Grid container spacing={1}>
        {activeEvents?.events?.map((event, index) => (
          <Grid item xs={12} sm={6} md={4} key={event.event_id || index}>
            <WorldEventCard event={event} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default Events2020_2040Page

