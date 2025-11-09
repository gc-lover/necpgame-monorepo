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
import MemoryIcon from '@mui/icons-material/Memory'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { CompatibilityCard } from '../components/CompatibilityCard'
import { DialogueGeneratorCard } from '../components/DialogueGeneratorCard'
import { TriggerPredictionCard } from '../components/TriggerPredictionCard'
import { PersonalityCard } from '../components/PersonalityCard'
import { NPCDecisionCard } from '../components/NPCDecisionCard'
import { AlgorithmMetricsCard } from '../components/AlgorithmMetricsCard'

const channels = ['ALL', 'ROMANCE', 'NPC', 'DECISION'] as const

const sampleCompatibility = {
  score: 86,
  recommendation: 'Trigger shared braindance mission',
  stage: 'DATING → INTIMACY',
  factors: [
    { name: 'Chemistry', weight: 0.32, contribution: 0.92 },
    { name: 'Trust history', weight: 0.24, contribution: 0.88 },
    { name: 'Conflict risk', weight: 0.18, contribution: 0.42 },
    { name: 'Narrative arc', weight: 0.26, contribution: 0.76 },
  ],
}

const sampleDialogue = {
  npcName: 'Judy Alvarez',
  tone: 'INTIMATE',
  stage: 'Act II',
  dialogueText: 'I archived a braindance that reminds me of us. Want to dive in tonight?',
  choices: [
    { id: 'choice-1', text: 'Set up the gear. I am in.', impact: '+affection' },
    { id: 'choice-2', text: 'Maybe after the next gig.', impact: 'neutral' },
    { id: 'choice-3', text: 'This feels too intense.', impact: '-trust' },
  ],
}

const sampleTrigger = {
  relationshipId: 'rel-judy-v',
  shouldTrigger: true,
  eventId: 'event-braindance-night',
  triggerProbability: 0.82,
  blockingReasons: [] as string[],
}

const samplePersonality = {
  npcName: 'NPC-774 · Fixer',
  template: 'FixerMentor',
  faction: 'Afterlife',
  region: 'Night City',
  role: 'Questgiver',
  traits: [
    { trait: 'Pragmatic', score: 0.91 },
    { trait: 'Loyal', score: 0.73 },
    { trait: 'Vengeful', score: 0.38 },
    { trait: 'Risky', score: 0.66 },
  ],
  quirks: ['Collects antique tech', 'Dislikes corpo slang'],
}

const sampleDecision = {
  context: 'Night Market negotiation',
  primaryAction: 'Offer rare implant discount',
  confidence: 0.78,
  options: [
    { action: 'Offer discount', probability: 0.42, rationale: 'Increase loyalty' },
    { action: 'Deny request', probability: 0.27, rationale: 'Maintain scarcity' },
    { action: 'Redirect to quest', probability: 0.21, rationale: 'Tie to storyline' },
  ],
}

const sampleMetrics = {
  latencyMs: 38,
  throughputPerMin: 220,
  cacheHitRate: 92,
  queueDepth: 4,
  incidents24h: 0,
}

export const AIAlgorithmsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [channelFilter, setChannelFilter] = useState<(typeof channels)[number]>('ALL')
  const [debugMode, setDebugMode] = useState<boolean>(false)

  const visibleCards = useMemo(() => {
    if (channelFilter === 'ROMANCE') {
      return ['compatibility', 'dialogue', 'trigger']
    }
    if (channelFilter === 'NPC') {
      return ['personality']
    }
    if (channelFilter === 'DECISION') {
      return ['decision']
    }
    return ['compatibility', 'dialogue', 'trigger', 'personality', 'decision']
  }, [channelFilter])

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
        AI Algorithms Lab
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Внутренние сервисы: romance AI, personality engine, decision brain.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Filters
      </Typography>
      <TextField
        select
        size="small"
        label="Channel"
        value={channelFilter}
        onChange={(event) => setChannelFilter(event.target.value as typeof channels[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {channels.map((channel) => (
          <MenuItem key={channel} value={channel} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {channel}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={debugMode} onChange={(event) => setDebugMode(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Debug payloads</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Actions
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Пересчитать совместимость
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Сгенерировать диалог
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Экспортировать JSON
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <AlgorithmMetricsCard metrics={sampleMetrics} />
      <Alert severity="warning" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        INTERNAL ONLY • Debug mode {debugMode ? 'active' : 'disabled'} • Queue depth {sampleMetrics.queueDepth}
      </Alert>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <MemoryIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          AI Algorithm Control Center
        </Typography>
        <Chip label="internal" size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Мониторинг romance AI, personality engine и decision brain. Все алгоритмы в одном SPA, умещается на один экран.
      </Alert>
      <Grid container spacing={1}>
        {visibleCards.includes('compatibility') && (
          <Grid item xs={12} md={6}>
            <CompatibilityCard summary={sampleCompatibility} />
          </Grid>
        )}
        {visibleCards.includes('dialogue') && (
          <Grid item xs={12} md={6}>
            <DialogueGeneratorCard dialogue={sampleDialogue} />
          </Grid>
        )}
        {visibleCards.includes('trigger') && (
          <Grid item xs={12} md={6}>
            <TriggerPredictionCard trigger={sampleTrigger} />
          </Grid>
        )}
        {visibleCards.includes('personality') && (
          <Grid item xs={12} md={6}>
            <PersonalityCard personality={samplePersonality} />
          </Grid>
        )}
        {visibleCards.includes('decision') && (
          <Grid item xs={12} md={6}>
            <NPCDecisionCard decision={sampleDecision} />
          </Grid>
        )}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default AIAlgorithmsPage


