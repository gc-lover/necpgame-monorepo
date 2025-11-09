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
import DisplaySettingsIcon from '@mui/icons-material/DisplaySettings'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { LoginScreenCard } from '../components/LoginScreenCard'
import { ServerListCard } from '../components/ServerListCard'
import { CharacterCreationFlowCard } from '../components/CharacterCreationFlowCard'
import { AppearanceOptionsCard } from '../components/AppearanceOptionsCard'
import { CharacterSelectCard } from '../components/CharacterSelectCard'
import { HUDOverviewCard } from '../components/HUDOverviewCard'
import { UIFeaturesCard } from '../components/UIFeaturesCard'
import { UISettingsCard } from '../components/UISettingsCard'

const uiChannels = ['ALL', 'LOGIN', 'CHARACTER', 'HUD', 'SETTINGS'] as const

const demoLogin = {
  title: 'Night City Login Portal',
  subtitle: 'Connect to the NC Grid',
  callToAction: 'Initiate Session',
  background: 'neon-district',
  rotatingTips: ['Maintain secure implants', 'Night Market opens at 22:00', 'Prime servers at 96% uptime'],
}

const demoServers = [
  {
    serverId: 'srv-eu-1',
    name: 'EU Nightfall',
    region: 'EU-West',
    population: 'HIGH' as const,
    ping: 42,
    status: 'ONLINE' as const,
  },
  {
    serverId: 'srv-na-2',
    name: 'NA Afterlife',
    region: 'NA-East',
    population: 'MEDIUM' as const,
    ping: 64,
    status: 'ONLINE' as const,
  },
  {
    serverId: 'srv-asia-1',
    name: 'Tokyo Pulse',
    region: 'Asia',
    population: 'FULL' as const,
    ping: 118,
    status: 'QUEUE' as const,
  },
]

const demoFlow = {
  totalSteps: 6,
  estimatedMinutes: 12,
  tutorialEnabled: true,
  steps: [
    { id: 'origin', name: 'Origin Story', description: 'Pick life path', mandatory: true },
    { id: 'role', name: 'Role Selection', description: 'Choose class', mandatory: true },
    { id: 'appearance', name: 'Appearance', description: 'Customize avatar', mandatory: false },
    { id: 'skills', name: 'Skill Allocation', description: 'Distribute attribute points', mandatory: true },
  ],
}

const demoAppearance = {
  totalCategories: 14,
  dnaLocking: true,
  categories: [
    { category: 'Hair', options: 48, presets: 12 },
    { category: 'Cyberware', options: 32, presets: 9 },
    { category: 'Voice', options: 6, presets: 6 },
  ],
}

const demoSelect = {
  maxSlots: 6,
  characters: [
    {
      characterId: 'char-v-01',
      name: 'V',
      className: 'Netrunner',
      level: 32,
      location: 'Watson · Megabuilding H10',
      lastPlayed: '2h ago',
    },
    {
      characterId: 'char-pan-02',
      name: 'Panam',
      className: 'Nomad Sharpshooter',
      level: 24,
      location: 'Badlands Convoy Route',
      lastPlayed: '8h ago',
    },
  ],
}

const demoHUD = {
  latencyMs: 28,
  widgetCount: 7,
  widgets: [
    { widget: 'Combat Feed', enabled: true, position: 'top-left', priority: 1 },
    { widget: 'Squad Panel', enabled: true, position: 'left', priority: 2 },
    { widget: 'Minimap', enabled: true, position: 'top-right', priority: 1 },
    { widget: 'Extraction Timer', enabled: true, position: 'bottom-right', priority: 2 },
  ],
}

const demoFeatures = [
  {
    id: 'feature-hud-custom',
    name: 'HUD Composer',
    description: 'Drag and drop widgets with snap-grid',
    unlocked: true,
    module: 'hud',
  },
  {
    id: 'feature-squad-ui',
    name: 'Squad Control Overlay',
    description: 'Issue squad commands via radial UI',
    unlocked: true,
    module: 'hud',
  },
  {
    id: 'feature-stream',
    name: 'Stream Safe Mode',
    description: 'Hide sensitive data when broadcasting',
    unlocked: false,
    module: 'settings',
  },
]

const demoSettings = {
  presetName: 'Night Ops',
  brightness: 62,
  saturation: 74,
  accessibility: ['Colorblind Mode', 'Large Fonts'],
  settings: [
    { key: 'hud.scale', label: 'HUD Scale', value: 0.92 },
    { key: 'hud.opacity', label: 'HUD Opacity', value: '80%' },
    { key: 'notifications.mode', label: 'Notifications', value: 'Minimal' },
    { key: 'controller.hints', label: 'Controller Hints', value: true },
  ],
}

export const UISystemsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [channelFilter, setChannelFilter] = useState<(typeof uiChannels)[number]>('ALL')
  const [showExperimental, setShowExperimental] = useState(true)

  const filteredFeatures = useMemo(
    () => (channelFilter === 'ALL' ? demoFeatures : demoFeatures.filter((feature) => feature.module === channelFilter.toLowerCase())),
    [channelFilter],
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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info.main">
        UI Systems Hub
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Управление login, servers, creation, HUD и настройками.
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
        onChange={(event) => setChannelFilter(event.target.value as typeof uiChannels[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {uiChannels.map((channel) => (
          <MenuItem key={channel} value={channel} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {channel}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={showExperimental} onChange={(event) => setShowExperimental(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Experimental UI</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Actions
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Синхронизировать настройки
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Открыть HUD Composer
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Экспорт пресета
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <UISettingsCard settings={demoSettings} />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Экспериментальные фичи доступны: {showExperimental ? 'активированы' : 'отключены'}.
      </Alert>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <DisplaySettingsIcon sx={{ fontSize: '1.4rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          UI Systems Command Center
        </Typography>
        <Chip label="technical" size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Контролируй все UX потоки: login, выбор сервера, создание персонажа, HUD и настройки. База для SPA интерфейса, всё умещается в один экран.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <LoginScreenCard data={demoLogin} />
        </Grid>
        <Grid item xs={12} md={6}>
          <ServerListCard servers={demoServers} />
        </Grid>
        <Grid item xs={12} md={6}>
          <CharacterCreationFlowCard flow={demoFlow} />
        </Grid>
        <Grid item xs={12} md={6}>
          <AppearanceOptionsCard options={demoAppearance} />
        </Grid>
        <Grid item xs={12} md={6}>
          <CharacterSelectCard data={demoSelect} />
        </Grid>
        <Grid item xs={12} md={6}>
          <HUDOverviewCard hud={demoHUD} />
        </Grid>
        <Grid item xs={12}>
          <UIFeaturesCard features={filteredFeatures} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default UISystemsPage


