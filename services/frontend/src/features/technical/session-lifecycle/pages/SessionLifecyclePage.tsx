import React, { useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Typography,
  Stack,
  Divider,
  Alert,
  Grid,
  Box,
  TextField,
  MenuItem,
  Chip,
  FormControlLabel,
  Switch,
} from '@mui/material'
import SensorsIcon from '@mui/icons-material/Sensors'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { SessionCard } from '../components/SessionCard'
import { HeartbeatMetricsCard } from '../components/HeartbeatMetricsCard'
import { AfkWarningCard } from '../components/AfkWarningCard'
import { ForceLogoutCard } from '../components/ForceLogoutCard'
import { SessionPoliciesCard } from '../components/SessionPoliciesCard'
import { SessionDiagnosticsCard } from '../components/SessionDiagnosticsCard'

const statuses = ['ACTIVE', 'AFK', 'DISCONNECTED', 'TERMINATED'] as const

const sessionList = [
  {
    sessionId: 'sess-7f3c',
    playerId: 'player-73f1',
    characterId: 'char-901',
    status: 'ACTIVE' as const,
    createdAt: '2025-11-08 04:10',
    lastHeartbeatAt: '2025-11-08 04:12',
    expiresAt: '2025-11-08 04:40',
  },
  {
    sessionId: 'sess-7f3d',
    playerId: 'player-99a1',
    characterId: 'char-510',
    status: 'AFK' as const,
    createdAt: '2025-11-08 03:58',
    lastHeartbeatAt: '2025-11-08 04:11',
    expiresAt: '2025-11-08 04:38',
  },
]

const heartbeatMetrics = [
  { timestamp: '04:10', latencyMs: 42, activity: 'active' as const },
  { timestamp: '04:11', latencyMs: 65, activity: 'active' as const, warning: 'LATE_HEARTBEAT' as const },
  { timestamp: '04:12', latencyMs: 37, activity: 'active' as const },
  { timestamp: '04:13', latencyMs: 55, activity: 'idle' as const },
]

const afkWarning = {
  triggeredAt: '2025-11-08 04:14',
  timeoutSeconds: 45,
  reason: 'No movement detected',
}

const forceLogoutPlan = {
  playerId: 'player-73f1',
  accountId: 'account-128',
  notify: true,
  scheduledFor: '2025-11-08 04:20',
}

const policies = [
  { name: 'Heartbeat interval', value: '30s ±10', description: 'Клиент должен отправлять heartbeat каждые 30 секунд' },
  { name: 'AFK timeout', value: '5m', description: 'Интервал до предупреждения об AFK' },
  { name: 'Session expiry', value: '40m', description: 'Сессия истекает без heartbeat' },
]

const diagnostics = [
  { label: 'Concurrent sessions', value: '1 of 1', status: 'ok' as const },
  { label: 'Heartbeat jitter', value: '±8s', status: 'warn' as const },
  { label: 'Anti-cheat status', value: 'verified', status: 'ok' as const },
  { label: 'Global state sync', value: 'delayed 3s', status: 'warn' as const },
]

export const SessionLifecyclePage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [statusFilter, setStatusFilter] = useState<(typeof statuses)[number]>('ACTIVE')
  const [autoWarn, setAutoWarn] = useState<boolean>(true)

  const visibleSessions = useMemo(() => sessionList.filter((session) => session.status === statusFilter), [statusFilter])

  const leftPanel = (
    <Stack spacing={2}>
      <CyberpunkButton
        variant='outlined'
        size='small'
        fullWidth
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/game')}
      >
        Назад
      </CyberpunkButton>
      {selectedCharacterId && (
        <Typography variant='caption' fontSize={cyberpunkTokens.fonts.xs} color='text.secondary'>
          Character: {selectedCharacterId}
        </Typography>
      )}
      <Typography variant='h6' fontSize={cyberpunkTokens.fonts.lg} fontWeight='bold' color='secondary.main'>
        Session Lifecycle
      </Typography>
      <Typography variant='caption' fontSize={cyberpunkTokens.fonts.xs} color='text.secondary'>
        Создание, heartbeat, AFK и force logout политики.
      </Typography>
      <Divider />
      <Typography variant='subtitle2' fontSize={cyberpunkTokens.fonts.md} fontWeight='bold'>
        Фильтры
      </Typography>
      <TextField
        select
        size='small'
        label='Status'
        value={statusFilter}
        onChange={(event) => setStatusFilter(event.target.value as typeof statuses[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {statuses.map((status) => (
          <MenuItem key={status} value={status} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {status}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size='small' checked={autoWarn} onChange={(event) => setAutoWarn(event.target.checked)} />}
        label={<Typography variant='caption' fontSize={cyberpunkTokens.fonts.xs}>Auto warn AFK players</Typography>}
      />
      <Divider />
      <Typography variant='subtitle2' fontSize={cyberpunkTokens.fonts.md} fontWeight='bold'>
        Быстрые действия
      </Typography>
      <CyberpunkButton variant='primary' size='small' fullWidth>
        Создать сессию
      </CyberpunkButton>
      <CyberpunkButton variant='secondary' size='small' fullWidth>
        Форсировать logout
      </CyberpunkButton>
      <CyberpunkButton variant='outlined' size='small' fullWidth>
        Открыть диагностику
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <SessionPoliciesCard policies={policies} />
      <SessionDiagnosticsCard diagnostics={diagnostics} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height='100%'>
      <Box display='flex' alignItems='center' gap={1}>
        <SensorsIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant='h5' fontSize={cyberpunkTokens.fonts.xl} fontWeight='bold'>
          Session Lifecycle Control
        </Typography>
        <Chip label={statusFilter} size='small' sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity='warning' sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Heartbeat SLA: 30s ±10, AFK предупреждение через 5 минут без активности. Force logout включён для concurrent login.
      </Alert>
      <Grid container spacing={1}>
        {visibleSessions.map((session) => (
          <Grid item xs={12} md={6} key={session.sessionId}>
            <SessionCard session={session} />
          </Grid>
        ))}
        <Grid item xs={12} md={6}>
          <HeartbeatMetricsCard metrics={heartbeatMetrics} />
        </Grid>
        <Grid item xs={12} md={6}>
          <AfkWarningCard warning={afkWarning} />
        </Grid>
        <Grid item xs={12} md={6}>
          <ForceLogoutCard plan={forceLogoutPlan} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default SessionLifecyclePage


