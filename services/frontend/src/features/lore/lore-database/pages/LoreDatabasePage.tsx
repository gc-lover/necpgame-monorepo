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
import { CityLoreCard } from '../components/CityLoreCard'
import { FactionLoreCard } from '../components/FactionLoreCard'
import { TechnologyLoreCard } from '../components/TechnologyLoreCard'
import { TimelineLoreCard } from '../components/TimelineLoreCard'
import { EventLoreCard } from '../components/EventLoreCard'
import { CultureLoreCard } from '../components/CultureLoreCard'
import { CombatAbilityCard } from '../components/CombatAbilityCard'
import { EnemyAICard } from '../components/EnemyAICard'

const loreChannels = ['ALL', 'CITIES', 'FACTIONS', 'TECH', 'TIMELINE', 'EVENTS', 'CULTURE', 'COMBAT'] as const

const demoCity = {
  name: 'Night City',
  region: 'North America',
  population: '8.1M',
  controllingFaction: 'CityNet Council · Arasaka influence',
  dangerLevel: 'CRITICAL' as const,
  timeline: [
    { year: 2023, event: 'Nuclear attack on Arasaka Tower' },
    { year: 2077, event: 'V arrives, Afterlife shifts power' },
    { year: 2084, event: 'Blackwall breach contained by Netwatch' },
  ],
}

const demoFaction = {
  name: 'Tyger Claws',
  type: 'GANG' as const,
  region: 'Japantown',
  influence: 0.74,
  foundedYear: 2070,
  keywords: ['Katanas', 'Braindance', 'Yakuza ties'],
}

const demoTechnology = {
  name: 'Blackwall Net Architecture',
  eraIntroduced: 2063,
  riskLevel: 87,
  description: 'AI firewall separating Old Net from the resterored network. Controlled by Netwatch.',
  keySystems: ['AI sentry nodes', 'Ghost trace detection', 'Rogue AI quarantine'],
}

const demoTimeline = {
  arc: 'Fifth Corporate War',
  eraRange: '2069-2077',
  highlightEvents: [
    { year: 2071, title: 'Netwatch vs. Rogue AIs', impact: 'Escalating digital warfare' },
    { year: 2074, title: 'Orbital strikes authorized', impact: 'Megacorps bypass local law' },
    { year: 2077, title: 'Night City cataclysm', impact: 'Arasaka tower detonation' },
  ],
}

const demoEvent = {
  name: 'Fifth Corporate War',
  years: '2069-2077',
  participants: ['Arasaka', 'Militech', 'Netwatch', 'Alt Cunningham'],
  outcome: 'Stalemate, fragmentation of megacorp power',
  phases: [
    { phase: 'Net escalation', description: 'Old Net becomes battle ground for AI weaponry.' },
    { phase: 'City Annihilation', description: 'Orbital strikes devastate Night City skyline.' },
  ],
}

const demoCulture = {
  theme: 'Cyberpunk Street Culture',
  influences: ['Synthwave', 'Street samurai', 'Megacorp propaganda'],
  iconicMedia: ['Samizdat Braindance Vol.3', 'Afterlife Pirate Radio'],
  slangTerms: ['chrome-junkie', 'flatline', 'edger', 'brain-burn'],
}

const demoAbility = {
  name: 'Overclocked Synapse Burst',
  category: 'ACTIVE' as const,
  description: 'Unleash EMP surge that stuns cyberpsycho targets within 12m.',
  cooldown: '35s',
  synergy: 'Combos with Netrunner breach for +20% duration.',
}

const demoEnemy = {
  name: 'Sandevistan Ronin AI',
  tier: 'ELITE' as const,
  aggression: 92,
  tactics: ['Blade rush', 'Bullet time parry', 'Smoke screen escape'],
  weaknesses: ['Thermal overload', 'EMP disruption'],
}

export const LoreDatabasePage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [channelFilter, setChannelFilter] = useState<(typeof loreChannels)[number]>('ALL')
  const [focusNightCity, setFocusNightCity] = useState<boolean>(true)

  const visibleSections = useMemo(() => {
    if (channelFilter === 'CITIES') return ['city']
    if (channelFilter === 'FACTIONS') return ['faction']
    if (channelFilter === 'TECH') return ['technology']
    if (channelFilter === 'TIMELINE') return ['timeline']
    if (channelFilter === 'EVENTS') return ['event']
    if (channelFilter === 'CULTURE') return ['culture']
    if (channelFilter === 'COMBAT') return ['combat', 'enemy']
    return ['city', 'faction', 'technology', 'timeline', 'event', 'culture', 'combat', 'enemy']
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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info.main">
        Lore Database
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        51+ документов лора: города, фракции, технологии, войны корпораций.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Filters
      </Typography>
      <TextField
        select
        size="small"
        label="Section"
        value={channelFilter}
        onChange={(event) => setChannelFilter(event.target.value as typeof loreChannels[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {loreChannels.map((channel) => (
          <MenuItem key={channel} value={channel} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {channel}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={focusNightCity} onChange={(event) => setFocusNightCity(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Night City focus</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Actions
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Сгенерировать PDF досье
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Экспортировать в Codex
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Обновить с Brain Docs
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Focus: {focusNightCity ? 'Night City + World cities' : 'Global lore feed'} · Sources synced hourly.
      </Alert>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <MenuBookIcon sx={{ fontSize: '1.4rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Lore Database Command Center
        </Typography>
        <Chip label="lore" size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Доступ к городам, фракциям, технологиям, таймлайнам и боевым данным. Всё в одном SPA, оптимизировано под 3-колоночную сетку.
      </Alert>
      <Box display="flex" gap={0.5}>
        <Chip label="Cities 6" size="small" sx={{ fontSize: cyberpunkTokens.fonts.xs }} />
        <Chip label="Factions 30" size="small" sx={{ fontSize: cyberpunkTokens.fonts.xs }} />
        <Chip label="Technology 3" size="small" sx={{ fontSize: cyberpunkTokens.fonts.xs }} />
        <Chip label="Events 3" size="small" sx={{ fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Grid container spacing={1}>
        {visibleSections.includes('city') && (
          <Grid item xs={12} md={6}>
            <CityLoreCard city={demoCity} />
          </Grid>
        )}
        {visibleSections.includes('faction') && (
          <Grid item xs={12} md={6}>
            <FactionLoreCard faction={demoFaction} />
          </Grid>
        )}
        {visibleSections.includes('technology') && (
          <Grid item xs={12} md={6}>
            <TechnologyLoreCard technology={demoTechnology} />
          </Grid>
        )}
        {visibleSections.includes('timeline') && (
          <Grid item xs={12} md={6}>
            <TimelineLoreCard timeline={demoTimeline} />
          </Grid>
        )}
        {visibleSections.includes('event') && (
          <Grid item xs={12} md={6}>
            <EventLoreCard event={demoEvent} />
          </Grid>
        )}
        {visibleSections.includes('culture') && (
          <Grid item xs={12} md={6}>
            <CultureLoreCard culture={demoCulture} />
          </Grid>
        )}
        {visibleSections.includes('combat') && (
          <Grid item xs={12} md={6}>
            <CombatAbilityCard ability={demoAbility} />
          </Grid>
        )}
        {visibleSections.includes('enemy') && (
          <Grid item xs={12} md={6}>
            <EnemyAICard enemy={demoEnemy} />
          </Grid>
        )}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default LoreDatabasePage

