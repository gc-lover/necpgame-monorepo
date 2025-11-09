import { useMemo, useState } from 'react'
import {
  Alert,
  Button,
  Card,
  CardContent,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import {
  useCreateGuild,
  useInviteToGuild,
  useJoinGuild,
  useLeaveGuild,
  useGetGuild,
} from '@/api/generated/social/guild-system/guilds/guilds'
import type {
  CreateGuildBody,
  InviteToGuildBody,
  JoinGuildBody,
  LeaveGuildBody,
} from '@/api/generated/social/guild-system/models'

export function GuildManagementPanel() {
  const [createPayload, setCreatePayload] = useState<CreateGuildBody>({
    founder_character_id: '',
    name: '',
    tag: '',
  })
  const [guildId, setGuildId] = useState('')
  const [invitePayload, setInvitePayload] = useState<InviteToGuildBody>({
    guild_id: '',
    inviter_id: '',
    invitee_id: '',
  })
  const [joinPayload, setJoinPayload] = useState<JoinGuildBody>({
    guild_id: '',
    character_id: '',
  })
  const [leavePayload, setLeavePayload] = useState<LeaveGuildBody>({
    guild_id: '',
    character_id: '',
  })

  const { mutate: createGuild, isPending: isCreating } = useCreateGuild()
  const { mutate: inviteToGuild, isPending: isInviting } = useInviteToGuild()
  const { mutate: joinGuild, isPending: isJoining } = useJoinGuild()
  const { mutate: leaveGuild, isPending: isLeaving } = useLeaveGuild()

  const guildQuery = useGetGuild(guildId || '', {
    query: { enabled: Boolean(guildId) },
  })

  const guild = guildQuery.data?.data
  const members = useMemo(() => guild?.members ?? [], [guild])

  const handleCreate = () => {
    createGuild(
      { data: createPayload },
      {
        onSuccess: (response) => {
          const newGuildId = response.data.guildId ?? response.data.id ?? ''
          setGuildId(newGuildId)
          setInvitePayload((prev) => ({ ...prev, guild_id: newGuildId }))
          setJoinPayload((prev) => ({ ...prev, guild_id: newGuildId }))
          setLeavePayload((prev) => ({ ...prev, guild_id: newGuildId }))
        },
      }
    )
  }

  const handleInvite = () => {
    inviteToGuild({ data: invitePayload })
  }

  const handleJoin = () => {
    joinGuild(
      { data: joinPayload },
      {
        onSuccess: () => guildQuery.refetch(),
      }
    )
  }

  const handleLeave = () => {
    leaveGuild(
      { data: leavePayload },
      {
        onSuccess: () => guildQuery.refetch(),
      }
    )
  }

  return (
    <Stack spacing={3}>
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Создание гильдии</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="Основатель"
                value={createPayload.founder_character_id}
                onChange={(event) =>
                  setCreatePayload((prev) => ({
                    ...prev,
                    founder_character_id: event.target.value,
                  }))
                }
                size="small"
              />
              <TextField
                label="Название"
                value={createPayload.name}
                onChange={(event) =>
                  setCreatePayload((prev) => ({ ...prev, name: event.target.value }))
                }
                size="small"
              />
              <TextField
                label="Тег"
                value={createPayload.tag}
                onChange={(event) =>
                  setCreatePayload((prev) => ({ ...prev, tag: event.target.value.toUpperCase() }))
                }
                size="small"
                inputProps={{ maxLength: 4 }}
              />
              <Button variant="contained" onClick={handleCreate} disabled={isCreating}>
                Создать гильдию
              </Button>
            </Stack>
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Приглашения</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="guild_id"
                value={invitePayload.guild_id}
                onChange={(event) =>
                  setInvitePayload((prev) => ({ ...prev, guild_id: event.target.value }))
                }
                size="small"
              />
              <TextField
                label="inviter_id"
                value={invitePayload.inviter_id}
                onChange={(event) =>
                  setInvitePayload((prev) => ({ ...prev, inviter_id: event.target.value }))
                }
                size="small"
              />
              <TextField
                label="invitee_id"
                value={invitePayload.invitee_id}
                onChange={(event) =>
                  setInvitePayload((prev) => ({ ...prev, invitee_id: event.target.value }))
                }
                size="small"
              />
              <Button variant="outlined" onClick={handleInvite} disabled={isInviting}>
                Пригласить
              </Button>
            </Stack>
          </Stack>
        </CardContent>
      </Card>
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Участие в гильдии</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="guild_id"
                value={joinPayload.guild_id}
                onChange={(event) =>
                  setJoinPayload((prev) => ({ ...prev, guild_id: event.target.value }))
                }
                size="small"
              />
              <TextField
                label="character_id"
                value={joinPayload.character_id}
                onChange={(event) =>
                  setJoinPayload((prev) => ({ ...prev, character_id: event.target.value }))
                }
                size="small"
              />
              <Button variant="contained" onClick={handleJoin} disabled={isJoining}>
                Вступить
              </Button>
            </Stack>

            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="guild_id"
                value={leavePayload.guild_id}
                onChange={(event) =>
                  setLeavePayload((prev) => ({ ...prev, guild_id: event.target.value }))
                }
                size="small"
              />
              <TextField
                label="character_id"
                value={leavePayload.character_id}
                onChange={(event) =>
                  setLeavePayload((prev) => ({ ...prev, character_id: event.target.value }))
                }
                size="small"
              />
              <Button variant="outlined" color="warning" onClick={handleLeave} disabled={isLeaving}>
                Покинуть
              </Button>
            </Stack>
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Состав и ранги</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="guild_id"
                value={guildId}
                onChange={(event) => setGuildId(event.target.value)}
                size="small"
              />
              <Button variant="outlined" onClick={() => guildQuery.refetch()}>
                Обновить
              </Button>
            </Stack>

            {guildQuery.isError && (
              <Alert severity="error">Не удалось получить информацию о гильдии.</Alert>
            )}

            {guild ? (
              <Stack spacing={1}>
                <Alert severity="info">
                  {guild.name} [{guild.tag}] — уровень {guild.level}. Лидер: {guild.leaderName ?? guild.leaderId}
                </Alert>
                {members.length ? (
                  members.map((member) => (
                    <Alert
                      key={member.characterId}
                      severity={member.rank === 'leader' ? 'success' : 'info'}
                    >
                      {member.characterName ?? member.characterId} — ранг: {member.rank}, вклад:{' '}
                      {member.contribution ?? 0}
                    </Alert>
                  ))
                ) : (
                  <Typography variant="body2" color="text.secondary">
                    У гильдии пока нет участников. Используй инвайты или join API.
                  </Typography>
                )}
              </Stack>
            ) : (
              <Typography variant="body2" color="text.secondary">
                Укажи guild_id, чтобы увидеть детали, банк и прогрессию.
              </Typography>
            )}
          </Stack>
        </CardContent>
      </Card>

      <Typography variant="caption" color="text.secondary">
        Гильдейские войны и банк отображаются как заглушки до готовности backend.
      </Typography>
    </Stack>
  )
}

