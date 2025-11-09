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
} from '@mui/material'
import AssignmentTurnedInIcon from '@mui/icons-material/AssignmentTurnedIn'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { EndpointsCard } from '../components/EndpointsCard'
import { ModelsCard } from '../components/ModelsCard'
import { InitialDataCard } from '../components/InitialDataCard'
import { ContentOverviewCard } from '../components/ContentOverviewCard'
import { ContentStatusCard } from '../components/ContentStatusCard'
import { TextVersionStateCard } from '../components/TextVersionStateCard'
import { MainUIDataCard } from '../components/MainUIDataCard'
import { MVPHealthCard } from '../components/MVPHealthCard'

const periodOptions = ['2020-2030', '2030-2045', '2045-2060', '2060-2077', '2078-2090', '2090-2093']

const demoEndpoints = {
  total: 128,
  categories: [
    { category: 'Auth', count: 8 },
    { category: 'Gameplay', count: 42 },
    { category: 'Economy', count: 28 },
  ],
  endpoints: [
    { endpoint: '/auth/login', method: 'POST', priority: 'CRITICAL' as const, implemented: true },
    { endpoint: '/economy/market/core', method: 'GET', priority: 'HIGH' as const, implemented: false },
    { endpoint: '/quests/start', method: 'POST', priority: 'HIGH' as const, implemented: true },
  ],
}

const demoModels = [
  {
    modelName: 'Character',
    description: 'Основные данные персонажа',
    fields: [
      { fieldName: 'name', type: 'string', required: true },
      { fieldName: 'level', type: 'number', required: true },
      { fieldName: 'faction_id', type: 'uuid', required: false },
    ],
  },
  {
    modelName: 'Quest',
    description: 'Прогресс и состояние квестов',
    fields: [
      { fieldName: 'quest_id', type: 'uuid', required: true },
      { fieldName: 'status', type: 'enum', required: true },
    ],
  },
]

const demoInitialData = {
  starterItems: [
    { itemId: 'item-starter-001', quantity: 1 },
    { itemId: 'item-medkit-001', quantity: 3 },
  ],
  starterQuests: ['First Day in Night City', 'Meet The Fixer'],
  starterLocations: ['Watson', 'Downtown Market'],
  npcs: [
    { npcId: 'npc-jackie', name: 'Jackie Welles', location: 'Watson Bar', role: 'Companion' },
    { npcId: 'npc-dex', name: 'Dex', location: 'Afterlife', role: 'Fixer' },
  ],
}

const demoContentOverview = {
  period: '2020-2030',
  totalQuests: 120,
  questsByType: { main: 10, side: 80, faction: 30 },
  totalLocations: 45,
  totalNPCs: 320,
  keyEvents: ['Arasaka corporate war', 'Emergence of Night Market'],
  implementedPercentage: 68,
}

const demoContentStatus = {
  ready: false,
  totalQuests: 320,
  totalLocations: 120,
  totalNPCs: 860,
  systemsReady: {
    quest_engine: true,
    combat: true,
    progression: false,
    social: true,
    economy: false,
  },
}

const demoTextState = {
  character: { name: 'V', level: 6, location: 'Watson Market', hp: 54, hpMax: 70 },
  availableActions: [
    { action: 'move', description: 'Перейти в район Heywood', command: '/move heywood' },
    { action: 'trade', description: 'Открыть торговлю с Fixer', command: '/trade fixer' },
  ],
  currentQuest: { questName: 'Night Market Setup', objectives: ['Найти поставщика', 'Договориться о цене'] },
  inventorySummary: { itemsCount: 18, weight: 32.4 },
  nearbyNPCs: [
    { name: 'Fixer Dex', canInteract: true },
    { name: 'Maelstrom Scout', canInteract: false },
  ],
}

const demoUIData = {
  character: { name: 'V', level: 6, xp: 1680, xpNeeded: 2200 },
  stats: { STR: 6, INT: 7, REF: 8 },
  quests: ['Night Market Setup', 'Maelstrom Negotiations'],
  notifications: ['New gig from Rogue', 'Inventory near capacity'],
}

const demoHealth = {
  status: 'DEGRADED' as const,
  systems: {
    auth: 'HEALTHY' as const,
    player_management: 'HEALTHY' as const,
    quest_engine: 'DEGRADED' as const,
    combat_session: 'HEALTHY' as const,
    progression: 'DEGRADED' as const,
  },
}

export const MVPContentPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [period, setPeriod] = useState<string>('2020-2030')

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
        MVP Content
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Каталог MVP эндпоинтов, моделей и стартовых данных. Основа текстовой версии и UI.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Период контента
      </Typography>
      <TextField
        select
        size="small"
        label="Period"
        value={period}
        onChange={(event) => setPeriod(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {periodOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Открыть документацию
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Проверить покрытие API
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Экспорт стартовых данных
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <ContentStatusCard {...demoContentStatus} />
      <MVPHealthCard health={demoHealth} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <AssignmentTurnedInIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          MVP Content Command Center
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Быстрый обзор MVP: эндпоинты, модели, стартовые данные и готовность систем. Всё, что нужно для запуска.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <EndpointsCard {...demoEndpoints} />
        </Grid>
        <Grid item xs={12} md={6}>
          <ModelsCard models={demoModels} />
        </Grid>
      </Grid>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <InitialDataCard {...demoInitialData} />
        </Grid>
        <Grid item xs={12} md={6}>
          <ContentOverviewCard overview={{ ...demoContentOverview, period }} />
        </Grid>
      </Grid>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <TextVersionStateCard state={demoTextState} />
        </Grid>
        <Grid item xs={12} md={6}>
          <MainUIDataCard data={demoUIData} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default MVPContentPage


