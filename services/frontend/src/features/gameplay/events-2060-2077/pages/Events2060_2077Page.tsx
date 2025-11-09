import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Box, Chip } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import BusinessIcon from '@mui/icons-material/Business'
import GameLayout from '@/features/game/components/GameLayout'
import { ModernEraEventCard } from '../components/ModernEraEventCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useGetActiveEvents20602077,
  useGetEraSettings20602077,
  useGenerateEvent20602077,
} from '@/api/generated/events-2060-2077/era-2060-2077/era-2060-2077'

export const Events2060_2077Page: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [selectedLocationId] = useState('city_center')

  const { data: activeEvents } = useGetActiveEvents20602077({})
  const { data: eraSettings } = useGetEraSettings20602077({})

  const generateEventMutation = useGenerateEvent20602077()

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
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" sx={{ color: '#FFD700' }}>
        Era 2060-2077
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Преддверие современности
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Настройки эпохи
      </Typography>
      {eraSettings && (
        <Box>
          <Typography variant="caption" fontSize="0.7rem">
            DC Скейлинг (топовый):
          </Typography>
          <Box display="flex" gap={0.3} mt={0.5} flexWrap="wrap">
            <Chip label="SOC 16/20/24" size="small" sx={{ fontSize: '0.6rem' }} />
            <Chip label="TECH 20/24/28" size="small" sx={{ fontSize: '0.6rem' }} />
            <Chip label="CBT 16/20/24" size="small" sx={{ fontSize: '0.6rem' }} />
          </Box>
        </Box>
      )}
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        AI Слайдеры
      </Typography>
      <Stack spacing={0.3}>
        {['Агрессия: 3', 'Экспансия: 4', 'Дипломатия: 4', 'Шпионаж: 5'].map((s, i) => (
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
        {['01-15: Корпорат. аудит', '16-30: Подполье', '31-45: Нейроинтерфейс сбои', '46-60: Высокий протокол', '61-75: Полит. интрига', '76-90: Космологистика', '91-00: Night City независимость'].map(
          (t, i) => (
            <Typography key={i} variant="caption" fontSize="0.7rem">
              {t}
            </Typography>
          )
        )}
      </Stack>
      <Divider />
      <Button onClick={handleGenerateEvent} size="small" variant="contained" fullWidth sx={{ bgcolor: '#FFD700', color: 'black', fontSize: '0.7rem' }}>
        Сгенерировать событие
      </Button>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <BusinessIcon sx={{ fontSize: '1.5rem', color: '#FFD700' }} />
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Мировые события 2060-2077
        </Typography>
      </Box>
      <Divider />
      <Alert severity="warning" sx={{ fontSize: '0.75rem', bgcolor: 'rgba(255, 215, 0, 0.1)' }}>
        Пик корпоративного контроля! Night City становится независимым. Космос, топовые импланты, глобальная сеть. d100 генератор событий.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        Активных событий: {activeEvents?.events?.length || 0}
      </Typography>
      <Grid container spacing={1}>
        {activeEvents?.events?.map((event, index) => (
          <Grid item xs={12} sm={6} md={4} key={event.event_id || index}>
            <ModernEraEventCard event={event} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default Events2060_2077Page

