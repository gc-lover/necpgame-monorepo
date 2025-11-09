import { useMemo, useState } from 'react'
import {
  Alert,
  Button,
  Card,
  CardContent,
  Divider,
  MenuItem,
  Stack,
  TextField,
  Typography,
} from '@mui/material'
import {
  CreatePartyBodyLootMode,
  type CreatePartyBody,
  type InviteToPartyBody,
  type JoinPartyBody,
  type LeavePartyBody,
} from '@/api/generated/social/party-system/models'
import {
  useCreateParty,
  useInviteToParty,
  useJoinParty,
  useLeaveParty,
  useGetParty,
} from '@/api/generated/social/party-system/party/party'

const lootModes = Object.values(CreatePartyBodyLootMode)

export function PartyManagementPanel() {
  const [createdPartyId, setCreatedPartyId] = useState<string | null>(null)
  const [leaderId, setLeaderId] = useState('')
  const [lootMode, setLootMode] = useState<CreatePartyBody['loot_mode']>('personal')
  const [maxMembers, setMaxMembers] = useState<number>(4)

  const [partyIdForInvite, setPartyIdForInvite] = useState('')
  const [inviterId, setInviterId] = useState('')
  const [inviteeId, setInviteeId] = useState('')

  const [activePartyId, setActivePartyId] = useState('')
  const [joinCharacterId, setJoinCharacterId] = useState('')
  const [leaveCharacterId, setLeaveCharacterId] = useState('')

  const { mutate: createParty, isPending: isCreating, reset: resetCreate } = useCreateParty()
  const { mutate: inviteToParty, isPending: isInviting } = useInviteToParty()
  const { mutate: joinParty, isPending: isJoining } = useJoinParty()
  const { mutate: leaveParty, isPending: isLeaving } = useLeaveParty()

  const partyDetailsQuery = useGetParty(activePartyId || '', {
    query: { enabled: Boolean(activePartyId) },
  })

  const handleCreate = () => {
    const payload: CreatePartyBody = {
      leader_character_id: leaderId,
      loot_mode: lootMode,
      max_members: Math.min(5, Math.max(2, maxMembers)),
    }
    createParty(
      { data: payload },
      {
        onSuccess: (response) => {
          setCreatedPartyId(response.data.partyId ?? response.data.id ?? 'unknown')
          setActivePartyId(response.data.partyId ?? response.data.id ?? '')
        },
      }
    )
  }

  const handleInvite = () => {
    const payload: InviteToPartyBody = {
      inviter_id: inviterId,
      invitee_id: inviteeId,
    }
    inviteToParty({ partyId: partyIdForInvite, data: payload })
  }

  const handleJoin = () => {
    const payload: JoinPartyBody = {
      character_id: joinCharacterId,
    }
    joinParty(
      { partyId: activePartyId, data: payload },
      {
        onSuccess: () => {
          partyDetailsQuery.refetch()
        },
      }
    )
  }

  const handleLeave = () => {
    const payload: LeavePartyBody = {
      character_id: leaveCharacterId,
    }
    leaveParty(
      { partyId: activePartyId, data: payload },
      {
        onSuccess: () => {
          partyDetailsQuery.refetch()
        },
      }
    )
  }

  const party = partyDetailsQuery.data?.data
  const members = useMemo(() => party?.memberDetails ?? [], [party])

  return (
    <Stack spacing={3}>
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Создание группы</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="Лидер (character_id)"
                value={leaderId}
                onChange={(event) => setLeaderId(event.target.value)}
                size="small"
                fullWidth
              />
              <TextField
                label="Макс. участников"
                type="number"
                value={maxMembers}
                onChange={(event) => setMaxMembers(Number(event.target.value))}
                size="small"
              />
              <TextField
                label="Режим лута"
                select
                value={lootMode}
                size="small"
                onChange={(event) =>
                  setLootMode(event.target.value as CreatePartyBody['loot_mode'])
                }
              >
                {lootModes.map((mode) => (
                  <MenuItem key={mode} value={mode}>
                    {mode}
                  </MenuItem>
                ))}
              </TextField>
              <Button variant="contained" onClick={handleCreate} disabled={isCreating}>
                Создать
              </Button>
            </Stack>
            {createdPartyId && (
              <Alert
                severity="success"
                onClose={() => {
                  setCreatedPartyId(null)
                  resetCreate()
                }}
              >
                Группа создана: {createdPartyId}. Используй ID для приглашений, присоединения и
                управления.
              </Alert>
            )}
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Приглашения</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="ID группы"
                value={partyIdForInvite}
                onChange={(event) => setPartyIdForInvite(event.target.value)}
                size="small"
              />
              <TextField
                label="Инициатор (leader)"
                value={inviterId}
                onChange={(event) => setInviterId(event.target.value)}
                size="small"
              />
              <TextField
                label="Приглашаемый"
                value={inviteeId}
                onChange={(event) => setInviteeId(event.target.value)}
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
            <Typography variant="h6">Управление группой</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="Активная группа"
                value={activePartyId}
                onChange={(event) => setActivePartyId(event.target.value)}
                size="small"
              />
              <Button variant="outlined" onClick={() => partyDetailsQuery.refetch()}>
                Обновить состав
              </Button>
            </Stack>

            <Stack
              direction={{ xs: 'column', sm: 'row' }}
              spacing={2}
              divider={<Divider orientation="vertical" flexItem sx={{ display: { xs: 'none', sm: 'block' } }} />}
            >
              <Stack spacing={1} flex={1}>
                <Typography variant="subtitle1">Присоединение</Typography>
                <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1}>
                  <TextField
                    label="character_id"
                    value={joinCharacterId}
                    onChange={(event) => setJoinCharacterId(event.target.value)}
                    size="small"
                    fullWidth
                  />
                  <Button variant="contained" onClick={handleJoin} disabled={isJoining}>
                    Присоединиться
                  </Button>
                </Stack>
              </Stack>

              <Stack spacing={1} flex={1}>
                <Typography variant="subtitle1">Покинуть</Typography>
                <Stack direction={{ xs: 'column', sm: 'row' }} spacing={1}>
                  <TextField
                    label="character_id"
                    value={leaveCharacterId}
                    onChange={(event) => setLeaveCharacterId(event.target.value)}
                    size="small"
                    fullWidth
                  />
                  <Button variant="outlined" onClick={handleLeave} disabled={isLeaving}>
                    Выйти из группы
                  </Button>
                </Stack>
              </Stack>
            </Stack>

            {partyDetailsQuery.isError && (
              <Alert severity="error">Не удалось загрузить детали группы.</Alert>
            )}

            {party && (
              <Stack spacing={1}>
                <Typography variant="subtitle1">Состав группы</Typography>
                <Typography variant="body2" color="text.secondary">
                  Лидер: {party.leaderId}. Режим лута: {party.lootMode}. Max участников:{' '}
                  {party.maxMembers}
                </Typography>
                {members.length ? (
                  members.map((member) => (
                    <Alert key={member.characterId} severity={member.role === 'leader' ? 'success' : 'info'}>
                      {member.characterName ?? member.characterId} — роль: {member.role}, статус:{' '}
                      {member.status}
                    </Alert>
                  ))
                ) : (
                  <Typography variant="body2" color="text.secondary">
                    Пока нет участников. Пригласи игроков или присоединись вручную.
                  </Typography>
                )}
              </Stack>
            )}
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )
}


