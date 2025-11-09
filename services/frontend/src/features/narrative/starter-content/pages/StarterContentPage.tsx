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
import MenuBookIcon from '@mui/icons-material/MenuBook'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { OriginStoryCard } from '../components/OriginStoryCard'
import { ClassQuestCard } from '../components/ClassQuestCard'
import { MainStoryQuestCard } from '../components/MainStoryQuestCard'
import { StarterProgressionCard } from '../components/StarterProgressionCard'
import { RecommendedContentCard } from '../components/RecommendedContentCard'

const availableClasses = ['FIXER', 'NETRUNNER', 'NOMAD', 'SOLO', 'TECHIE'] as const
const availablePeriods = ['2023-2045', '2045-2060', '2060-2077', '2077-2093'] as const

const demoOrigin = {
  originId: 'origin-1',
  name: 'Kabuki Street Kid',
  description: 'Start in Kabuki market, debt to local fixer.',
  recommendedClass: 'FIXER',
  startingLocation: 'Night City · Kabuki',
}

const demoClassQuests = [
  {
    questId: 'class-fixer-1',
    name: 'Breaking the Deal',
    questType: 'CLASS',
    description: 'Sabotage corpo shipment for local fixer.',
    rewards: ['Street cred +150', 'Quickhack blueprint'],
  },
  {
    questId: 'class-fixer-2',
    name: 'Brokered Silence',
    questType: 'CLASS',
    description: 'Negotiate peace between Tyger Claws and Moxes.',
    rewards: ['Faction favor', 'Eurodollars +500'],
  },
]

const demoMainStory = [
  {
    questId: 'main-2077-1',
    name: 'Ghosts of Arasaka',
    period: '2077',
    chapter: 3,
    description: 'Investigate relic blacksite in Arasaka ruins.',
    objectives: ['Meet Alt contact', 'Infiltrate relic vault', 'Escape with data'],
  },
  {
    questId: 'main-2077-2',
    name: 'City of Glass',
    period: '2077',
    chapter: 4,
    description: 'Defend Afterlife from Militech raid.',
    objectives: ['Secure entry', 'Hack convoy drones', 'Lead counter assault'],
  },
]

const demoProgression = [
  { step: 1, questId: 'origin-1', questName: 'Kabuki Street Kid', estimatedLevel: 1 },
  { step: 2, questId: 'tutorial-netrun', questName: 'Netrunning 101', estimatedLevel: 2 },
  { step: 3, questId: 'class-fixer-1', questName: 'Breaking the Deal', estimatedLevel: 3 },
  { step: 4, questId: 'main-2077-1', questName: 'Ghosts of Arasaka', estimatedLevel: 5 },
]

const demoRecommended = {
  originQuest: demoClassQuests[0],
  classQuests: demoClassQuests,
  tutorialQuests: [
    {
      questId: 'tutorial-combat',
      name: 'Combat Tutorial',
      questType: 'TUTORIAL',
      description: '',
      rewards: ['Weapon mod'],
    },
    {
      questId: 'tutorial-netrun',
      name: 'Netrunning 101',
      questType: 'TUTORIAL',
      description: '',
      rewards: ['Quickhack'],
    },
  ],
}

export const StarterContentPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [classFilter, setClassFilter] = useState<(typeof availableClasses)[number]>('FIXER')
  const [periodFilter, setPeriodFilter] = useState<(typeof availablePeriods)[number]>('2077-2093')
  const [showTutorials, setShowTutorials] = useState(true)

  const classQuests = useMemo(() => demoClassQuests, [classFilter])
  const mainStory = useMemo(() => demoMainStory.filter((quest) => quest.period === '2077'), [periodFilter])

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
        Starter Content
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Origins, классовые и сюжетные квесты для старта
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        size="small"
        label="Class"
        value={classFilter}
        onChange={(event) => setClassFilter(event.target.value as typeof availableClasses[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {availableClasses.map((item) => (
          <MenuItem key={item} value={item} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {item}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        size="small"
        label="Period"
        value={periodFilter}
        onChange={(event) => setPeriodFilter(event.target.value as typeof availablePeriods[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {availablePeriods.map((period) => (
          <MenuItem key={period} value={period} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {period}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={showTutorials} onChange={(event) => setShowTutorials(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Tutorial quests</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Сформировать стартовый пакет
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Показать класс квесты
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Экспорт в Codex
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <StarterProgressionCard progression={demoProgression} />
      <RecommendedContentCard
        originQuest={demoRecommended.originQuest}
        classQuests={demoRecommended.classQuests}
        tutorialQuests={showTutorials ? demoRecommended.tutorialQuests : []}
      />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <MenuBookIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Starter Content Command Center
        </Typography>
        <Chip label={`Class ${classFilter}`} size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        24 стартовых квеста: origin stories, 5 классовых, фракционные и основная сюжетка (2023-2093). Подготовь персонажа к Night City.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <OriginStoryCard origin={demoOrigin} />
        </Grid>
        {classQuests.map((quest) => (
          <Grid item xs={12} md={6} key={quest.questId}>
            <ClassQuestCard quest={quest} />
          </Grid>
        ))}
        {mainStory.map((quest) => (
          <Grid item xs={12} md={6} key={quest.questId}>
            <MainStoryQuestCard quest={quest} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default StarterContentPage


