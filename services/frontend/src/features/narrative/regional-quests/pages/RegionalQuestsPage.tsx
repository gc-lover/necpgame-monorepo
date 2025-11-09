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
import PublicIcon from '@mui/icons-material/Public'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { DailyQuestCard } from '../components/DailyQuestCard'
import { WeeklyQuestCard } from '../components/WeeklyQuestCard'
import { RegionalQuestCard } from '../components/RegionalQuestCard'
import { WorldQuestCard } from '../components/WorldQuestCard'
import { QuestAvailabilityCard } from '../components/QuestAvailabilityCard'

const regions = ['AFRICA', 'AMERICA', 'ASIA', 'CIS', 'EUROPE', 'MIDDLE_EAST', 'OCEANIA', 'NIGHT_CITY'] as const
const factions = ['Arasaka', 'Militech', 'Voodoo Boys', 'Nomad Council', 'Tiger Claws'] as const

const dailySample = [
  {
    questId: 'daily-1',
    name: 'Cyber Heist Intercept',
    region: 'NIGHT_CITY',
    difficulty: 'HARD' as const,
    objective: 'Stop data heist in Japantown alley',
    reward: 'Street cred +60, Eurodollars +800',
    resetsAt: '03:00 NCST',
  },
  {
    questId: 'daily-2',
    name: 'Nomad Supply Run',
    region: 'OCEANIA',
    difficulty: 'NORMAL' as const,
    objective: 'Deliver parts to drifting Nomad caravan',
    reward: 'Nomad rep +35, Vehicle mod',
    resetsAt: '03:00 NCST',
  },
]

const weeklySample = [
  {
    questId: 'weekly-1',
    name: 'Gulf States Raid',
    region: 'MIDDLE_EAST',
    recommendedPower: 540,
    description: 'Disable Militech oil rig drone hub in Abu Dhabi corridor.',
    reward: 'Legendary implant schematic',
  },
  {
    questId: 'weekly-2',
    name: 'Siberian Blackwall Breach',
    region: 'CIS',
    recommendedPower: 620,
    description: 'Deploy icebreakers to contain rogue AI beyond Novosibirsk.',
    reward: 'Relic shard + faction standing',
  },
]

const regionalSample = [
  {
    questId: 'regional-1',
    name: 'West Africa Drone Uprising',
    region: 'AFRICA',
    minLevel: 18,
    summary: 'Broker truce between Biotechnica and local nomad tribes as drones rebel.',
    faction: 'Biotechnica',
    repeatable: true,
  },
  {
    questId: 'regional-2',
    name: 'Night City Data Smuggling',
    region: 'NIGHT_CITY',
    minLevel: 22,
    summary: 'Trace encrypted cargo through Watson docks before it vanishes.',
    faction: 'Afterlife Brokers',
    repeatable: false,
  },
]

const worldSample = [
  {
    questId: 'world-1',
    name: 'Arasaka Debt Collection',
    faction: 'Arasaka',
    description: 'Collect overdue payments from five continents in one run.',
    regionImpact: 'Raises Arasaka control +5% globally',
  },
  {
    questId: 'world-2',
    name: 'Nomad Relief Convoy',
    faction: 'Nomad Council',
    description: 'Coordinate multi-region convoy to deliver cybernetic meds.',
    regionImpact: 'Stabilises reputation in America and Europe',
  },
]

const availabilitySample = {
  dailySlotsAvailable: 5,
  dailySlotsUsed: 2,
  weeklySlotsAvailable: 3,
  weeklySlotsUsed: 1,
  resetsAt: '03:00 NCST (global sync)',
}

export const RegionalQuestsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [regionFilter, setRegionFilter] = useState<(typeof regions)[number]>('NIGHT_CITY')
  const [factionFilter, setFactionFilter] = useState<(typeof factions)[number]>('Arasaka')
  const [onlyRepeatable, setOnlyRepeatable] = useState<boolean>(false)

  const visibleRegional = useMemo(
    () =>
      regionalSample.filter(
        (quest) => quest.region === regionFilter && (!onlyRepeatable || quest.repeatable),
      ),
    [regionFilter, onlyRepeatable],
  )

  const visibleDaily = useMemo(
    () => dailySample.filter((quest) => quest.region === regionFilter),
    [regionFilter],
  )

  const visibleWeekly = useMemo(
    () => weeklySample.filter((quest) => quest.region === regionFilter),
    [regionFilter],
  )

  const visibleWorld = useMemo(
    () => worldSample.filter((quest) => quest.faction === factionFilter),
    [factionFilter],
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
        Regional Quests
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Daily, weekly и мировые цепочки по регионам.
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
        label="Faction"
        value={factionFilter}
        onChange={(event) => setFactionFilter(event.target.value as typeof factions[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {factions.map((faction) => (
          <MenuItem key={faction} value={faction} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {faction}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={onlyRepeatable} onChange={(event) => setOnlyRepeatable(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Repeatable only</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Обновить daily пул
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Отправить фракционный запрос
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Экспорт регионов в Codex
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <QuestAvailabilityCard availability={availabilitySample} />
      <Alert severity="warning" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Предупреждение: активация мировых квестов поднимает уровень угрозы корпораций.
      </Alert>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <PublicIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Regional Quest Ops Center
        </Typography>
        <Chip label={regionFilter} size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Регионы: Africa, America, Asia, CIS, Europe, Middle East, Oceania, Night City. Daily/Weekly обновляются в 03:00 NCST, мировые квесты завязаны на фракции.
      </Alert>
      <Grid container spacing={1}>
        {visibleDaily.map((quest) => (
          <Grid item xs={12} md={6} key={quest.questId}>
            <DailyQuestCard quest={quest} />
          </Grid>
        ))}
        {visibleWeekly.map((quest) => (
          <Grid item xs={12} md={6} key={quest.questId}>
            <WeeklyQuestCard quest={quest} />
          </Grid>
        ))}
        {visibleRegional.map((quest) => (
          <Grid item xs={12} md={6} key={quest.questId}>
            <RegionalQuestCard quest={quest} />
          </Grid>
        ))}
        {visibleWorld.map((quest) => (
          <Grid item xs={12} md={6} key={quest.questId}>
            <WorldQuestCard quest={quest} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default RegionalQuestsPage

