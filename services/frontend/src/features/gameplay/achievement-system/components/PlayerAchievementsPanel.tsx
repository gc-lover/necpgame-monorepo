import { useState } from 'react'
import {
  Alert,
  Button,
  Card,
  CardContent,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import {
  useGetPlayerAchievements,
  useUpdateAchievementProgress,
  useGetPlayerTitles,
  useSetActiveTitle,
} from '@/api/generated/progression/achievement-system/achievement-progress/achievement-progress'
import type {
  UpdateProgressRequest,
  SetActiveTitleBody,
} from '@/api/generated/progression/achievement-system/models'

export function PlayerAchievementsPanel() {
  const [playerIdInput, setPlayerIdInput] = useState('')
  const [playerId, setPlayerId] = useState('')
  const [statusFilter, setStatusFilter] = useState<string>('')

  const playerAchievementsQuery = useGetPlayerAchievements(
    playerId || '',
    { status: statusFilter || undefined },
    { query: { enabled: Boolean(playerId) } }
  )

  const playerTitlesQuery = useGetPlayerTitles(playerId || '', {
    query: { enabled: Boolean(playerId) },
  })

  const { mutate: updateProgress, isPending: isUpdating } = useUpdateAchievementProgress()
  const { mutate: setActiveTitle, isPending: isSettingTitle } = useSetActiveTitle()

  const [progressPayload, setProgressPayload] = useState<UpdateProgressRequest>({
    achievement_id: '',
    event_type: 'manual_update',
    increment: 1,
  })
  const [activeTitleId, setActiveTitleId] = useState<string>('')

  const achievements = playerAchievementsQuery.data?.data.achievements ?? []
  const titles = playerTitlesQuery.data?.data.titles ?? []

  const handleProgressUpdate = () => {
    if (!playerId) {
      return
    }
    updateProgress(
      { playerId, data: progressPayload },
      {
        onSuccess: () => {
          playerAchievementsQuery.refetch()
        },
      }
    )
  }

  const handleSetTitle = () => {
    if (!playerId) {
      return
    }
    const payload: SetActiveTitleBody = {
      title_id: activeTitleId || undefined,
    }
    setActiveTitle(
      { playerId, data: payload },
      {
        onSuccess: () => {
          playerAchievementsQuery.refetch()
          playerTitlesQuery.refetch()
        },
      }
    )
  }

  return (
    <Stack spacing={3}>
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Игрок</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="player_id"
                value={playerIdInput}
                onChange={(event) => setPlayerIdInput(event.target.value)}
                size="small"
              />
              <Button
                variant="contained"
                onClick={() => setPlayerId(playerIdInput.trim())}
                disabled={!playerIdInput.trim()}
              >
                Загрузить достижения
              </Button>
              <FormControl size="small" sx={{ minWidth: 160 }}>
                <InputLabel id="status-filter">Статус</InputLabel>
                <Select
                  labelId="status-filter"
                  label="Статус"
                  value={statusFilter}
                  onChange={(event) => setStatusFilter(event.target.value)}
                >
                  <MenuItem value="">Все</MenuItem>
                  <MenuItem value="LOCKED">LOCKED</MenuItem>
                  <MenuItem value="IN_PROGRESS">IN_PROGRESS</MenuItem>
                  <MenuItem value="COMPLETED">COMPLETED</MenuItem>
                  <MenuItem value="CLAIMED">CLAIMED</MenuItem>
                </Select>
              </FormControl>
            </Stack>
            {playerAchievementsQuery.isError && (
              <Alert severity="error">Не удалось получить достижения игрока.</Alert>
            )}
          </Stack>
        </CardContent>
      </Card>

      {playerId ? (
        <>
          <Card variant="outlined">
            <CardContent>
              <Stack spacing={1}>
                <Typography variant="h6">Список достижений</Typography>
                {achievements.length ? (
                  achievements.map((item) => (
                    <Alert
                      key={item.achievementId}
                      severity={item.status === 'COMPLETED' ? 'success' : 'info'}
                    >
                      <Stack spacing={0.5}>
                        <Typography variant="subtitle2">
                          {item.name} · {item.status}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                          Прогресс: {item.progress.current}/{item.progress.required}
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                          Последнее обновление: {item.progress.updatedAt}
                        </Typography>
                      </Stack>
                    </Alert>
                  ))
                ) : (
                  <Typography variant="body2" color="text.secondary">
                    Достижения не найдены. Попробуй обновить прогресс вручную.
                  </Typography>
                )}
              </Stack>
            </CardContent>
          </Card>

          <Card variant="outlined">
            <CardContent>
              <Stack spacing={2}>
                <Typography variant="h6">Обновить прогресс</Typography>
                <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
                  <TextField
                    label="achievement_id"
                    value={progressPayload.achievement_id}
                    onChange={(event) =>
                      setProgressPayload((prev) => ({
                        ...prev,
                        achievement_id: event.target.value,
                      }))
                    }
                    size="small"
                  />
                  <TextField
                    label="event_type"
                    value={progressPayload.event_type}
                    onChange={(event) =>
                      setProgressPayload((prev) => ({
                        ...prev,
                        event_type: event.target.value,
                      }))
                    }
                    size="small"
                  />
                  <TextField
                    label="increment"
                    type="number"
                    value={progressPayload.increment ?? 1}
                    onChange={(event) =>
                      setProgressPayload((prev) => ({
                        ...prev,
                        increment: Number(event.target.value),
                      }))
                    }
                    size="small"
                  />
                  <Button variant="contained" onClick={handleProgressUpdate} disabled={isUpdating}>
                    Обновить
                  </Button>
                </Stack>
              </Stack>
            </CardContent>
          </Card>

          <Card variant="outlined">
            <CardContent>
              <Stack spacing={2}>
                <Typography variant="h6">Активный титул</Typography>
                <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
                  <FormControl size="small" sx={{ minWidth: 220 }}>
                    <InputLabel id="title-select">Титул</InputLabel>
                    <Select
                      labelId="title-select"
                      label="Титул"
                      value={activeTitleId}
                      onChange={(event) => setActiveTitleId(event.target.value)}
                    >
                      <MenuItem value="">Без титула</MenuItem>
                      {titles.map((title) => (
                        <MenuItem key={title.titleId} value={title.titleId}>
                          {title.name} · {title.rarity}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                  <Button variant="outlined" onClick={handleSetTitle} disabled={isSettingTitle}>
                    Применить
                  </Button>
                </Stack>
              </Stack>
            </CardContent>
          </Card>
        </>
      ) : (
        <Typography variant="body2" color="text.secondary">
          Укажи player_id, чтобы увидеть прогресс достижений и титулы.
        </Typography>
      )}
    </Stack>
  )
}


