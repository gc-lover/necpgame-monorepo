import { useMemo, useState } from 'react'
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
  useGetGlobalLeaderboard,
  useGetPlayerGlobalRank,
  useUpdateLeaderboardScore,
} from '@/api/generated/progression/leaderboard-system/global-leaderboards/global-leaderboards'
import { useGetFriendLeaderboard } from '@/api/generated/progression/leaderboard-system/friend-leaderboards/friend-leaderboards'
import {
  useGetGuildLeaderboard,
  useGetGuildRank,
} from '@/api/generated/progression/leaderboard-system/guild-leaderboards/guild-leaderboards'
import { useGetSeasonalLeaderboard } from '@/api/generated/progression/leaderboard-system/seasonal-leaderboards/seasonal-leaderboards'
import type {
  GetGlobalLeaderboardParams,
  GetSeasonalLeaderboardParams,
  UpdateScoreRequest,
} from '@/api/generated/progression/leaderboard-system/models'
import { UpdateScoreRequestCategory } from '@/api/generated/progression/leaderboard-system/models'

const globalCategories = [
  'LEVEL',
  'WEALTH',
  'PVP_RATING',
  'ACHIEVEMENTS',
  'COMBAT_KILLS',
  'RAID_CLEARS',
] as const

const friendCategories = ['LEVEL', 'WEALTH', 'PVP_RATING', 'ACHIEVEMENTS'] as const

const guildCategories = ['LEVEL', 'WEALTH', 'MEMBERS', 'PVP_RATING', 'RAID_CLEARS', 'TERRITORY'] as const

const seasonalCategories = ['LEVEL', 'PVP_RATING', 'RAID_PROGRESS', 'ACHIEVEMENTS'] as const

const updateCategories = Object.values(UpdateScoreRequestCategory)

function renderLeaderboardEntries(
  title: string,
  entries: Array<{ position: number; name?: string | null; value?: number | null; guildName?: string | null }>
) {
  return (
    <Card variant="outlined">
      <CardContent>
        <Stack spacing={1}>
          <Typography variant="h6">{title}</Typography>
          {entries.length ? (
            entries.map((entry) => (
              <Alert key={`${title}-${entry.position}-${entry.name ?? entry.guildName}`} severity={entry.position <= 3 ? 'success' : 'info'}>
                #{entry.position} · {entry.name ?? entry.guildName ?? '—'} · {entry.value ?? 0}
              </Alert>
            ))
          ) : (
            <Typography variant="body2" color="text.secondary">
              Нет записей для выбранных параметров.
            </Typography>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

export function LeaderboardDashboardPanel() {
  const [globalCategory, setGlobalCategory] = useState<(typeof globalCategories)[number]>('LEVEL')
  const [top, setTop] = useState(20)
  const [offset, setOffset] = useState(0)
  const globalParams: GetGlobalLeaderboardParams = { top, offset }

  const globalQuery = useGetGlobalLeaderboard(globalCategory, globalParams, {})

  const [rankPlayerId, setRankPlayerId] = useState('')
  const playerRankQuery = useGetPlayerGlobalRank(globalCategory, rankPlayerId || '', {
    query: { enabled: Boolean(rankPlayerId) },
  })

  const [friendPlayerId, setFriendPlayerId] = useState('')
  const [friendCategory, setFriendCategory] = useState<(typeof friendCategories)[number]>('LEVEL')
  const friendQuery = useGetFriendLeaderboard(friendPlayerId || '', friendCategory, {
    query: { enabled: Boolean(friendPlayerId) },
  })

  const [guildCategory, setGuildCategory] = useState<(typeof guildCategories)[number]>('LEVEL')
  const [guildTop, setGuildTop] = useState(20)
  const guildQuery = useGetGuildLeaderboard(guildCategory, { top: guildTop }, {})

  const [guildId, setGuildId] = useState('')
  const guildRankQuery = useGetGuildRank(guildCategory, guildId || '', {
    query: { enabled: Boolean(guildId) },
  })

  const [seasonId, setSeasonId] = useState('')
  const [seasonCategory, setSeasonCategory] = useState<(typeof seasonalCategories)[number]>('LEVEL')
  const [seasonLimit, setSeasonLimit] = useState(20)
  const seasonalParams: GetSeasonalLeaderboardParams = { top: seasonLimit }
  const seasonalQuery = useGetSeasonalLeaderboard(seasonId || '', seasonCategory, seasonalParams, {
    query: { enabled: Boolean(seasonId) },
  })

  const { mutate: updateScore, isPending: isUpdating } = useUpdateLeaderboardScore()
  const [scorePayload, setScorePayload] = useState<UpdateScoreRequest>({
    player_id: '',
    category: UpdateScoreRequestCategory.LEVEL,
    score: 0,
  })

  const globalEntries = useMemo(
    () =>
      globalQuery.data?.data.entries?.map((entry) => ({
        position: entry.position ?? 0,
        name: entry.playerName ?? entry.playerId,
        value: entry.score,
      })) ?? [],
    [globalQuery.data]
  )

  const friendEntries = useMemo(
    () =>
      friendQuery.data?.data.entries?.map((entry) => ({
        position: entry.position ?? 0,
        name: entry.playerName ?? entry.playerId,
        value: entry.score,
      })) ?? [],
    [friendQuery.data]
  )

  const guildEntries = useMemo(
    () =>
      guildQuery.data?.data.entries?.map((entry) => ({
        position: entry.position ?? 0,
        name: entry.guildName ?? entry.guildId,
        value: entry.score,
        guildName: entry.guildName ?? entry.guildId,
      })) ?? [],
    [guildQuery.data]
  )

  const seasonalEntries = useMemo(
    () =>
      seasonalQuery.data?.data.entries?.map((entry) => ({
        position: entry.position ?? 0,
        name: entry.playerName ?? entry.playerId,
        value: entry.score,
      })) ?? [],
    [seasonalQuery.data]
  )

  const handleUpdateScore = () => {
    updateScore(
      { data: scorePayload },
      {
        onSuccess: () => {
          globalQuery.refetch()
          friendQuery.refetch()
          seasonalQuery.refetch()
        },
      }
    )
  }

  return (
    <Stack spacing={4}>
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Глобальные рейтинги</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <FormControl size="small" sx={{ minWidth: 200 }}>
                <InputLabel id="global-category">Категория</InputLabel>
                <Select
                  labelId="global-category"
                  label="Категория"
                  value={globalCategory}
                  onChange={(event) => setGlobalCategory(event.target.value as (typeof globalCategories)[number])}
                >
                  {globalCategories.map((value) => (
                    <MenuItem key={value} value={value}>
                      {value}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField
                label="top"
                type="number"
                size="small"
                value={top}
                onChange={(event) => setTop(Number(event.target.value))}
              />
              <TextField
                label="offset"
                type="number"
                size="small"
                value={offset}
                onChange={(event) => setOffset(Number(event.target.value))}
              />
            </Stack>
            {globalQuery.isError && <Alert severity="error">Не удалось загрузить глобальный рейтинг.</Alert>}
            {renderLeaderboardEntries('Топ глобального рейтинга', globalEntries)}
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="player_id"
                value={rankPlayerId}
                onChange={(event) => setRankPlayerId(event.target.value)}
                size="small"
              />
              {playerRankQuery.isError && (
                <Alert severity="error" sx={{ flexGrow: 1 }}>
                  Не удалось найти позицию игрока.
                </Alert>
              )}
              {playerRankQuery.data && (
                <Alert severity="info" sx={{ flexGrow: 1 }}>
                  {playerRankQuery.data.data.playerName ?? playerRankQuery.data.data.playerId} — место{' '}
                  {playerRankQuery.data.data.position}, очки {playerRankQuery.data.data.score}
                </Alert>
              )}
            </Stack>
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Рейтинг друзей</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="player_id"
                value={friendPlayerId}
                onChange={(event) => setFriendPlayerId(event.target.value)}
                size="small"
              />
              <FormControl size="small" sx={{ minWidth: 200 }}>
                <InputLabel id="friend-category">Категория</InputLabel>
                <Select
                  labelId="friend-category"
                  label="Категория"
                  value={friendCategory}
                  onChange={(event) => setFriendCategory(event.target.value as (typeof friendCategories)[number])}
                >
                  {friendCategories.map((value) => (
                    <MenuItem key={value} value={value}>
                      {value}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Stack>
            {friendQuery.isError && (
              <Alert severity="warning">Не удалось загрузить рейтинг друзей.</Alert>
            )}
            {renderLeaderboardEntries('Друзья', friendEntries)}
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Гильдейские рейтинги</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <FormControl size="small" sx={{ minWidth: 200 }}>
                <InputLabel id="guild-category">Категория</InputLabel>
                <Select
                  labelId="guild-category"
                  label="Категория"
                  value={guildCategory}
                  onChange={(event) => setGuildCategory(event.target.value as (typeof guildCategories)[number])}
                >
                  {guildCategories.map((value) => (
                    <MenuItem key={value} value={value}>
                      {value}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField
                label="top"
                type="number"
                size="small"
                value={guildTop}
                onChange={(event) => setGuildTop(Number(event.target.value))}
              />
            </Stack>
            {guildQuery.isError && (
              <Alert severity="error">Не удалось получить рейтинг гильдий.</Alert>
            )}
            {renderLeaderboardEntries('Гильдии', guildEntries)}
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="guild_id"
                value={guildId}
                onChange={(event) => setGuildId(event.target.value)}
                size="small"
              />
              {guildRankQuery.isError && <Alert severity="warning">Гильдия не найдена.</Alert>}
              {guildRankQuery.data && (
                <Alert severity="info" sx={{ flexGrow: 1 }}>
                  {guildRankQuery.data.data.guildName ?? guildRankQuery.data.data.guildId} — место{' '}
                  {guildRankQuery.data.data.position}, очки {guildRankQuery.data.data.score}
                </Alert>
              )}
            </Stack>
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Сезонные рейтинги</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="season_id"
                value={seasonId}
                onChange={(event) => setSeasonId(event.target.value)}
                size="small"
              />
              <FormControl size="small" sx={{ minWidth: 200 }}>
                <InputLabel id="season-category">Категория</InputLabel>
                <Select
                  labelId="season-category"
                  label="Категория"
                  value={seasonCategory}
                  onChange={(event) => setSeasonCategory(event.target.value as (typeof seasonalCategories)[number])}
                >
                  {seasonalCategories.map((value) => (
                    <MenuItem key={value} value={value}>
                      {value}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField
                label="top"
                type="number"
                size="small"
                value={seasonLimit}
                onChange={(event) => setSeasonLimit(Number(event.target.value))}
              />
            </Stack>
            {seasonalQuery.isError && (
              <Alert severity="warning">Не удалось получить сезонный рейтинг.</Alert>
            )}
            {renderLeaderboardEntries('Сезонный рейтинг', seasonalEntries)}
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            <Typography variant="h6">Администрирование рейтингов</Typography>
            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
              <TextField
                label="player_id"
                value={scorePayload.player_id}
                onChange={(event) =>
                  setScorePayload((prev) => ({ ...prev, player_id: event.target.value }))
                }
                size="small"
              />
              <FormControl size="small" sx={{ minWidth: 200 }}>
                <InputLabel id="score-category">Категория</InputLabel>
                <Select
                  labelId="score-category"
                  label="Категория"
                  value={scorePayload.category}
                  onChange={(event) =>
                    setScorePayload((prev) => ({
                      ...prev,
                      category: event.target.value as UpdateScoreRequest['category'],
                    }))
                  }
                >
                  {updateCategories.map((value) => (
                    <MenuItem key={value} value={value}>
                      {value}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField
                label="score"
                type="number"
                size="small"
                value={scorePayload.score}
                onChange={(event) =>
                  setScorePayload((prev) => ({ ...prev, score: Number(event.target.value) }))
                }
              />
              <TextField
                label="season_id (optional)"
                value={scorePayload.season_id ?? ''}
                onChange={(event) =>
                  setScorePayload((prev) => ({ ...prev, season_id: event.target.value || undefined }))
                }
                size="small"
              />
              <Button variant="contained" onClick={handleUpdateScore} disabled={isUpdating}>
                Обновить очки
              </Button>
            </Stack>
            <Alert severity="info">
              Используй форму для корректировки очков в QA/Dev среде. В бою изменения поступают от игровых сервисов.
            </Alert>
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )
}


