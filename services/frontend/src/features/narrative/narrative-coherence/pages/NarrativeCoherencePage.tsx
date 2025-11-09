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
import TimelineIcon from '@mui/icons-material/Timeline'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import SyncIcon from '@mui/icons-material/Sync'
import ReviewsIcon from '@mui/icons-material/Reviews'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { PlotThreadCard } from '../components/PlotThreadCard'
import { ArcStatusCard } from '../components/ArcStatusCard'
import { NarrativeRiskCard } from '../components/NarrativeRiskCard'
import { ContinuityAlertCard } from '../components/ContinuityAlertCard'
import { NarrativeSummaryCard } from '../components/NarrativeSummaryCard'
import { useGameState } from '@/features/game/hooks/useGameState'

const threadFilterOptions = ['ALL', 'MAIN', 'FACTION', 'ROMANCE', 'SIDE'];
const severityFilterOptions = ['ALL', 'INFO', 'WARNING', 'CRITICAL'];

const demoThreads = [
  {
    threadId: 'thread-phantom',
    title: 'Phantom Rebellion',
    faction: 'Neon Phantoms',
    arcStage: 'CONFLICT' as const,
    coherenceScore: 82,
    openBeats: 4,
    resolvedBeats: 6,
    synopsis: 'Underground cells prepare for a coordinated strike on corporate networks.',
    category: 'FACTION',
  },
  {
    threadId: 'thread-romance',
    title: 'Chrome Hearts',
    faction: 'Personal',
    arcStage: 'CLIMAX' as const,
    coherenceScore: 74,
    openBeats: 2,
    resolvedBeats: 7,
    synopsis: 'Multiple romance arcs converge before the gala event.',
    category: 'ROMANCE',
  },
]

const demoArcs = [
  {
    arcName: 'Corporate War 2.0',
    phase: 'PHASE_2' as const,
    episodesReleased: 5,
    totalEpisodes: 9,
    branchingPoints: 3,
    coherenceDelta: -6,
  },
  {
    arcName: 'Night Market Legends',
    phase: 'PHASE_1' as const,
    episodesReleased: 3,
    totalEpisodes: 8,
    branchingPoints: 2,
    coherenceDelta: 4,
  },
]

const demoRisks = [
  {
    riskId: 'risk-1',
    description: 'Major continuity break detected between “Phantom Rebellion” and “Corporate War 2.0”.',
    severity: 'CRITICAL' as const,
    impactedThreads: ['Phantom Rebellion', 'Corporate War 2.0'],
    mitigation: 'Trigger retcon patch questline and update briefings.',
  },
]

const demoAlerts = [
  {
    alertId: 'alert-1',
    title: 'Quest timeline discrepancy',
    severity: 'WARNING' as const,
    description: 'Quest 34 resolves before prerequisite event 31.',
    recommendedAction: 'Lock quest 34 until event 31 completed.',
    detectedAt: '2077-11-07 19:45',
  },
]

const summaryStats = {
  activeThreads: 12,
  arcsTracked: 5,
  unresolvedBeats: 9,
  totalCoherence: 86,
  lastSync: '2077-11-07 23:45',
}

export const NarrativeCoherencePage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [threadFilter, setThreadFilter] = useState<string>('ALL')
  const [severityFilter, setSeverityFilter] = useState<string>('ALL')
  const [autoSync, setAutoSync] = useState<boolean>(true)

  const filteredThreads = demoThreads.filter((thread) => threadFilter === 'ALL' || thread.category === threadFilter)
  const filteredAlerts = demoAlerts.filter((alert) => severityFilter === 'ALL' || alert.severity === severityFilter)

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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info.main">
        Narrative Coherence
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Монитор событий, арок, рисков. Поддерживай целостность истории Night City.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        size="small"
        label="Thread Type"
        value={threadFilter}
        onChange={(event) => setThreadFilter(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {threadFilterOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        size="small"
        label="Alert Severity"
        value={severityFilter}
        onChange={(event) => setSeverityFilter(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {severityFilterOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={autoSync} onChange={(event) => setAutoSync(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Auto sync with writers</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth startIcon={<SyncIcon />}>
        Sync coherence data
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth startIcon={<ReviewsIcon />}>
        Launch continuity review
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <NarrativeSummaryCard {...summaryStats} />
      <Alert severity="warning" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Coherence drift > 5% требует внимания сценаристов. Настрой автоматический retcon-патч или вручную скорректируй арки.
      </Alert>
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Автосинхронизация обновляет квестовые цепочки, NPC-расписания и фракционные события.
      </Alert>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <TimelineIcon sx={{ fontSize: '1.4rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Narrative Coherence Command Center
        </Typography>
        <Chip label="narrative" size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Следи за сюжетными арками, ветвлениями и рисками. Поддерживай единый таймлайн в Night City и интервенциях за пределами города.
      </Alert>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Plot Threads
      </Typography>
      <Grid container spacing={1}>
        {filteredThreads.map((thread) => (
          <Grid key={thread.threadId} item xs={12} md={6}>
            <PlotThreadCard thread={thread} />
          </Grid>
        ))}
      </Grid>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Arc Status
      </Typography>
      <Grid container spacing={1}>
        {demoArcs.map((arc) => (
          <Grid key={arc.arcName} item xs={12} md={6}>
            <ArcStatusCard arc={arc} />
          </Grid>
        ))}
      </Grid>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Risks & Alerts
      </Typography>
      <Grid container spacing={1}>
        {demoRisks.map((risk) => (
          <Grid key={risk.riskId} item xs={12} md={6}>
            <NarrativeRiskCard risk={risk} />
          </Grid>
        ))}
        {filteredAlerts.map((alert) => (
          <Grid key={alert.alertId} item xs={12} md={6}>
            <ContinuityAlertCard alert={alert} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default NarrativeCoherencePage


