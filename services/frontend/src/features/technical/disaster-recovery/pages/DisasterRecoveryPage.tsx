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
import SecurityIcon from '@mui/icons-material/Security'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { DrStatusCard } from '../components/DrStatusCard'
import { BackupPlanCard } from '../components/BackupPlanCard'
import { FailoverTargetsCard } from '../components/FailoverTargetsCard'
import { EmergencyActionsCard } from '../components/EmergencyActionsCard'
import { IncidentLogCard } from '../components/IncidentLogCard'

const regions = ['Night City', 'Europe', 'Asia', 'America'] as const
const backupModes = ['Full', 'Incremental', 'Snapshot'] as const

const statusSample = {
  ready: true,
  lastBackup: '2025-11-08 02:45 NCST',
  backupFrequency: 'every 30 min incremental / daily full',
  failoverReady: true,
  rpoMinutes: 25,
  rtoMinutes: 60,
}

const backupPlans = [
  {
    name: 'Night City Full Backup',
    cadence: 'Daily 02:00',
    retention: '30 versions',
    lastRun: '2025-11-08 02:05',
    nextRun: '2025-11-09 02:00',
  },
  {
    name: 'Incremental Stream',
    cadence: 'Every 30 minutes',
    retention: '48 hours',
    lastRun: '2025-11-08 02:32',
    nextRun: '2025-11-08 03:02',
  },
]

const failoverTargets = [
  {
    datacenter: 'Tokyo-ARC-02',
    region: 'Asia',
    latencyMs: 38,
    capacityPercent: 82,
  },
  {
    datacenter: 'Berlin-SHIELD-01',
    region: 'Europe',
    latencyMs: 42,
    capacityPercent: 67,
  },
  {
    datacenter: 'Denver-VANGUARD-03',
    region: 'America',
    latencyMs: 55,
    capacityPercent: 74,
  },
]

const incidentLog = [
  {
    timestamp: '2025-11-07 19:20',
    type: 'Failover drill',
    summary: 'Simulated Arasaka datacenter outage, failover completed in 54 min',
    status: 'passed',
  },
  {
    timestamp: '2025-11-06 08:15',
    type: 'Backup anomaly',
    summary: 'Incremental backup lag detected, auto healed via resync',
    status: 'resolved',
  },
  {
    timestamp: '2025-11-05 03:00',
    type: 'RPO breach',
    summary: 'Nomad cluster offline for 40 min, RPO escalated',
    status: 'mitigated',
  },
]

export const DisasterRecoveryPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [regionFilter, setRegionFilter] = useState<(typeof regions)[number]>('Night City')
  const [backupMode, setBackupMode] = useState<(typeof backupModes)[number]>('Full')
  const [autoFailover, setAutoFailover] = useState<boolean>(true)

  const filteredPlans = useMemo(() => backupPlans.slice(0, backupMode === 'Full' ? 1 : 2), [backupMode])

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
        Disaster Recovery Ops
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Emergencies, backups и failover подготовка.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
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
      <TextField
        select
        size="small"
        label="Backup mode"
        value={backupMode}
        onChange={(event) => setBackupMode(event.target.value as typeof backupModes[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {backupModes.map((mode) => (
          <MenuItem key={mode} value={mode} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {mode}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={autoFailover} onChange={(event) => setAutoFailover(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Auto failover</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Проверить готовность
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Включить синхронизацию
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Открыть журнал инцидентов
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <EmergencyActionsCard />
      <FailoverTargetsCard targets={failoverTargets} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <SecurityIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Disaster Recovery Command Center
        </Typography>
        <Chip label={regionFilter} size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Защищаем бизнес-континуитет: backups, failover, RPO/RTO. Автопроверки каждые 15 минут, ручные emergency процедуры доступны.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <DrStatusCard status={statusSample} />
        </Grid>
        {filteredPlans.map((plan) => (
          <Grid item xs={12} md={6} key={plan.name}>
            <BackupPlanCard plan={plan} />
          </Grid>
        ))}
        <Grid item xs={12} md={6}>
          <IncidentLogCard incidents={incidentLog} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default DisasterRecoveryPage


