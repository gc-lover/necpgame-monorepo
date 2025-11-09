import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  CardHeader,
  Chip,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import SearchIcon from '@mui/icons-material/Search'
import HotelIcon from '@mui/icons-material/Hotel'
import PrecisionManufacturingIcon from '@mui/icons-material/PrecisionManufacturing'
import LockOpenIcon from '@mui/icons-material/LockOpen'
import type { HackSystemBodyMethod } from '@/api/generated/actions/models'
import {
  useExploreLocation,
  useRestAction,
  useUseObject,
  useHackSystem,
} from '@/api/generated/actions/gameplay/gameplay'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import { ActionPromptDialog, ActionResultDialog } from '../components'
import { useGameState } from '@/features/game/hooks/useGameState'

interface ActionHistoryEntry {
  id: string
  title: string
  detail: string
  success: boolean
  timestamp: string
}

interface DialogState {
  open: boolean
  title: string
  success: boolean
  result?: {
    description?: string
    healthRestored?: number
    energyRestored?: number
    timePassed?: number
    pointsOfInterest?: string[]
    hiddenObjects?: string[]
    dataAccessed?: string[]
    reward?: unknown
  }
}

function getErrorMessage(error: unknown): string {
  if (error instanceof Error) {
    return error.message
  }
  if (typeof error === 'string') {
    return error
  }
  return 'Не удалось выполнить действие. Попробуйте позже.'
}

export function ActionsPage() {
  const navigate = useNavigate()
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)

  const [locationId, setLocationId] = useState('')
  const [restDuration, setRestDuration] = useState('60')
  const [promptMode, setPromptMode] = useState<'use' | 'hack' | null>(null)
  const [errorMessage, setErrorMessage] = useState<string | null>(null)
  const [history, setHistory] = useState<ActionHistoryEntry[]>([])
  const [dialogState, setDialogState] = useState<DialogState>({ open: false, title: '', success: true })

  useEffect(() => {
    if (!selectedCharacterId) {
      navigate('/characters')
    }
  }, [selectedCharacterId, navigate])

  const addHistoryEntry = (entry: Omit<ActionHistoryEntry, 'id' | 'timestamp'>) => {
    const timestamp = new Date().toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' })
    setHistory((previous) => [
      {
        id: `${Date.now()}-${Math.random().toString(36).slice(2)}`,
        timestamp,
        ...entry,
      },
      ...previous,
    ].slice(0, 8))
  }

  const handleSuccess = (title: string, detail: string) => {
    addHistoryEntry({ title, detail, success: true })
    setErrorMessage(null)
  }

  const handleError = (title: string, detail: string) => {
    addHistoryEntry({ title, detail, success: false })
    setErrorMessage(detail)
  }

  const exploreMutation = useExploreLocation({
    mutation: {
      onSuccess: (data) => {
        setDialogState({
          open: true,
          title: 'Осмотр локации',
          success: true,
          result: {
            description: data.description ?? 'Вы осмотрели окрестности.',
            pointsOfInterest: data.pointsOfInterest ?? [],
            hiddenObjects: data.hiddenObjects ?? undefined,
          },
        })
        handleSuccess('Осмотр локации', data.description ?? 'Локация исследована.')
      },
      onError: (error) => {
        const message = getErrorMessage(error)
        handleError('Осмотр локации', message)
      },
    },
  })

  const restMutation = useRestAction({
    mutation: {
      onSuccess: (data) => {
        setDialogState({
          open: true,
          title: 'Отдых',
          success: true,
          result: {
            description: 'Отдых завершён.',
            healthRestored: data.healthRestored,
            energyRestored: data.energyRestored,
            timePassed: data.timePassed,
          },
        })
        handleSuccess('Отдых', `Восстановлено ${data.healthRestored} HP и ${data.energyRestored} энергии.`)
      },
      onError: (error) => {
        const message = getErrorMessage(error)
        handleError('Отдых', message)
      },
    },
  })

  const useObjectMutation = useUseObject({
    mutation: {
      onSuccess: (data) => {
        setDialogState({
          open: true,
          title: 'Использование объекта',
          success: Boolean(data.success),
          result: {
            description: data.result ?? 'Объект активирован.',
            reward: data.reward ?? undefined,
          },
        })
        handleSuccess('Использование объекта', data.result ?? 'Объект использован.')
      },
      onError: (error) => {
        const message = getErrorMessage(error)
        handleError('Использование объекта', message)
      },
    },
  })

  const hackSystemMutation = useHackSystem({
    mutation: {
      onSuccess: (data) => {
        setDialogState({
          open: true,
          title: 'Взлом системы',
          success: Boolean(data.success),
          result: {
            description: data.result ?? 'Взлом выполнен.',
            dataAccessed: data.dataAccessed ?? undefined,
          },
        })
        const detail = data.success ? 'Доступ к данным получен.' : 'Взлом не удался.'
        handleSuccess('Взлом системы', detail)
      },
      onError: (error) => {
        const message = getErrorMessage(error)
        handleError('Взлом системы', message)
      },
    },
  })

  const isLoading = exploreMutation.isPending || restMutation.isPending || useObjectMutation.isPending || hackSystemMutation.isPending

  const handleExplore = () => {
    if (!selectedCharacterId) {
      setErrorMessage('Сначала выберите персонажа.')
      return
    }

    if (!locationId.trim()) {
      setErrorMessage('Укажите ID локации для осмотра.')
      return
    }

    setErrorMessage(null)

    exploreMutation.mutate({
      data: {
        characterId: selectedCharacterId,
        locationId: locationId.trim(),
      },
    })
  }

  const handleRest = () => {
    if (!selectedCharacterId) {
      setErrorMessage('Сначала выберите персонажа.')
      return
    }

    const durationValue = parseInt(restDuration, 10)
    if (Number.isNaN(durationValue) || durationValue <= 0) {
      setErrorMessage('Длительность отдыха должна быть положительным числом минут.')
      return
    }

    setErrorMessage(null)

    restMutation.mutate({
      data: {
        characterId: selectedCharacterId,
        duration: durationValue,
      },
    })
  }

  const handleUseSubmit = (values: { objectId: string; locationId: string }) => {
    if (!selectedCharacterId) {
      setErrorMessage('Сначала выберите персонажа.')
      return
    }

    const resolvedLocationId = values.locationId.trim() || locationId.trim()
    if (!resolvedLocationId) {
      setErrorMessage('Укажите ID локации для использования объекта.')
      return
    }

    setPromptMode(null)
    setErrorMessage(null)

    useObjectMutation.mutate({
      data: {
        characterId: selectedCharacterId,
        locationId: resolvedLocationId,
        objectId: values.objectId.trim(),
      },
    })
  }

  const handleHackSubmit = (values: { targetId: string; method: HackSystemBodyMethod }) => {
    if (!selectedCharacterId) {
      setErrorMessage('Сначала выберите персонажа.')
      return
    }

    setPromptMode(null)
    setErrorMessage(null)

    hackSystemMutation.mutate({
      data: {
        characterId: selectedCharacterId,
        targetId: values.targetId.trim(),
        method: values.method,
      },
    })
  }

  const leftPanel = useMemo(() => (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, height: '100%', minHeight: 0 }}>
      <Typography
        variant="h6"
        sx={{
          color: 'primary.main',
          textShadow: '0 0 8px currentColor',
          fontWeight: 'bold',
          fontSize: '0.9rem',
          textTransform: 'uppercase',
          letterSpacing: '0.08em',
        }}
      >
        Доступные действия
      </Typography>
      <Box sx={{ flex: 1, overflowY: 'auto' }}>
        <List dense sx={{ color: 'text.secondary' }}>
          <ListItem sx={{ alignItems: 'flex-start' }}>
            <ListItemIcon sx={{ minWidth: 32, mt: 0.5 }}>
              <SearchIcon fontSize="small" color="primary" />
            </ListItemIcon>
            <ListItemText
              primary="Осмотреть локацию"
              secondary="Получите описание окружения, точки интереса и скрытые объекты."
              primaryTypographyProps={{ fontSize: '0.85rem', color: 'text.primary' }}
              secondaryTypographyProps={{ fontSize: '0.75rem' }}
            />
          </ListItem>
          <ListItem sx={{ alignItems: 'flex-start' }}>
            <ListItemIcon sx={{ minWidth: 32, mt: 0.5 }}>
              <HotelIcon fontSize="small" color="secondary" />
            </ListItemIcon>
            <ListItemText
              primary="Отдохнуть"
              secondary="Восстановите здоровье и энергию, указав продолжительность отдыха."
              primaryTypographyProps={{ fontSize: '0.85rem', color: 'text.primary' }}
              secondaryTypographyProps={{ fontSize: '0.75rem' }}
            />
          </ListItem>
          <ListItem sx={{ alignItems: 'flex-start' }}>
            <ListItemIcon sx={{ minWidth: 32, mt: 0.5 }}>
              <PrecisionManufacturingIcon fontSize="small" color="info" />
            </ListItemIcon>
            <ListItemText
              primary="Использовать объект"
              secondary="Активируйте терминалы, двери и другие объекты в текущей локации."
              primaryTypographyProps={{ fontSize: '0.85rem', color: 'text.primary' }}
              secondaryTypographyProps={{ fontSize: '0.75rem' }}
            />
          </ListItem>
          <ListItem sx={{ alignItems: 'flex-start' }}>
            <ListItemIcon sx={{ minWidth: 32, mt: 0.5 }}>
              <LockOpenIcon fontSize="small" color="warning" />
            </ListItemIcon>
            <ListItemText
              primary="Взломать систему"
              secondary="Выберите цель и метод (breach, quickhack, daemon), чтобы получить доступ к данным."
              primaryTypographyProps={{ fontSize: '0.85rem', color: 'text.primary' }}
              secondaryTypographyProps={{ fontSize: '0.75rem' }}
            />
          </ListItem>
        </List>
      </Box>
    </Box>
  ), [])

  const rightPanel = (
    <StatsPanel>
      <Card variant="outlined">
        <CardHeader
          title="Журнал действий"
          subheader="Отслеживание последних операций персонажа"
          titleTypographyProps={{ fontSize: '0.95rem', fontWeight: 600 }}
          subheaderTypographyProps={{ fontSize: '0.75rem' }}
        />
        <CardContent sx={{ maxHeight: 320, overflowY: 'auto', pt: 1 }}>
          {history.length === 0 ? (
            <Typography variant="caption" color="text.secondary" sx={{ fontSize: '0.75rem' }}>
              История действий появится после выполнения первого запроса.
            </Typography>
          ) : (
            <Stack spacing={1.2}>
              {history.map((entry) => (
                <Stack
                  key={entry.id}
                  direction="row"
                  spacing={1}
                  alignItems="center"
                  sx={{
                    border: '1px solid',
                    borderColor: entry.success ? 'success.light' : 'error.light',
                    borderRadius: 1,
                    p: 1,
                  }}
                >
                  <Chip
                    label={entry.timestamp}
                    size="small"
                    sx={{ fontSize: '0.65rem' }}
                    color="default"
                  />
                  <Box sx={{ flex: 1 }}>
                    <Typography variant="body2" sx={{ fontSize: '0.8rem', color: 'text.primary' }}>
                      {entry.title}
                    </Typography>
                    <Typography
                      variant="caption"
                      sx={{ fontSize: '0.72rem', color: entry.success ? 'success.main' : 'error.main' }}
                    >
                      {entry.detail}
                    </Typography>
                  </Box>
                </Stack>
              ))}
            </Stack>
          )}
        </CardContent>
      </Card>
    </StatsPanel>
  )

  if (!selectedCharacterId) {
    return null
  }

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      <Header />
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2, overflowY: 'auto', py: 2 }}>
          <Typography variant="h4" sx={{ fontSize: '1.4rem', fontWeight: 700 }}>
            Управление действиями в локации
          </Typography>
          <Typography variant="body2" sx={{ color: 'text.secondary', fontSize: '0.85rem' }}>
            Используйте доступные действия, чтобы исследовать окружение, отдыхать, взаимодействовать с объектами
            и взламывать системы. Все запросы выполняются через OpenAPI спецификацию `api/v1/gameplay/actions/actions.yaml`.
          </Typography>

          {errorMessage && (
            <Alert severity="error" onClose={() => setErrorMessage(null)} sx={{ fontSize: '0.85rem' }}>
              {errorMessage}
            </Alert>
          )}

          <Card variant="outlined">
            <CardHeader
              title="Осмотреть локацию"
              subheader="POST /gameplay/actions/explore"
              titleTypographyProps={{ fontSize: '1rem', fontWeight: 600 }}
              subheaderTypographyProps={{ fontSize: '0.75rem' }}
            />
            <CardContent>
              <Stack spacing={2}>
                <TextField
                  label="ID локации"
                  size="small"
                  value={locationId}
                  onChange={(event) => setLocationId(event.target.value)}
                  placeholder="loc-downtown-001"
                />
                <Button
                  variant="contained"
                  size="small"
                  onClick={handleExplore}
                  disabled={exploreMutation.isPending}
                  sx={{ alignSelf: 'flex-start', fontSize: '0.8rem' }}
                >
                  {exploreMutation.isPending ? 'Осмотр...' : 'Осмотреть'}
                </Button>
              </Stack>
            </CardContent>
          </Card>

          <Card variant="outlined">
            <CardHeader
              title="Отдохнуть"
              subheader="POST /gameplay/actions/rest"
              titleTypographyProps={{ fontSize: '1rem', fontWeight: 600 }}
              subheaderTypographyProps={{ fontSize: '0.75rem' }}
            />
            <CardContent>
              <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} alignItems={{ sm: 'center' }}>
                <TextField
                  label="Длительность (минуты)"
                  size="small"
                  value={restDuration}
                  onChange={(event) => setRestDuration(event.target.value)}
                  sx={{ maxWidth: 200 }}
                />
                <Button
                  variant="contained"
                  size="small"
                  onClick={handleRest}
                  disabled={restMutation.isPending}
                  sx={{ fontSize: '0.8rem' }}
                >
                  {restMutation.isPending ? 'Отдых...' : 'Начать отдых'}
                </Button>
              </Stack>
            </CardContent>
          </Card>

          <Card variant="outlined">
            <CardHeader
              title="Продвинутые действия"
              subheader="POST /gameplay/actions/use, /gameplay/actions/hack"
              titleTypographyProps={{ fontSize: '1rem', fontWeight: 600 }}
              subheaderTypographyProps={{ fontSize: '0.75rem' }}
            />
            <CardContent>
              <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
                <Button
                  variant="outlined"
                  size="small"
                  onClick={() => setPromptMode('use')}
                  disabled={useObjectMutation.isPending || isLoading}
                  sx={{ fontSize: '0.8rem' }}
                >
                  {useObjectMutation.isPending ? 'Выполнение...' : 'Использовать объект'}
                </Button>
                <Button
                  variant="outlined"
                  size="small"
                  onClick={() => setPromptMode('hack')}
                  disabled={hackSystemMutation.isPending || isLoading}
                  sx={{ fontSize: '0.8rem' }}
                >
                  {hackSystemMutation.isPending ? 'Взлом...' : 'Взломать систему'}
                </Button>
              </Stack>
            </CardContent>
          </Card>
        </Box>
      </GameLayout>

      <ActionPromptDialog
        open={promptMode === 'use'}
        mode="use"
        defaultLocationId={locationId}
        isLoading={useObjectMutation.isPending}
        onClose={() => setPromptMode(null)}
        onSubmit={handleUseSubmit}
      />

      <ActionPromptDialog
        open={promptMode === 'hack'}
        mode="hack"
        isLoading={hackSystemMutation.isPending}
        onClose={() => setPromptMode(null)}
        onSubmit={handleHackSubmit}
      />

      <ActionResultDialog
        open={dialogState.open}
        onClose={() => setDialogState((previous) => ({ ...previous, open: false }))}
        title={dialogState.title}
        success={dialogState.success}
        result={dialogState.result}
      />
    </Box>
  )
}

