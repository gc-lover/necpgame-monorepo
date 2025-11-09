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
import ReportProblemIcon from '@mui/icons-material/ReportProblem'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { IncidentCard } from '../components/IncidentCard'
import { EscalationCard } from '../components/EscalationCard'
import { TimelineCard } from '../components/TimelineCard'
import { RcaCard } from '../components/RcaCard'
import { OnCallCard } from '../components/OnCallCard'

const severities = ['critical', 'high', 'medium', 'low'] as const
const warRooms = ['#war-room-latency', '#war-room-payments', '#war-room-infra'] as const

const incidents = [
  {
    id: 'inc_7f3c2b90',
    title: 'Latency spike on matchmaking',
    severity: 'critical' as const,
    status: 'acknowledged' as const,
    detectedAt: '2025-11-07 19:58',
    commander: 'oncall-engineer',
    affectedServices: ['matchmaking-service', 'voice-provider-adapter'],
  },
  {
    id: 'inc_5a1d3c22',
    title: 'Inventory microservice 500 errors',
    severity: 'high' as const,
    status: 'mitigated' as const,
    detectedAt: '2025-11-06 11:14',
    commander: 'sre-lead',
    affectedServices: ['inventory-service'],
  },
]

const escalations = [
  {
    level: 'L2 SRE',
    target: 'sre@oncall',
    triggeredAt: '2025-11-07 20:01',
    channel: 'PagerDuty',
    status: 'engaged' as const,
  },
  {
    level: 'Lead Engineer',
    target: 'lead-engineer@necpgame',
    triggeredAt: '2025-11-07 20:05',
    channel: 'Slack',
    status: 'pending' as const,
  },
]

const timelineEvents = [
  {
    timestamp: '19:58',
    actor: 'Grafana Alert',
    description: 'Detected latency spike > 5s',
    category: 'detection' as const,
  },
  {
    timestamp: '19:59',
    actor: 'On-call Engineer',
    description: 'Acknowledged alert & joined war-room',
    category: 'communication' as const,
  },
  {
    timestamp: '20:03',
    actor: 'SRE Team',
    description: 'Shifted traffic to EU-West shard',
    category: 'mitigation' as const,
  },
  {
    timestamp: '20:08',
    actor: 'Voice Ops',
    description: 'Reconfigured voice adapter failover',
    category: 'recovery' as const,
  },
]

const rcaRecord = {
  rootCause: 'Cache cluster misconfiguration',
  contributingFactors: ['Config deployed without warmup'],
  correctiveActions: [
    { description: 'Add config validation step', owner: 'sre-team', dueDate: '2025-11-20', status: 'pending' as const },
    { description: 'Implement canary warmup', owner: 'platform-team', dueDate: '2025-11-18', status: 'in_progress' as const },
  ],
  lessonsLearned: ['Require canary validation for cache changes'],
}

const onCallInfo = {
  currentResponder: 'oncall-engineer',
  rotation: 'SRE-primary',
  timeRemaining: '02:15',
  nextUp: 'platform-duty',
}

export const IncidentResponsePage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [severityFilter, setSeverityFilter] = useState<(typeof severities)[number]>('critical')
  const [warRoomChannel, setWarRoomChannel] = useState<(typeof warRooms)[number]>('#war-room-latency')
  const [autoNotify, setAutoNotify] = useState<boolean>(true)

  const visibleIncidents = useMemo(
    () => incidents.filter((incident) => incident.severity === severityFilter),
    [severityFilter],
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
        Incident Response
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        SLA breach triage, эскалации и RCA действия.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        size="small"
        label="Severity"
        value={severityFilter}
        onChange={(event) => setSeverityFilter(event.target.value as typeof severities[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {severities.map((severity) => (
          <MenuItem key={severity} value={severity} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {severity}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        size="small"
        label="War-room channel"
        value={warRoomChannel}
        onChange={(event) => setWarRoomChannel(event.target.value as typeof warRooms[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {warRooms.map((channel) => (
          <MenuItem key={channel} value={channel} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {channel}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={autoNotify} onChange={(event) => setAutoNotify(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Auto-notify escalation chain</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Создать инцидент
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Запустить эскалацию
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Открыть war-room
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <OnCallCard info={onCallInfo} />
      <EscalationCard escalation={escalations[0]} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <ReportProblemIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Incident Response Center
        </Typography>
        <Chip label={severityFilter.toUpperCase()} size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="warning" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        SLA: MTTA ≤ 5 мин, MTTR ≤ 30 мин. Инциденты синхронизированы с on-call и war-room каналами.
      </Alert>
      <Grid container spacing={1}>
        {visibleIncidents.map((incident) => (
          <Grid item xs={12} md={6} key={incident.id}>
            <IncidentCard incident={incident} />
          </Grid>
        ))}
        <Grid item xs={12} md={6}>
          <TimelineCard events={timelineEvents} />
        </Grid>
        <Grid item xs={12} md={6}>
          <RcaCard rca={rcaRecord} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default IncidentResponsePage


