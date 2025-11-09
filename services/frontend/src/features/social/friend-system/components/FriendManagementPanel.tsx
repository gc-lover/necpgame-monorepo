import { useState } from 'react'
import {
  Alert,
  Button,
  Card,
  CardContent,
  Divider,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import {
  useGetFriends,
  useSendFriendRequest,
  useAcceptFriendRequest,
  useRemoveFriend,
  useBlockPlayer,
} from '@/api/generated/social/friend-system/friends/friends'
import type {
  SendFriendRequestBody,
  BlockPlayerBody,
} from '@/api/generated/social/friend-system/models'

export function FriendManagementPanel() {
  const [playerIdInput, setPlayerIdInput] = useState('')
  const [playerId, setPlayerId] = useState('')
  const friendsQuery = useGetFriends(
    { player_id: playerId || 'placeholder' },
    { query: { enabled: Boolean(playerId) } }
  )

  const { mutate: sendRequest, isPending: isSending } = useSendFriendRequest()
  const { mutate: acceptRequest, isPending: isAccepting } = useAcceptFriendRequest()
  const { mutate: removeFriend, isPending: isRemoving } = useRemoveFriend()
  const { mutate: blockPlayer, isPending: isBlocking } = useBlockPlayer()

  const [requestPayload, setRequestPayload] = useState<SendFriendRequestBody>({
    player_id: '',
    target_player_name: '',
  })
  const [acceptRequestId, setAcceptRequestId] = useState('')
  const [removeId, setRemoveId] = useState('')
  const [blockPayload, setBlockPayload] = useState<BlockPlayerBody>({
    player_id: '',
    target_player_id: '',
    reason: '',
  })

  const friendItems = friendsQuery.data?.data.friends ?? []

  return (
    <Stack spacing={3}>
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Список друзей</Typography>
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
                Загрузить
              </Button>
            </Stack>

            {friendsQuery.isError && (
              <Alert severity="error">Не удалось получить список друзей.</Alert>
            )}

            {playerId ? (
              friendItems.length ? (
                friendItems.map((friend) => (
                  <Alert key={friend.friendId} severity={friend.online ? 'success' : 'info'}>
                    {friend.friendName ?? friend.friendId} — статус: {friend.status}, онлайн:{' '}
                    {friend.online ? 'да' : 'нет'}
                  </Alert>
                ))
              ) : (
                <Typography variant="body2" color="text.secondary">
                  Пока нет друзей. Отправь запрос или ожидай принять.
                </Typography>
              )
            ) : (
              <Typography variant="body2" color="text.secondary">
                Укажи player_id, чтобы увидеть друзей и запросы.
              </Typography>
            )}
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Запрос в друзья</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="player_id"
                value={requestPayload.player_id}
                onChange={(event) =>
                  setRequestPayload((prev) => ({ ...prev, player_id: event.target.value }))
                }
                size="small"
              />
              <TextField
                label="target_player_name"
                value={requestPayload.target_player_name}
                onChange={(event) =>
                  setRequestPayload((prev) => ({
                    ...prev,
                    target_player_name: event.target.value,
                  }))
                }
                size="small"
              />
              <Button
                variant="outlined"
                onClick={() => sendRequest({ data: requestPayload })}
                disabled={isSending}
              >
                Отправить
              </Button>
            </Stack>
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Обработка запросов</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="request_id"
                value={acceptRequestId}
                onChange={(event) => setAcceptRequestId(event.target.value)}
                size="small"
              />
              <Button
                variant="contained"
                onClick={() => acceptRequest({ requestId: acceptRequestId })}
                disabled={isAccepting}
              >
                Принять запрос
              </Button>
            </Stack>

            <Divider />

            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="friendship_id"
                value={removeId}
                onChange={(event) => setRemoveId(event.target.value)}
                size="small"
              />
              <Button
                variant="outlined"
                color="warning"
                onClick={() => removeFriend({ friendshipId: removeId })}
                disabled={isRemoving}
              >
                Удалить из друзей
              </Button>
            </Stack>
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Блокировка игрока</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="player_id"
                value={blockPayload.player_id}
                onChange={(event) =>
                  setBlockPayload((prev) => ({ ...prev, player_id: event.target.value }))
                }
                size="small"
              />
              <TextField
                label="target_player_id"
                value={blockPayload.target_player_id}
                onChange={(event) =>
                  setBlockPayload((prev) => ({
                    ...prev,
                    target_player_id: event.target.value,
                  }))
                }
                size="small"
              />
              <TextField
                label="Причина"
                value={blockPayload.reason ?? ''}
                onChange={(event) =>
                  setBlockPayload((prev) => ({ ...prev, reason: event.target.value }))
                }
                size="small"
                fullWidth
              />
              <Button
                variant="outlined"
                color="error"
                onClick={() => blockPlayer({ data: blockPayload })}
                disabled={isBlocking}
              >
                Заблокировать
              </Button>
            </Stack>
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )
}


