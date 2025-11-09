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
import StorageIcon from '@mui/icons-material/Storage'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { RealtimeInstanceCard } from '../components/RealtimeInstanceCard'
import { ZoneCard } from '../components/ZoneCard'
import { TransferPlanCard } from '../components/TransferPlanCard'
import { EvacuationPlanCard } from '../components/EvacuationPlanCard'
import { CellHeatmapCard } from '../components/CellHeatmapCard'
import { TickRateChart } from '../components/TickRateChart'
import { AlertFeedCard } from '../components/AlertFeedCard'

const statuses = ['ONLINE', 'MAINTENANCE', 'DRAINING', 'OFFLINE'] as const
const regions = ['night-city/eu-west', 'night-city/us-east', 'night-city/apac'] as const

const instanceData = [
  {
    instanceId: 'rt-nyc-01',
    region: 'night-city/eu-west',
    status: 'ONLINE' as const,
    tickRate: 30 as const,
    maxPlayers: 1200,
    activePlayers: 980,
    maxZones: 6,
    supportedZoneTypes: ['urban', 'corporate', 'raid'],
    metadata: { build: '0.9.7', gitSha: 'a1b2c3d' },
  },
  {
    instanceId: 'rt-nyc-02',
    region: 'night-city/us-east',
    status: 'DRAINING' as const,
    tickRate: 20 as const,
    maxPlayers: 800,
    activePlayers: 450,
    maxZones: 4,
    supportedZoneTypes: ['urban', 'corporate'],
    metadata: { build: '0.9.7', gitSha: 'd4e5f6g' },
  },
]

const zoneData = [
  {
    zoneId: 'night-city.watson',
    zoneName: 'Watson Downtown',
    status: 'ONLINE' as const,
    assignedServerId: 'rt-nyc-01',
    playerCount: 420,
    npcCount: 320,
    isPvpEnabled: true,
  },
  {
    zoneId: 'night-city.kabuki',
    zoneName: 'Kabuki District',
    status: 'MIGRATING' as const,
    assignedServerId: 'rt-nyc-02',
    playerCount: 280,
    npcCount: 150,
    isPvpEnabled: true,
  },
]

const transferPlan = {
  targetInstanceId: 'rt-nyc-03',
  drainStrategy: 'gradual' as const,
  priority: 'high' as const,
  reason: 'CPU load 92%',
  scheduledFor: '2025-11-07 18:20',
}

const evacuationPlan = {
  targetZoneId: 'night-city.watson.safe',
  batchSize: 25,
  intervalMs: 500,
  notifyPlayers: true,
  timeoutSeconds: 90,
}

const cellMetrics = [
  { cellKey: 'A1', playerCount: 64, npcCount: 32, latencyMs: 35 },
  { cellKey: 'B4', playerCount: 48, npcCount: 21, latencyMs: 42 },
  { cellKey: 'C3', playerCount: 12, npcCount: 5, latencyMs: 28 },
  { cellKey: 'D2', playerCount: 88, npcCount: 40, latencyMs: 55 },
]

const tickMetrics = [
  { timestamp: '18:00', tickDurationMs: 38, warnings: [] },
  { timestamp: '18:05', tickDurationMs: 58, warnings: ['TICK_OVER_50MS'] },
  { timestamp: '18:10', tickDurationMs: 42, warnings: [] },
  { timestamp: '18:15', tickDurationMs: 47, warnings: [] },
]

const alertData = [
  { id: 'alert-1', level: 'warning' as const, message: 'Tick duration > 50ms on rt-nyc-02', raisedAt: '18:05', resolvedAt: '18:12' },
  { id: 'alert-2', level: 'info' as const, message: 'Zone transfer scheduled for Kabuki', raisedAt: '18:07' },
]

export const RealtimeServerZonesPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [statusFilter, setStatusFilter] = useState<(typeof statuses)[number]>('ONLINE')
  const [regionFilter, setRegionFilter] = useState<(typeof regions)[number]>('night-city/eu-west')
  const [autoDrain, setAutoDrain] = useState<boolean>(true)

  const visibleInstances = useMemo(
    () =>
      instanceData.filter(
        (instance) => instance.status === statusFilter && instance.region === regionFilter,
      ),
    [statusFilter, regionFilter],
  )

  const visibleZones = useMemo(
    () => zoneData.filter((zone) => zone.status !== 'OFFLINE'),
    [],
  )

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
        Realtime Ops
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Управление инстансами, зонами и эвакуацией игроков.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        size="small"
        label="Status"
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
        control={<Switch size="small" checked={autoDrain} onChange={(event) => setAutoDrain(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Auto drain overload zones</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Зарегистрировать инстанс
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Планировать перенос зоны
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Эвакуировать игроков
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <TickRateChart metrics={tickMetrics} />
      <AlertFeedCard alerts={alertData} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <StorageIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Realtime Server Zones
        </Typography>
        <Chip label={regionFilter} size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="warning" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        SLA: Tick Duration ≤ 45ms, зона load balancing ≤ 70%. Авто эвакуация при CPU > 90%.
      </Alert>
      <Grid container spacing={1}>
        {visibleInstances.map((instance) => (
          <Grid item xs={12} md={6} key={instance.instanceId}>
            <RealtimeInstanceCard instance={instance} />
          </Grid>
        ))}
        {visibleZones.map((zone) => (
          <Grid item xs={12} md={6} key={zone.zoneId}>
            <ZoneCard zone={zone} />
          </Grid>
        ))}
        <Grid item xs={12} md={6}>
          <TransferPlanCard plan={transferPlan} />
        </Grid>
        <Grid item xs={12} md={6}>
          <EvacuationPlanCard plan={evacuationPlan} />
        </Grid>
        <Grid item xs={12} md={6}>
          <CellHeatmapCard cells={cellMetrics} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default RealtimeServerZonesPage


