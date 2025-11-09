import React, { useState } from 'react'
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
  FormControlLabel,
  Switch,
  Chip,
} from '@mui/material'
import PublicIcon from '@mui/icons-material/Public'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import StorageIcon from '@mui/icons-material/Storage'
import ReportIcon from '@mui/icons-material/Report'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { GlobalStateSummaryCard } from '../components/GlobalStateSummaryCard'
import { StateComponentCard } from '../components/StateComponentCard'
import { SyncStatusCard } from '../components/SyncStatusCard'
import { ConflictResolutionCard } from '../components/ConflictResolutionCard'
import { StateSnapshotCard } from '../components/StateSnapshotCard'
import { OperationQueueCard } from '../components/OperationQueueCard'

const componentFilters = ['ALL', 'WORLD', 'FACTIONS', 'ECONOMY', 'PLAYER', 'QUESTS', 'COMBAT']
const riskFilters = ['ALL', 'RESOLVED', 'RESOLVING', 'PENDING']

const summaryStats = {
  worldVersion: 124,
  factionVersion: 88,
  economyVersion: 71,
  activeSessions: 3287,
  mutationQueue: 14,
  globalCoherence: 91,
}

const componentStates = [
  {
    component: 'WORLD' as const,
    version: 124,
    health: 94,
    pendingMutations: 3,
    drift: 2,
    lastUpdated: '2077-11-08 00:12',
  },
  {
    component: 'FACTIONS' as const,
    version: 88,
    health: 86,
    pendingMutations: 5,
    drift: 6,
    lastUpdated: '2077-11-08 00:09',
  },
  {
    component: 'ECONOMY' as const,
    version: 71,
    health: 79,
    pendingMutations: 7,
    drift: 11,
    lastUpdated: '2077-11-08 00:05',
  },
  {
    component: 'PLAYER' as const,
    version: 215,
    health: 92,
    pendingMutations: 2,
    drift: 1,
    lastUpdated: '2077-11-08 00:13',
  },
]

const syncStatus = {
  syncStatus: 'DEGRADED' as const,
  syncQueueSize: 12,
  successRate: 78,
  nodes: [
    { node: 'shard-eu', latencyMs: 42, driftMs: 12, lastAck: '2077-11-08 00:05' },
    { node: 'shard-na', latencyMs: 35, driftMs: 7, lastAck: '2077-11-08 00:06' },
  ],
}

const conflictData = [
  {
    conflictId: 'conf-1',
    component: 'ECONOMY',
    detectedAt: '2077-11-07 22:30',
    status: 'RESOLVING' as const,
    strategy: 'Replaying last 5 ops with CAS',
  },
  {
    conflictId: 'conf-2',
    component: 'FACTIONS',
    detectedAt: '2077-11-07 21:40',
    status: 'PENDING' as const,
    strategy: 'Manual merge by writer on duty',
  },
]

const snapshotData = {
  snapshotId: 'snap-2077-11-07T22:00Z',
  createdAt: '2077-11-07 22:00',
  createdBy: 'System Scheduler',
  sizeMb: 128,
  tags: ['pre-raid', 'night-city'],
  rollbackAvailable: true,
}

const queueData = {
  queueSize: 18,
  throughputPerMinute: 40,
  backlogMinutes: 12,
  operations: [
    { opId: 'op-1', type: 'CAS_UPDATE', component: 'ECONOMY', retries: 1, etaMs: 240 },
    { opId: 'op-2', type: 'DELTA_PATCH', component: 'FACTIONS', retries: 0, etaMs: 180 },
  ],
}

export const GlobalStateExtendedPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [componentFilter, setComponentFilter] = useState<string>('ALL')
  const [riskFilter, setRiskFilter] = useState<string>('ALL')
  const [autoSnapshots, setAutoSnapshots] = useState<boolean>(true)

  const filteredComponents = componentStates.filter((state) => componentFilter === 'ALL' || state.component === componentFilter)
  const filteredConflicts = conflictData.filter((conflict) => riskFilter === 'ALL' || conflict.status === riskFilter)

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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="success.main">
        Global State Extended
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Версии мира, синхронизация, конфликты, снапшоты — всё в одном экране.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        size="small"
        label="Component"
        value={componentFilter}
        onChange={(event) => setComponentFilter(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {componentFilters.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        size="small"
        label="Conflict status"
        value={riskFilter}
        onChange={(event) => setRiskFilter(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {riskFilters.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={autoSnapshots} onChange={(event) => setAutoSnapshots(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Auto snapshots</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth startIcon={<StorageIcon />}>
        Применить delta patch
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth startIcon={<ReportIcon />}>
        Создать конфликтный отчёт
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <GlobalStateSummaryCard {...summaryStats} />
      <SyncStatusCard {...syncStatus} />
      <Alert severity="warning" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Drift > 5%: рекомендуется пересинхронизация шардов и ручная проверка экономического состояния.
      </Alert>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <PublicIcon sx={{ fontSize: '1.4rem', color: 'success.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Global State Control Center
        </Typography>
        <Chip label="technical" size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Управляй глобальными состояниями: версии мира, синхронизация шардов, очередь мутаций, снапшоты и конфликты.
      </Alert>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        State Components
      </Typography>
      <Grid container spacing={1}>
        {filteredComponents.map((state) => (
          <Grid key={state.component} item xs={12} md={6}>
            <StateComponentCard state={state} />
          </Grid>
        ))}
      </Grid>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Conflicts & Queue
      </Typography>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <ConflictResolutionCard conflicts={filteredConflicts} />
        </Grid>
        <Grid item xs={12} md={6}>
          <OperationQueueCard {...queueData} />
        </Grid>
      </Grid>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Snapshots
      </Typography>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <StateSnapshotCard snapshot={snapshotData} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default GlobalStateExtendedPage


