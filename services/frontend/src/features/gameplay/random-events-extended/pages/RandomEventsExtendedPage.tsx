import { useEffect, useMemo, useState } from 'react'
import {
  Alert,
  Box,
  Button,
  Divider,
  Grid,
  Paper,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import EventIcon from '@mui/icons-material/Event'
import {
  useListRandomEvents,
  useGetRandomEvent,
  useGetActiveEvents,
} from '@/api/generated/random-events-extended/random-events/random-events'
import {
  useTriggerRandomEvent,
  useGenerateEventForLocation,
} from '@/api/generated/random-events-extended/event-triggers/event-triggers'
import { useResolveEvent } from '@/api/generated/random-events-extended/event-history/event-history'
import {
  ListRandomEventsCategory,
  ListRandomEventsLocationType,
  ListRandomEventsPeriod,
} from '@/api/generated/random-events-extended/models'
import type { ListRandomEventsParams } from '@/api/generated/random-events-extended/models'
import { GameLayout } from '@/shared/ui/layout'
import { useGameState } from '@/features/game/hooks/useGameState'
import { RandomEventExtendedCard } from '../components/RandomEventExtendedCard'

const periodOptions = Object.values(ListRandomEventsPeriod)
const categoryOptions = Object.values(ListRandomEventsCategory)
const locationOptions = Object.values(ListRandomEventsLocationType)

export const RandomEventsExtendedPage = () => {
  const { selectedCharacterId } = useGameState()
  const [filters, setFilters] = useState<ListRandomEventsParams>({ page: 1, page_size: 18 })
  const [selectedEventId, setSelectedEventId] = useState<string | undefined>()
  const [characterId, setCharacterId] = useState(selectedCharacterId ?? '')
  const [locationId, setLocationId] = useState('night-city-plaza')
  const [choiceId, setChoiceId] = useState('CHOICE_GOOD')

  const eventsQuery = useListRandomEvents(filters)
  const events = eventsQuery.data?.data.data ?? []

  const eventDetailsQuery = useGetRandomEvent(selectedEventId ?? '', {
    query: { enabled: Boolean(selectedEventId) },
  })

  const activeEventsQuery = useGetActiveEvents(characterId, {
    query: { enabled: Boolean(characterId) },
  })

  const triggerMutation = useTriggerRandomEvent()
  const generateMutation = useGenerateEventForLocation()
  const resolveMutation = useResolveEvent()

  useEffect(() => {
    if (!selectedEventId && events.length > 0) {
      setSelectedEventId(events[0].event_id)
    }
  }, [events, selectedEventId])

  const handleFilterChange = (name: keyof ListRandomEventsParams, value?: string) => {
    setFilters(prev => ({
      ...prev,
      [name]: value && value.length > 0 ? value : undefined,
    }))
  }

  const handleTriggerEvent = async (eventId: string) => {
    if (!characterId || !locationId) return
    await triggerMutation.mutateAsync({
      data: { event_id: eventId, character_id: characterId, location_id: locationId, override_chance: true },
    })
    activeEventsQuery.refetch()
  }

  const handleGenerateEvent = async () => {
    await generateMutation.mutateAsync({
      data: {
        location_id: locationId,
        character_level: 30,
        time_of_day: 'NIGHT',
      },
    })
    activeEventsQuery.refetch()
  }

  const handleResolveEvent = async (instanceId: string) => {
    await resolveMutation.mutateAsync({
      data: { instance_id: instanceId, choice_id: choiceId },
    })
    activeEventsQuery.refetch()
  }

  const eventDetails = eventDetailsQuery.data?.data
  const activeEvents = activeEventsQuery.data?.data.active_events ?? []

  const leftPanel = (
    <Stack spacing={2}>
      <Typography variant="h6" fontSize="1rem" fontWeight={600}>
        Фильтры событий
      </Typography>
      <TextField
        label="Период"
        size="small"
        select
        SelectProps={{ native: true }}
        value={filters.period ?? ''}
        onChange={event => handleFilterChange('period', event.target.value)}
      >
        <option value="">Все</option>
        {periodOptions.map(period => (
          <option key={period} value={period}>
            {period}
          </option>
        ))}
      </TextField>
      <TextField
        label="Категория"
        size="small"
        select
        SelectProps={{ native: true }}
        value={filters.category ?? ''}
        onChange={event => handleFilterChange('category', event.target.value)}
      >
        <option value="">Все</option>
        {categoryOptions.map(category => (
          <option key={category} value={category}>
            {category}
          </option>
        ))}
      </TextField>
      <TextField
        label="Тип локации"
        size="small"
        select
        SelectProps={{ native: true }}
        value={filters.location_type ?? ''}
        onChange={event => handleFilterChange('location_type', event.target.value)}
      >
        <option value="">Все</option>
        {locationOptions.map(location => (
          <option key={location} value={location}>
            {location}
          </option>
        ))}
      </TextField>
      <Divider />
      <Typography variant="subtitle2" fontWeight={600}>
        Генерация события
      </Typography>
      <TextField
        label="Location ID"
        size="small"
        value={locationId}
        onChange={event => setLocationId(event.target.value)}
      />
      <Button variant="contained" size="small" onClick={handleGenerateEvent} disabled={generateMutation.isPending}>
        Сгенерировать
      </Button>
      {generateMutation.error && (
        <Alert severity="error">Не удалось сгенерировать событие для указанной локации.</Alert>
      )}
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="h6" fontSize="1rem" fontWeight={600}>
        Активные события персонажа
      </Typography>
      <TextField
        label="Character ID"
        size="small"
        value={characterId}
        onChange={event => setCharacterId(event.target.value)}
        helperText="Используется для загрузки активных событий"
      />
      <TextField
        label="Choice ID"
        size="small"
        select
        SelectProps={{ native: true }}
        value={choiceId}
        onChange={event => setChoiceId(event.target.value)}
      >
        {['CHOICE_GOOD', 'CHOICE_NEUTRAL', 'CHOICE_BAD'].map(choice => (
          <option key={choice} value={choice}>
            {choice}
          </option>
        ))}
      </TextField>
      <Stack spacing={1}>
        {activeEvents.map(instance => (
          <Paper key={instance.instance_id} variant="outlined" sx={{ p: 1 }}>
            <Stack spacing={0.5}>
              <Typography variant="subtitle2" fontWeight={600}>
                {instance.event?.title ?? instance.instance_id}
              </Typography>
              <Typography variant="caption" color="text.secondary">
                Осталось: {instance.time_remaining_seconds ?? 0} сек
              </Typography>
              <Button
                variant="outlined"
                size="small"
                onClick={() => instance.instance_id && handleResolveEvent(instance.instance_id)}
                disabled={resolveMutation.isPending}
              >
                Завершить событие
              </Button>
            </Stack>
          </Paper>
        ))}
        {!activeEvents.length && (
          <Typography variant="body2" color="text.secondary">
            Активных событий нет.
          </Typography>
        )}
      </Stack>
    </Stack>
  )

  const sortedEvents = useMemo(
    () =>
      events.slice().sort((a, b) => {
        if ((b.risk_level ?? '').localeCompare(a.risk_level ?? '') !== 0) {
          return (b.risk_level ?? '').localeCompare(a.risk_level ?? '')
        }
        return (a.title ?? '').localeCompare(b.title ?? '')
      }),
    [events]
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      <Stack spacing={2} height="100%">
        <Box display="flex" alignItems="center" gap={1}>
          <EventIcon sx={{ fontSize: '1.5rem', color: 'primary.main' }} />
          <Typography variant="h5" fontWeight={600}>
            Расширенная система случайных событий
          </Typography>
        </Box>
        {eventsQuery.isError && <Alert severity="error">Не удалось загрузить события.</Alert>}
        <Typography variant="caption" color="text.secondary">
          Всего событий: {events.length}
        </Typography>
        <Grid container spacing={1.5}>
          {sortedEvents.map(event => (
            <Grid item xs={12} sm={6} md={4} key={event.event_id}>
              <RandomEventExtendedCard
                event={event}
                onTrigger={() => event.event_id && handleTriggerEvent(event.event_id)}
              />
            </Grid>
          ))}
        </Grid>
        {eventDetails && (
          <Paper variant="outlined" sx={{ p: 2 }}>
            <Stack spacing={1}>
              <Typography variant="subtitle2" fontWeight={600}>
                {eventDetails.title}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                {eventDetails.description}
              </Typography>
              <Typography variant="caption" color="text.secondary">
                Выборов: {eventDetails.choices?.length ?? 0} • Последствий:{' '}
                {eventDetails.outcomes?.length ?? 0}
              </Typography>
            </Stack>
          </Paper>
        )}
      </Stack>
    </GameLayout>
  )
}

export default RandomEventsExtendedPage

