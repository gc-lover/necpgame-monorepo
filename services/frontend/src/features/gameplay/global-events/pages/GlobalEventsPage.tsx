import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Tabs, Tab, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import PublicIcon from '@mui/icons-material/Public'
import GameLayout from '@/features/game/components/GameLayout'
import { GlobalEventCard } from '../components/GlobalEventCard'
import {
  useGetGlobalEvents,
  useGetActiveGlobalEvents,
  useGetEventsByEra,
} from '@/api/generated/global-events/global-events/global-events'

export const GlobalEventsPage: React.FC = () => {
  const navigate = useNavigate()
  const [selectedEra, setSelectedEra] = useState<string | undefined>(undefined)
  const [eventTypeFilter, setEventTypeFilter] = useState<string | undefined>(undefined)

  const { data: allEventsData, isLoading } = useGetGlobalEvents({ era: selectedEra, event_type: eventTypeFilter, include_inactive: true }, { query: { enabled: true } })

  const { data: activeEventsData } = useGetActiveGlobalEvents({}, { query: { enabled: true } })

  const handleEventClick = (eventId: string) => {
    console.log('Open event details:', eventId)
  }

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="info">
        Global Events
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Timeline 2020-2093
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Эпохи
      </Typography>
      <Tabs value={selectedEra || 'all'} onChange={(_, v) => setSelectedEra(v === 'all' ? undefined : v)} orientation="vertical" variant="scrollable">
        <Tab value="all" label="Все" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab value="2020-2030" label="2020-2030" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab value="2030-2045" label="2030-2045" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab value="2045-2060" label="2045-2060" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab value="2060-2077" label="2060-2077" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab value="2077" label="2077" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
        <Tab value="2078-2093" label="2078-2093" sx={{ fontSize: '0.7rem', minHeight: 36, alignItems: 'flex-start' }} />
      </Tabs>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Активные события
      </Typography>
      <Divider />
      <Typography variant="caption" fontSize="0.7rem">
        Время: {activeEventsData?.game_time ? new Date(activeEventsData.game_time).toLocaleDateString() : '-'}
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Активных: {activeEventsData?.active_events?.length || 0}
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Типы
      </Typography>
      <Stack spacing={0.5}>
        {['political', 'economic', 'technological', 'environmental', 'social'].map((t, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {t}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <PublicIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Глобальные события
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        Timeline 2020-2093: Political, Economic, Technological, Environmental, Social. Влияние на регионы, фракции, экономику.
      </Alert>
      {isLoading ? (
        <Typography variant="body2" fontSize="0.75rem">
          Загрузка событий...
        </Typography>
      ) : (
        <>
          <Typography variant="body2" fontSize="0.75rem">
            Событий: {allEventsData?.total || 0}
          </Typography>
          <Grid container spacing={1}>
            {allEventsData?.events?.map((event, index) => (
              <Grid item xs={12} sm={6} md={4} key={event.event_id || index}>
                <GlobalEventCard event={event} onClick={handleEventClick} />
              </Grid>
            ))}
          </Grid>
        </>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default GlobalEventsPage

