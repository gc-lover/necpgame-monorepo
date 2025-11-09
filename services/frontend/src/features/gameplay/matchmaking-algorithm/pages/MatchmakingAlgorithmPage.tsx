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
import MatchIcon from '@mui/icons-material/SportsEsports'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { QueueStatusCard } from '../components/QueueStatusCard'
import { MatchTicketCard } from '../components/MatchTicketCard'
import { ReadyCheckCard } from '../components/ReadyCheckCard'
import { QualityMetricsCard } from '../components/QualityMetricsCard'
import { TelemetryCard } from '../components/TelemetryCard'
import { AnalyticsCard } from '../components/AnalyticsCard'

const modes = ['RANKED_PVP', 'SCRIM', 'PVE_RAID'] as const
const regions = ['Night City', 'Europe', 'Asia'] as const

const queueSamples = [
  {
    mode: 'RANKED_PVP',
    population: 842,
    estimatedWait: '01:12',
    inReadyCheck: 38,
    activeTickets: 112,
  },
  {
    mode: 'PVE_RAID',
    population: 214,
    estimatedWait: '00:45',
    inReadyCheck: 12,
    activeTickets: 54,
  },
]

const ticketSamples = [
  {
    ticketId: 'MM-4F9A',
    mode: 'RANKED_PVP',
    players: 5,
    latencyMs: 32,
    status: 'READY_CHECK' as const,
    createdAt: '02:14 ago',
  },
  {
    ticketId: 'MM-7B21',
    mode: 'SCRIM',
    players: 10,
    latencyMs: 48,
    status: 'SEARCHING' as const,
    createdAt: '00:37 ago',
  },
]

const readyCheckSample = {
  matchId: 'MATCH-8921',
  expiresInSeconds: 34,
  accepted: 8,
  declined: 1,
  pending: 1,
}

const qualityMetricsSample = [
  { name: 'Latency spread', target: '< 45 ms', current: '38 ms', status: 'OK' as const },
  { name: 'Skill gap', target: '< 120 MMR', current: '135', status: 'WARN' as const },
  { name: 'Role coverage', target: '100%', current: '96%', status: 'WARN' as const },
  { name: 'Queue dodge', target: '< 3%', current: '2.6%', status: 'OK' as const },
]

const telemetrySample = [
  { label: 'P50', value: 28, percentile: 50 },
  { label: 'P75', value: 41, percentile: 75 },
  { label: 'P90', value: 63, percentile: 90 },
  { label: 'P99', value: 110, percentile: 99 },
]

const analyticsSample = {
  matchesToday: 12840,
  averageWait: '00:56',
  cancellations: 284,
  dodges: 167,
}

export const MatchmakingAlgorithmPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [modeFilter, setModeFilter] = useState<(typeof modes)[number]>('RANKED_PVP')
  const [regionFilter, setRegionFilter] = useState<(typeof regions)[number]>('Night City')
  const [autoRequeue, setAutoRequeue] = useState<boolean>(true)

  const visibleQueues = useMemo(() => queueSamples.filter((queue) => queue.mode === modeFilter), [modeFilter])
  const visibleTickets = useMemo(() => ticketSamples.filter((ticket) => ticket.mode !== 'PVE_RAID'), [])

  const leftPanel = (
    <Stack spacing={2}>
      <CyberpunkButton
        variant="outlined"
        size="small"
        fullWidth
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/game')}
      >
        Назад
      </CyberpunkButton>
      {selectedCharacterId && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Character: {selectedCharacterId}
        </Typography>
      )}
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="secondary.main">
        Matchmaking Ops
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Управление очередями, ready-check и качеством матчей.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        size="small"
        label="Mode"
        value={modeFilter}
        onChange={(event) => setModeFilter(event.target.value as typeof modes[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {modes.map((mode) => (
          <MenuItem key={mode} value={mode} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {mode}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        size="small"
        label="Region"
        value={regionFilter}
        onChange={(event) => setRegionFilter(event.target.value as typeof regions[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {regions.map((region) => (
          <MenuItem key={region} value={region} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {region}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={autoRequeue} onChange={(event) => setAutoRequeue(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Auto requeue after dodge</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Стартовать подбор
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Сбросить ready-check
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Экспортировать телеметрию
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <TelemetryCard telemetry={telemetrySample} />
      <AnalyticsCard snapshot={analyticsSample} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <MatchIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Matchmaking Control Center
        </Typography>
        <Chip label={modeFilter} size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        SLA: PvP ≤ 120s, PvE ≤ 90s. Ready-check покрытие, latency buckets и skill gap отслеживаются в реальном времени.
      </Alert>
      <Grid container spacing={1}>
        {visibleQueues.map((queue) => (
          <Grid item xs={12} md={6} key={queue.mode}>
            <QueueStatusCard status={queue} />
          </Grid>
        ))}
        {visibleTickets.map((ticket) => (
          <Grid item xs={12} md={6} key={ticket.ticketId}>
            <MatchTicketCard ticket={ticket} />
          </Grid>
        ))}
        <Grid item xs={12} md={6}>
          <ReadyCheckCard state={readyCheckSample} />
        </Grid>
        <Grid item xs={12} md={6}>
          <QualityMetricsCard metrics={qualityMetricsSample} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default MatchmakingAlgorithmPage


