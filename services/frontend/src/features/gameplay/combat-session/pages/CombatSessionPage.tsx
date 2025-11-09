import { useMemo, useState } from 'react'
import {
  Alert,
  Box,
  Button,
  Divider,
  FormControlLabel,
  Grid,
  IconButton,
  MenuItem,
  Paper,
  Stack,
  Switch,
  TextField,
  Typography,
} from '@mui/material'
import AddIcon from '@mui/icons-material/Add'
import DeleteIcon from '@mui/icons-material/Delete'
import ReplayIcon from '@mui/icons-material/Replay'
import SportsKabaddiIcon from '@mui/icons-material/SportsKabaddi'
import type { ParticipantInitType } from '@/api/generated/gameplay/combat-session/models'
import {
  useCreateCombatSession,
  useGetCombatEvents,
  useGetCombatSession,
} from '@/api/generated/gameplay/combat-session/combat-sessions/combat-sessions'
import { useNextTurn } from '@/api/generated/gameplay/combat-session/combat-actions/combat-actions'
import { useEndCombatSession } from '@/api/generated/gameplay/combat-session/combat-results/combat-results'
import type { CreateCombatSessionRequestCombatType } from '@/api/generated/gameplay/combat-session/models'
import { GameLayout } from '@/shared/ui/layout'
import { SessionStatsOverview } from '../components/SessionStatsOverview'
import { ParticipantsGrid } from '../components/ParticipantsGrid'
import { EventTimeline } from '../components/EventTimeline'
import { DamageActionForm } from '../components/DamageActionForm'
import { useGameState } from '@/features/game/hooks/useGameState'
import type { CreateCombatSessionRequest } from '@/api/generated/gameplay/combat-session/models'

type ParticipantDraft = {
  id: string
  name: string
  team: string
  type: ParticipantInitType
}

const COMBAT_TYPES: CreateCombatSessionRequestCombatType[] = [
  'PVE',
  'PVP_DUEL',
  'PVP_ARENA',
  'RAID_BOSS',
  'EXTRACTION',
]

const PARTICIPANT_TYPES: ParticipantInitType[] = ['PLAYER', 'NPC', 'AI_ENEMY']

const createDefaultParticipants = (characterId?: string): ParticipantDraft[] => [
  {
    id: characterId ?? 'player-1',
    name: characterId ? 'Selected Character' : 'Игрок',
    team: 'A',
    type: 'PLAYER',
  },
  {
    id: 'enemy-1',
    name: 'Враг',
    team: 'B',
    type: 'AI_ENEMY',
  },
]

export const CombatSessionPage = () => {
  const { selectedCharacterId } = useGameState()
  const [sessionId, setSessionId] = useState('')
  const [combatType, setCombatType] = useState<CreateCombatSessionRequestCombatType>('PVE')
  const [autoRefresh, setAutoRefresh] = useState(true)
  const [participants, setParticipants] = useState<ParticipantDraft[]>(
    createDefaultParticipants(selectedCharacterId)
  )
  const [eventsSinceId, setEventsSinceId] = useState<number | undefined>(undefined)

  const createMutation = useCreateCombatSession()
  const sessionQuery = useGetCombatSession(sessionId, {
    query: {
      enabled: Boolean(sessionId),
      refetchInterval: autoRefresh ? 5000 : undefined,
    },
  })
  const eventsQuery = useGetCombatEvents(
    sessionId,
    eventsSinceId ? { since_event_id: eventsSinceId } : undefined,
    {
      query: {
        enabled: Boolean(sessionId),
        refetchInterval: autoRefresh ? 5000 : undefined,
      },
    }
  )
  const nextTurnMutation = useNextTurn()
  const endSessionMutation = useEndCombatSession()

  const sessionData = sessionQuery.data?.data
  const events = eventsQuery.data?.data.events ?? []

  const canCreateSession = useMemo(
    () => participants.length >= 2 && participants.every(item => item.id.trim().length > 0),
    [participants]
  )

  const handleAddParticipant = () => {
    setParticipants(prev => [
      ...prev,
      { id: `participant-${prev.length + 1}`, name: 'Участник', team: 'A', type: 'NPC' },
    ])
  }

  const handleRemoveParticipant = (index: number) => {
    setParticipants(prev => prev.filter((_, idx) => idx !== index))
  }

  const handleParticipantChange = (index: number, key: keyof ParticipantDraft, value: string) => {
    setParticipants(prev =>
      prev.map((participant, idx) =>
        idx === index ? { ...participant, [key]: value } : participant
      )
    )
  }

  const handleCreateSession = async () => {
    const payload: CreateCombatSessionRequest = {
      combat_type: combatType,
      participants: participants.map(participant => ({
        id: participant.id,
        type: participant.type,
        team: participant.team || 'A',
      })),
    }

    const result = await createMutation.mutateAsync({ data: payload })
    const newSessionId = result.data.id
    if (newSessionId) {
      setSessionId(newSessionId)
    }
  }

  const handleNextTurn = async () => {
    if (!sessionId) {
      return
    }
    await nextTurnMutation.mutateAsync({ session_id: sessionId })
    sessionQuery.refetch()
  }

  const handleEndSession = async (outcome: 'VICTORY' | 'DEFEAT' | 'DRAW' | 'TIMEOUT') => {
    if (!sessionId) {
      return
    }
    await endSessionMutation.mutateAsync({
      session_id: sessionId,
      data: { outcome },
    })
    sessionQuery.refetch()
  }

  const handleRefresh = () => {
    sessionQuery.refetch()
    eventsQuery.refetch()
  }

  return (
    <GameLayout
      leftPanel={
        <Stack spacing={2}>
          <Typography variant="h6" fontSize="1rem" fontWeight={600}>
            Управление сессией
          </Typography>

          <TextField
            label="Session ID"
            size="small"
            value={sessionId}
            onChange={event => setSessionId(event.target.value)}
            helperText="Введите ID существующей сессии либо создайте новую"
          />

          <Paper variant="outlined" sx={{ p: 1.5 }}>
            <Stack spacing={1.5}>
              <Typography variant="subtitle2" fontWeight={600}>
                Новая сессия
              </Typography>
              <TextField
                select
                label="Тип боя"
                size="small"
                value={combatType}
                onChange={event =>
                  setCombatType(event.target.value as CreateCombatSessionRequestCombatType)
                }
              >
                {COMBAT_TYPES.map(type => (
                  <MenuItem key={type} value={type}>
                    {type}
                  </MenuItem>
                ))}
              </TextField>

              <Stack spacing={1}>
                {participants.map((participant, index) => (
                  <Paper key={participant.id} variant="outlined" sx={{ p: 1 }}>
                    <Stack spacing={0.75}>
                      <Stack direction="row" justifyContent="space-between" alignItems="center">
                        <Typography variant="caption" color="text.secondary">
                          Участник #{index + 1}
                        </Typography>
                        {participants.length > 2 && (
                          <IconButton
                            size="small"
                            aria-label="Удалить участника"
                            onClick={() => handleRemoveParticipant(index)}
                          >
                            <DeleteIcon fontSize="small" />
                          </IconButton>
                        )}
                      </Stack>
                      <TextField
                        label="ID"
                        size="small"
                        value={participant.id}
                        onChange={event => handleParticipantChange(index, 'id', event.target.value)}
                      />
                      <TextField
                        label="Имя"
                        size="small"
                        value={participant.name}
                        onChange={event =>
                          handleParticipantChange(index, 'name', event.target.value)
                        }
                      />
                      <TextField
                        label="Команда"
                        size="small"
                        value={participant.team}
                        onChange={event =>
                          handleParticipantChange(index, 'team', event.target.value)
                        }
                      />
                      <TextField
                        select
                        label="Тип"
                        size="small"
                        value={participant.type}
                        onChange={event =>
                          handleParticipantChange(index, 'type', event.target.value as ParticipantInitType)
                        }
                      >
                        {PARTICIPANT_TYPES.map(type => (
                          <MenuItem key={type} value={type}>
                            {type}
                          </MenuItem>
                        ))}
                      </TextField>
                    </Stack>
                  </Paper>
                ))}
                <Button
                  startIcon={<AddIcon />}
                  variant="outlined"
                  size="small"
                  onClick={handleAddParticipant}
                >
                  Добавить участника
                </Button>
              </Stack>

              {createMutation.error && (
                <Alert severity="error">
                  {(createMutation.error as { message?: string })?.message ??
                    'Не удалось создать сессию'}
                </Alert>
              )}

              <Button
                variant="contained"
                size="small"
                onClick={handleCreateSession}
                disabled={!canCreateSession || createMutation.isPending}
              >
                Создать боевую сессию
              </Button>
            </Stack>
          </Paper>

          <Divider />

          <FormControlLabel
            control={
              <Switch
                checked={autoRefresh}
                onChange={event => setAutoRefresh(event.target.checked)}
                size="small"
              />
            }
            label="Автообновление"
          />

          <TextField
            label="since_event_id"
            size="small"
            type="number"
            value={eventsSinceId ?? ''}
            onChange={event =>
              setEventsSinceId(event.target.value ? Number(event.target.value) : undefined)
            }
            helperText="Загружать события после указанного ID"
          />

          <Button
            startIcon={<ReplayIcon />}
            variant="outlined"
            size="small"
            onClick={handleRefresh}
          >
            Обновить данные
          </Button>
        </Stack>
      }
      rightPanel={
        <Stack spacing={2}>
          <Typography variant="h6" fontSize="1rem" fontWeight={600}>
            Боевые действия
          </Typography>
          <DamageActionForm sessionId={sessionId} onApplied={handleRefresh} />
          <Paper variant="outlined" sx={{ p: 1.5 }}>
            <Stack spacing={1}>
              <Typography variant="subtitle2" fontWeight={600}>
                Управление ходами
              </Typography>
              {nextTurnMutation.error && (
                <Alert severity="error">Не удалось переключить ход</Alert>
              )}
              <Button
                startIcon={<SportsKabaddiIcon />}
                variant="outlined"
                size="small"
                disabled={!sessionId || nextTurnMutation.isPending}
                onClick={handleNextTurn}
              >
                Следующий ход
              </Button>
              <Divider />
              <Typography variant="subtitle2" fontWeight={600}>
                Завершить бой
              </Typography>
              <Stack direction="row" spacing={1}>
                {(['VICTORY', 'DEFEAT', 'DRAW', 'TIMEOUT'] as const).map(outcome => (
                  <Button
                    key={outcome}
                    variant="contained"
                    color="secondary"
                    size="small"
                    onClick={() => handleEndSession(outcome)}
                    disabled={!sessionId || endSessionMutation.isPending}
                  >
                    {outcome}
                  </Button>
                ))}
              </Stack>
            </Stack>
          </Paper>
        </Stack>
      }
    >
      <Stack spacing={2} sx={{ height: '100%' }}>
        <Stack direction="row" spacing={1} alignItems="center">
          <SportsKabaddiIcon fontSize="small" color="primary" />
          <Typography variant="h5" fontSize="1.25rem" fontWeight={600}>
            Боевая сессия
          </Typography>
        </Stack>

        {sessionQuery.isError && (
          <Alert severity="error">Не удалось загрузить данные сессии: отсутствует доступ</Alert>
        )}

        <SessionStatsOverview session={sessionData} />

        <ParticipantsGrid
          participants={sessionData?.participants}
          activeParticipantId={sessionData?.active_participant_id}
        />

        <Grid container spacing={2}>
          <Grid item xs={12} md={7}>
            <EventTimeline events={events} />
          </Grid>
          <Grid item xs={12} md={5}>
            <Paper variant="outlined" sx={{ p: 1.5 }}>
              <Typography variant="subtitle2" fontWeight={600} gutterBottom>
                Информация об ошибках
              </Typography>
              <Stack spacing={1}>
                {sessionQuery.isLoading && (
                  <Typography variant="body2" color="text.secondary">
                    Загружаем информацию о сессии...
                  </Typography>
                )}
                {eventsQuery.isLoading && (
                  <Typography variant="body2" color="text.secondary">
                    Загружаем события...
                  </Typography>
                )}
                <Typography variant="body2" color="text.secondary">
                  Активная сессия: {sessionId || 'не выбрана'}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  Получено событий: {events.length}
                </Typography>
              </Stack>
            </Paper>
          </Grid>
        </Grid>

        <Box flexGrow={1} />
      </Stack>
    </GameLayout>
  )
}

export default CombatSessionPage







