import { Card, CardContent, Chip, Divider, Stack, Typography } from '@mui/material'
import type { CombatSession } from '@/api/generated/gameplay/combat-session/models'
import { format } from 'date-fns'

interface SessionStatsOverviewProps {
  session?: CombatSession
}

const safeFormat = (value?: string | null) => {
  if (!value) {
    return '—'
  }

  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }

  return format(date, 'dd.MM.yyyy HH:mm')
}

export const SessionStatsOverview = ({ session }: SessionStatsOverviewProps) => {
  if (!session) {
    return (
      <Card variant="outlined">
        <CardContent>
          <Typography variant="body2" color="text.secondary">
            Сессия ещё не выбрана
          </Typography>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card variant="outlined">
      <CardContent>
        <Stack spacing={1.5}>
          <Stack direction="row" spacing={1} alignItems="center" justifyContent="space-between">
            <Typography variant="h6" fontSize="1rem" fontWeight={600}>
              Состояние сессии
            </Typography>
            <Chip label={session.status ?? 'unknown'} color="primary" size="small" />
          </Stack>

          <Divider />

          <Stack spacing={1}>
            <Stack direction="row" spacing={1} justifyContent="space-between">
              <Typography variant="caption" color="text.secondary">
                Тип боя
              </Typography>
              <Typography variant="body2">{session.combat_type ?? '—'}</Typography>
            </Stack>
            <Stack direction="row" spacing={1} justifyContent="space-between">
              <Typography variant="caption" color="text.secondary">
                Текущий ход
              </Typography>
              <Typography variant="body2">
                {session.current_turn != null ? session.current_turn : '—'}
              </Typography>
            </Stack>
            <Stack direction="row" spacing={1} justifyContent="space-between">
              <Typography variant="caption" color="text.secondary">
                Активный участник
              </Typography>
              <Typography variant="body2">{session.active_participant_id ?? '—'}</Typography>
            </Stack>
            <Stack direction="row" spacing={1} justifyContent="space-between">
              <Typography variant="caption" color="text.secondary">
                Начало
              </Typography>
              <Typography variant="body2">{safeFormat(session.started_at)}</Typography>
            </Stack>
            <Stack direction="row" spacing={1} justifyContent="space-between">
              <Typography variant="caption" color="text.secondary">
                Завершение
              </Typography>
              <Typography variant="body2">{safeFormat(session.ended_at)}</Typography>
            </Stack>
            <Stack direction="row" spacing={1} justifyContent="space-between">
              <Typography variant="caption" color="text.secondary">
                Длительность (сек)
              </Typography>
              <Typography variant="body2">
                {session.duration_seconds != null ? session.duration_seconds : '—'}
              </Typography>
            </Stack>
            <Stack direction="row" spacing={1} justifyContent="space-between">
              <Typography variant="caption" color="text.secondary">
                Команда-победитель
              </Typography>
              <Typography variant="body2">{session.winner_team ?? '—'}</Typography>
            </Stack>
          </Stack>
        </Stack>
      </CardContent>
    </Card>
  )
}


