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
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Chip,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import RefreshIcon from '@mui/icons-material/Refresh'
import GameLayout from '@/features/game/components/GameLayout'
import { RandomEventCard } from '../components/RandomEventCard'
import { EventDialog } from '../components/EventDialog'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useGetRandomEvent,
  useRespondToEvent,
  useGetActiveEvents,
} from '@/api/generated/events/events/events'
import { RandomEvent, EventResult } from '@/api/generated/events/models'

/**
 * Страница случайных событий
 * Отображает активные события и позволяет генерировать новые
 * Компактная SPA структура с 3-колоночной сеткой
 */
export const EventsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()

  // Состояние для диалога события
  const [selectedEvent, setSelectedEvent] = useState<RandomEvent | null>(null)
  const [eventResult, setEventResult] = useState<EventResult | null>(null)
  const [dialogOpen, setDialogOpen] = useState(false)

  // API запросы
  const {
    data: activeEventsData,
    isLoading: isLoadingActive,
    error: activeError,
    refetch: refetchActive,
  } = useGetActiveEvents(
    { characterId: selectedCharacterId || '' },
    {
      query: {
        enabled: !!selectedCharacterId,
      },
    }
  )

  const { mutate: generateEvent, isPending: isGenerating } = useGetRandomEvent()
  const { mutate: respondToEvent, isPending: isResponding } = useRespondToEvent()

  // Обработчики
  const handleGenerateEvent = () => {
    if (!selectedCharacterId) return

    generateEvent(
      { characterId: selectedCharacterId },
      {
        onSuccess: (data) => {
          setSelectedEvent(data)
          setEventResult(null)
          setDialogOpen(true)
        },
        onError: (error) => {
          console.error('Failed to generate event:', error)
        },
      }
    )
  }

  const handleEventClick = (event: RandomEvent) => {
    setSelectedEvent(event)
    setEventResult(null)
    setDialogOpen(true)
  }

  const handleSelectOption = (optionId: string) => {
    if (!selectedCharacterId || !selectedEvent) return

    respondToEvent(
      {
        data: {
          characterId: selectedCharacterId,
          eventId: selectedEvent.id,
          optionId,
        },
      },
      {
        onSuccess: (result) => {
          setEventResult(result)
          refetchActive()
        },
        onError: (error) => {
          console.error('Failed to respond to event:', error)
        },
      }
    )
  }

  const handleCloseDialog = () => {
    setDialogOpen(false)
    setSelectedEvent(null)
    setEventResult(null)
  }

  // Левая панель - управление событиями
  const leftPanel = (
    <Stack spacing={2} height="100%">
      <Box>
        <Button
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/game')}
          fullWidth
          variant="outlined"
          size="small"
          sx={{ fontSize: '0.75rem', mb: 1 }}
        >
          Назад к игре
        </Button>
        <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">
          События
        </Typography>
        <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
          Случайные события и встречи
        </Typography>
      </Box>

      <Divider />

      {/* Действия */}
      <Box>
        <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" mb={1}>
          Действия
        </Typography>
        <Stack spacing={1}>
          <Button
            variant="contained"
            onClick={handleGenerateEvent}
            disabled={isGenerating || !selectedCharacterId}
            startIcon={isGenerating ? <CircularProgress size={16} /> : <RefreshIcon />}
            fullWidth
            size="small"
            sx={{ fontSize: '0.75rem' }}
          >
            {isGenerating ? 'Генерация...' : 'Новое событие'}
          </Button>
          <Button
            variant="outlined"
            onClick={() => refetchActive()}
            disabled={isLoadingActive}
            fullWidth
            size="small"
            sx={{ fontSize: '0.75rem' }}
          >
            Обновить список
          </Button>
        </Stack>
      </Box>

      <Divider />

      {/* Статистика */}
      <Box>
        <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" mb={1}>
          Статистика
        </Typography>
        <List dense disablePadding>
          <ListItem disablePadding>
            <ListItemText
              primary="Активных событий"
              secondary={activeEventsData?.events?.length || 0}
              primaryTypographyProps={{ fontSize: '0.75rem' }}
              secondaryTypographyProps={{ fontSize: '0.7rem' }}
            />
          </ListItem>
        </List>
      </Box>

      {/* Подсказка */}
      <Box mt="auto">
        <Alert severity="info" sx={{ fontSize: '0.7rem', py: 0.5 }}>
          События могут происходить во время путешествий и исследований
        </Alert>
      </Box>
    </Stack>
  )

  // Правая панель - информация
  const rightPanel = (
    <Stack spacing={2} height="100%">
      <Box>
        <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
          Справка
        </Typography>
      </Box>

      <Divider />

      <Box>
        <Typography variant="body2" fontSize="0.75rem" color="text.secondary" paragraph>
          События - это случайные ситуации, которые могут произойти во время вашего
          путешествия по Night City.
        </Typography>
        <Typography variant="body2" fontSize="0.75rem" color="text.secondary" paragraph>
          Каждое событие предлагает несколько вариантов действий. Ваш выбор влияет на
          результат.
        </Typography>
      </Box>

      <Divider />

      <Box>
        <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" mb={1}>
          Уровни опасности
        </Typography>
        <Stack spacing={1}>
          <Box display="flex" alignItems="center" gap={1}>
            <Chip label="Низкая" color="success" size="small" sx={{ fontSize: '0.65rem' }} />
            <Typography variant="caption" fontSize="0.7rem">
              Безопасные события
            </Typography>
          </Box>
          <Box display="flex" alignItems="center" gap={1}>
            <Chip label="Средняя" color="warning" size="small" sx={{ fontSize: '0.65rem' }} />
            <Typography variant="caption" fontSize="0.7rem">
              Умеренный риск
            </Typography>
          </Box>
          <Box display="flex" alignItems="center" gap={1}>
            <Chip label="Высокая" color="error" size="small" sx={{ fontSize: '0.65rem' }} />
            <Typography variant="caption" fontSize="0.7rem">
              Опасные ситуации
            </Typography>
          </Box>
        </Stack>
      </Box>

      <Divider />

      <Box>
        <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" mb={1}>
          Требования
        </Typography>
        <Typography variant="body2" fontSize="0.75rem" color="text.secondary">
          Некоторые варианты действий требуют определенных характеристик или навыков.
          Выполненные требования отображаются зеленым цветом.
        </Typography>
      </Box>
    </Stack>
  )

  // Центральная панель - список событий
  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box>
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Активные события
        </Typography>
        <Typography variant="body2" color="text.secondary" fontSize="0.75rem">
          События, которые ожидают вашего решения
        </Typography>
      </Box>

      <Divider />

      {/* Ошибки */}
      {activeError && (
        <Alert severity="error" sx={{ fontSize: '0.75rem' }}>
          Ошибка загрузки событий: {(activeError as Error).message}
        </Alert>
      )}

      {/* Загрузка */}
      {isLoadingActive && (
        <Box display="flex" justifyContent="center" alignItems="center" py={4}>
          <CircularProgress size={32} />
        </Box>
      )}

      {/* Список событий */}
      {!isLoadingActive && activeEventsData?.events && (
        <Box sx={{ overflowY: 'auto', flex: 1 }}>
          {activeEventsData.events.length === 0 ? (
            <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
              Нет активных событий. Нажмите "Новое событие" для генерации.
            </Alert>
          ) : (
            <Stack spacing={1.5}>
              {activeEventsData.events.map((event) => (
                <RandomEventCard
                  key={event.id}
                  event={event}
                  onClick={() => handleEventClick(event)}
                />
              ))}
            </Stack>
          )}
        </Box>
      )}
    </Stack>
  )

  return (
    <>
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        {centerContent}
      </GameLayout>

      {/* Диалог события */}
      <EventDialog
        open={dialogOpen}
        event={selectedEvent}
        result={eventResult}
        onClose={handleCloseDialog}
        onSelectOption={handleSelectOption}
        isResponding={isResponding}
      />
    </>
  )
}

export default EventsPage

