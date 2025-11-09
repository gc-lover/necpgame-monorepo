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
  Slider,
  Chip,
} from '@mui/material'
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder'
import HeartBrokenIcon from '@mui/icons-material/HeartBroken'
import VolunteerActivismIcon from '@mui/icons-material/VolunteerActivism'
import LoopIcon from '@mui/icons-material/Loop'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { RomanceNPCCard, RomanceNPCSummary } from '../components/RomanceNPCCard'
import { RomanceRelationshipCard, RomanceRelationshipSummary } from '../components/RomanceRelationshipCard'
import { RomanceEventCard, RomanceEventSummary } from '../components/RomanceEventCard'
import { RomanceChoiceCard, RomanceEventInstanceSummary } from '../components/RomanceChoiceCard'
import { RomanceSummaryCard } from '../components/RomanceSummaryCard'

const regionOptions = ['ALL', 'Night City', 'America', 'Asia', 'Europe', 'Africa', 'CIS']
const stageOptions = ['ANY', 'MEETING', 'FRIENDSHIP', 'FLIRTING', 'DATING', 'INTIMACY', 'CONFLICT', 'RECONCILIATION', 'COMMITMENT']

const demoNpcs: RomanceNPCSummary[] = [
  {
    npcId: 'npc-judy',
    name: 'Judy Alvarez',
    region: 'Night City',
    orientation: 'BI',
    romanceDifficulty: 'MEDIUM',
    compatibilityScore: 82,
    personalityTraits: ['Creative', 'Idealistic', 'Loyal'],
    interests: ['Braindance', 'Tech', 'Underground Art'],
    currentStatus: 'DATING',
  },
  {
    npcId: 'npc-panam',
    name: 'Panam Palmer',
    region: 'America',
    orientation: 'HETERO',
    romanceDifficulty: 'HARD',
    compatibilityScore: 74,
    personalityTraits: ['Brave', 'Independent', 'Stubborn'],
    interests: ['Nomad Clan', 'Engineering', 'Convoys'],
    currentStatus: 'FRIEND',
  },
]

const demoRelationships: RomanceRelationshipSummary[] = [
  {
    relationshipId: 'rel-judy',
    npcName: 'Judy Alvarez',
    stage: 'DATING',
    affectionLevel: 88,
    trustLevel: 79,
    jealousyLevel: 12,
    eventsCompleted: 14,
    startedAt: '2077-09-21T18:00:00Z',
  },
  {
    relationshipId: 'rel-river',
    npcName: 'River Ward',
    stage: 'FRIENDSHIP',
    affectionLevel: 56,
    trustLevel: 68,
    jealousyLevel: 8,
    eventsCompleted: 6,
    startedAt: '2077-10-12T10:00:00Z',
  },
]

const demoEvents: RomanceEventSummary[] = [
  {
    eventId: 'event-rooftop',
    name: 'Neon Rooftop Date',
    stage: 'DATING',
    description: 'Ужин на крыше с видом на Неоновую Ночью.',
    location: 'Japantown Rooftop',
    durationMinutes: 90,
    affectionImpact: { min: 10, max: 24 },
    requiredAffection: 65,
  },
  {
    eventId: 'event-convoy',
    name: 'Nomad Convoy Mission',
    stage: 'FRIENDSHIP',
    description: 'Впечатляющее приключение с Панам, защита груза.',
    location: 'Badlands Highway',
    durationMinutes: 120,
    affectionImpact: { min: 8, max: 20 },
    requiredAffection: 40,
  },
]

const demoChoiceInstance: RomanceEventInstanceSummary = {
  instanceId: 'inst-night-market',
  eventName: 'Night Market Walk',
  stage: 'FRIENDSHIP',
  choices: [
    { choiceId: 'choice-food', text: 'Купить уличную еду', affectionChange: 5, skillCheck: null },
    { choiceId: 'choice-gift', text: 'Подарить кастомный имплант', affectionChange: 15, skillCheck: 'TECH 14' },
  ],
}

export const RomanceSystemPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [regionFilter, setRegionFilter] = useState<string>('ALL')
  const [stageFilter, setStageFilter] = useState<string>('ANY')
  const [minCompatibility, setMinCompatibility] = useState<number>(60)

  const leftPanel = (
    <Stack spacing={2}>
      <CyberpunkButton
        variant="outlined"
        size="small"
        fullWidth
        startIcon={<VolunteerActivismIcon />}
        onClick={() => navigate('/game')}
      >
        Назад
      </CyberpunkButton>
      {selectedCharacterId && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Character: {selectedCharacterId}
        </Typography>
      )}
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="error.main">
        Romance System
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Управляй 9 стадиями отношений, ревностью и событиями для 1000+ NPC.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        size="small"
        select
        label="Region"
        value={regionFilter}
        onChange={(event) => setRegionFilter(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {regionOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        size="small"
        select
        label="Stage"
        value={stageFilter}
        onChange={(event) => setStageFilter(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {stageOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary" gutterBottom>
          Min compatibility: {minCompatibility}%
        </Typography>
        <Slider
          size="small"
          value={minCompatibility}
          onChange={(_, value) => setMinCompatibility(value as number)}
          step={5}
          min={0}
          max={100}
          sx={{ color: '#ff2a6d' }}
        />
      </Box>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth startIcon={<FavoriteBorderIcon />}>
        Начать роман
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth startIcon={<LoopIcon />}>
        Прогрессировать стадию
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth startIcon={<HeartBrokenIcon />}>
        Завершить отношения
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <RomanceSummaryCard
        activeRelationships={demoRelationships.length}
        maxConcurrent={3}
        jealousyAlerts={1}
        conflicts={0}
        commitmentRate={48}
      />
      <CompactCard color="yellow" glowIntensity="weak" compact>
        <Stack spacing={0.4}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Relationship Tips
          </Typography>
          {[
            'Jealousy > 60 запускает конфликтные ивенты',
            'Commitment недоступен без доверия 80+',
            'Разные регионы → уникальные события и подарки',
          ].map((hint) => (
            <Typography key={hint} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              • {hint}
            </Typography>
          ))}
        </Stack>
      </CompactCard>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <FavoriteBorderIcon sx={{ fontSize: '1.4rem', color: 'error.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Romance Operations Center
        </Typography>
        <Chip label="Mega Romance System" size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert
        severity="info"
        sx={{ fontSize: cyberpunkTokens.fonts.sm }}
      >
        Отслеживай этапы отношений, запускай события и управляй ревностью. 9 стадий, множественные романы, конфликты и примирения.
      </Alert>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Available NPCs
      </Typography>
      <Grid container spacing={1}>
        {demoNpcs.map((npc) => (
          <Grid key={npc.npcId} item xs={12} md={6}>
            <RomanceNPCCard npc={npc} />
          </Grid>
        ))}
      </Grid>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Active Relationships
      </Typography>
      <Grid container spacing={1}>
        {demoRelationships.map((relationship) => (
          <Grid key={relationship.relationshipId} item xs={12} md={6}>
            <RomanceRelationshipCard relationship={relationship} />
          </Grid>
        ))}
      </Grid>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Events & Choices
      </Typography>
      <Grid container spacing={1}>
        {demoEvents.map((event) => (
          <Grid key={event.eventId} item xs={12} md={6}>
            <RomanceEventCard event={event} />
          </Grid>
        ))}
        <Grid item xs={12} md={6}>
          <RomanceChoiceCard instance={demoChoiceInstance} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default RomanceSystemPage


