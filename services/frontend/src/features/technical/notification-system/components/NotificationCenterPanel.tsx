import { useMemo, useState } from 'react'
import {
  Alert,
  Button,
  Card,
  CardContent,
  Chip,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  Switch,
  TextField,
  Typography,
} from '@mui/material'
import {
  useGetNotifications,
  useSendNotification,
  useMarkNotificationRead,
  useMarkAllNotificationsRead,
} from '@/api/generated/technical/notification-system/notifications/notifications'
import {
  SendNotificationBodyType,
  SendNotificationBodyPriority,
  type SendNotificationBody,
  type GetNotificationsType,
  type MarkAllNotificationsReadBody,
} from '@/api/generated/technical/notification-system/models'

const notificationTypes = Object.values(SendNotificationBodyType)
const priorities = Object.values(SendNotificationBodyPriority)

const priorityToSeverity: Record<string, 'info' | 'warning' | 'error' | 'success'> = {
  low: 'info',
  normal: 'success',
  high: 'warning',
  urgent: 'error',
}

export function NotificationCenterPanel() {
  const [playerIdInput, setPlayerIdInput] = useState('')
  const [playerId, setPlayerId] = useState('')
  const [unreadOnly, setUnreadOnly] = useState(false)
  const [typeFilter, setTypeFilter] = useState<GetNotificationsType | ''>('')
  const [limit, setLimit] = useState(20)
  const [messagePayload, setMessagePayload] = useState<SendNotificationBody>({
    player_id: '',
    type: SendNotificationBodyType.system,
    priority: SendNotificationBodyPriority.normal,
    message: '',
    send_email: false,
  })

  const notificationsQuery = useGetNotifications(
    {
      player_id: playerId || 'placeholder',
      unread_only: unreadOnly || undefined,
      type: typeFilter || undefined,
      limit,
    },
    { query: { enabled: Boolean(playerId) } }
  )

  const { mutate: sendNotification, isPending: isSending } = useSendNotification()
  const { mutate: markNotificationRead, isPending: isMarking } = useMarkNotificationRead()
  const { mutate: markAllRead, isPending: isMarkingAll } = useMarkAllNotificationsRead()

  const notifications = useMemo(
    () => notificationsQuery.data?.data.items ?? [],
    [notificationsQuery.data]
  )

  const handleMarkAll = () => {
    if (!playerId) {
      return
    }
    const payload: MarkAllNotificationsReadBody = { player_id: playerId }
    markAllRead(
      { data: payload },
      {
        onSuccess: () => {
          notificationsQuery.refetch()
        },
      }
    )
  }

  const handleSendNotification = () => {
    sendNotification(
      {
        data: {
          ...messagePayload,
          player_id: messagePayload.player_id || playerId,
        },
      },
      {
        onSuccess: () => {
          setMessagePayload((prev) => ({ ...prev, message: '' }))
          notificationsQuery.refetch()
        },
      }
    )
  }

  return (
    <Stack spacing={3}>
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Фильтры</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} alignItems="center">
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
                Загрузить
              </Button>
              <FormControl size="small" sx={{ minWidth: 160 }}>
                <InputLabel id="notification-type">Тип</InputLabel>
                <Select
                  labelId="notification-type"
                  label="Тип"
                  value={typeFilter}
                  onChange={(event) => setTypeFilter(event.target.value as GetNotificationsType | '')}
                >
                  <MenuItem value="">Все</MenuItem>
                  {notificationTypes.map((value) => (
                    <MenuItem key={value} value={value}>
                      {value}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField
                label="limit"
                type="number"
                size="small"
                value={limit}
                onChange={(event) => setLimit(Number(event.target.value))}
              />
              <Stack direction="row" spacing={1} alignItems="center">
                <Typography variant="body2">Только непрочитанные</Typography>
                <Switch
                  checked={unreadOnly}
                  onChange={(event) => setUnreadOnly(event.target.checked)}
                />
              </Stack>
            </Stack>
            {notificationsQuery.isError && (
              <Alert severity="error">Не удалось загрузить уведомления.</Alert>
            )}
            {playerId ? (
              <Stack spacing={2}>
                <Stack direction="row" spacing={1}>
                  <Button
                    variant="outlined"
                    onClick={() => notificationsQuery.refetch()}
                    disabled={notificationsQuery.isFetching}
                  >
                    Обновить
                  </Button>
                  <Button variant="contained" onClick={handleMarkAll} disabled={isMarkingAll}>
                    Пометить все прочитанными
                  </Button>
                  <Chip
                    label={`Всего: ${notificationsQuery.data?.data.total ?? 0}`}
                    color="primary"
                    variant="outlined"
                  />
                </Stack>
                {notifications.length ? (
                  notifications.map((item) => (
                    <Alert
                      key={item.notificationId}
                      severity={priorityToSeverity[item.priority ?? 'normal']}
                      action={
                        <Button
                          color="inherit"
                          size="small"
                          onClick={() =>
                            markNotificationRead(
                              { notificationId: item.notificationId },
                              { onSuccess: () => notificationsQuery.refetch() }
                            )
                          }
                          disabled={isMarking}
                        >
                          Прочитано
                        </Button>
                      }
                    >
                      <Stack spacing={0.5}>
                        <Typography variant="subtitle2">
                          {item.type} · {item.createdAt}
                        </Typography>
                        <Typography variant="body2">{item.message}</Typography>
                        <Typography variant="caption" color="text.secondary">
                          Статус: {item.read ? 'прочитано' : 'не прочитано'} · Приоритет:{' '}
                          {item.priority}
                        </Typography>
                      </Stack>
                    </Alert>
                  ))
                ) : (
                  <Typography variant="body2" color="text.secondary">
                    Уведомлений нет. Отправь тестовое уведомление ниже.
                  </Typography>
                )}
              </Stack>
            ) : (
              <Typography variant="body2" color="text.secondary">
                Укажи player_id, чтобы получить уведомления. Поддерживается фильтр по типу и пагинация.
              </Typography>
            )}
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Отправить уведомление</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="player_id"
                value={messagePayload.player_id}
                onChange={(event) =>
                  setMessagePayload((prev) => ({ ...prev, player_id: event.target.value }))
                }
                helperText="По умолчанию используется player_id из фильтра"
                size="small"
                fullWidth
              />
              <FormControl size="small" sx={{ minWidth: 140 }}>
                <InputLabel id="send-type">Тип</InputLabel>
                <Select
                  labelId="send-type"
                  label="Тип"
                  value={messagePayload.type}
                  onChange={(event) =>
                    setMessagePayload((prev) => ({
                      ...prev,
                      type: event.target.value as SendNotificationBodyType,
                    }))
                  }
                >
                  {notificationTypes.map((value) => (
                    <MenuItem key={value} value={value}>
                      {value}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <FormControl size="small" sx={{ minWidth: 140 }}>
                <InputLabel id="priority">Приоритет</InputLabel>
                <Select
                  labelId="priority"
                  label="Приоритет"
                  value={messagePayload.priority}
                  onChange={(event) =>
                    setMessagePayload((prev) => ({
                      ...prev,
                      priority: event.target.value as SendNotificationBodyPriority,
                    }))
                  }
                >
                  {priorities.map((value) => (
                    <MenuItem key={value} value={value}>
                      {value}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Stack>
            <TextField
              label="Сообщение"
              value={messagePayload.message}
              onChange={(event) =>
                setMessagePayload((prev) => ({ ...prev, message: event.target.value }))
              }
              multiline
              minRows={3}
              fullWidth
            />
            <Stack direction="row" spacing={1} alignItems="center">
              <Typography variant="body2">Отправить на email</Typography>
              <Switch
                checked={messagePayload.send_email ?? false}
                onChange={(event) =>
                  setMessagePayload((prev) => ({ ...prev, send_email: event.target.checked }))
                }
              />
            </Stack>
            <Button variant="contained" onClick={handleSendNotification} disabled={isSending}>
              Отправить уведомление
            </Button>
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )
}


