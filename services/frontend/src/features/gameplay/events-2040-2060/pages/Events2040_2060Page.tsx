import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Box, Chip } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import Brightness3Icon from '@mui/icons-material/Brightness3'
import GameLayout from '@/features/game/components/GameLayout'
import { RedEraEventCard } from '../components/RedEraEventCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useGetActiveEvents20402060,
  useGetEraSettings20402060,
  useGenerateEvent20402060,
} from '@/api/generated/events-2040-2060/era-2040-2060/era-2040-2060'

export const Events2040_2060Page: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [selectedLocationId] = useState('heywood')

  const { data: activeEvents } = useGetActiveEvents20402060({})
  const { data: eraSettings } = useGetEraSettings20402060({})

  const generateEventMutation = useGenerateEvent20402060()

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
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" sx={{ color: '#8B0000' }}>
        Era 2040-2060
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Time of the Red
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
            <Chip label="SOC 15/19/22" size="small" sx={{ fontSize: '0.6rem' }} />
            <Chip label="TECH 18/22/24" size="small" sx={{ fontSize: '0.6rem' }} />
            <Chip label="CBT 14/18/22" size="small" sx={{ fontSize: '0.6rem' }} />
          </Box>
        </Box>
      )}
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        AI Слайдеры
      </Typography>
      <Stack spacing={0.3}>
        {['Агрессия: 3', 'Экспансия: 3', 'Дипломатия: 3', 'Шпионаж: 3'].map((s, i) => (
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
        {['01-15: Киберпсихоз', '16-30: Брейндэнс-утечки', '31-45: Уличные бунты', '46-60: Подпольные клиники', '61-75: Сетевой прорыв', '76-90: Реставрация районов', '91-00: Субкультуры'].map((t, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            {t}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Button onClick={handleGenerateEvent} size="small" variant="contained" fullWidth sx={{ bgcolor: '#8B0000', fontSize: '0.7rem' }}>
        Сгенерировать событие
      </Button>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <Brightness3Icon sx={{ fontSize: '1.5rem', color: '#8B0000' }} />
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Мировые события 2040-2060
        </Typography>
      </Box>
      <Divider />
      <Alert severity="warning" sx={{ fontSize: '0.75rem', bgcolor: 'rgba(139, 0, 0, 0.1)' }}>
        Time of the Red. Восстановление. Киберпсихоз. Брейндэнс-утечки. Новые технологии. Рост субкультур. d100 генератор событий.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        Активных событий: {activeEvents?.events?.length || 0}
      </Typography>
      <Grid container spacing={1}>
        {activeEvents?.events?.map((event, index) => (
          <Grid item xs={12} sm={6} md={4} key={event.event_id || index}>
            <RedEraEventCard event={event} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default Events2040_2060Page

