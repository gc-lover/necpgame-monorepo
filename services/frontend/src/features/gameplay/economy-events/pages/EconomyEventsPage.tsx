/**
 * EconomyEventsPage — монитор экономических событий
 */

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
import CrisisAlertIcon from '@mui/icons-material/CrisisAlert'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { EconomyEventCard } from '../components/EconomyEventCard'
import { EconomyImpactCard } from '../components/EconomyImpactCard'
import { EventHistoryCard } from '../components/EventHistoryCard'
import { EventPredictionsCard } from '../components/EventPredictionsCard'

const typeOptions = ['ALL', 'CRISIS', 'INFLATION', 'RECESSION', 'BOOM', 'TRADE_WAR', 'CORPORATE', 'COMMODITY']
const severityOptions = ['ALL', 'MINOR', 'MODERATE', 'MAJOR', 'CATASTROPHIC']

const demoEvents = [
  {
    eventId: 'evt-neo-882',
    name: 'Night Market Trade War',
    type: 'TRADE_WAR' as const,
    severity: 'MAJOR' as const,
    startDate: '2077-11-01',
    endDate: null,
    isActive: true,
    affectedRegions: ['Night City', 'Pacifica'],
    affectedSectors: ['Logistics', 'Cyberware'],
  },
  {
    eventId: 'evt-neo-877',
    name: 'Arasaka Innovation Boom',
    type: 'BOOM' as const,
    severity: 'MODERATE' as const,
    startDate: '2077-10-25',
    endDate: '2077-11-05',
    isActive: false,
    affectedRegions: ['Night City'],
    affectedSectors: ['Cyberware', 'Finance'],
  },
]

const demoImpact = {
  timestamp: '2077-11-07T20:20:00Z',
  activeEventsCount: 3,
  overallMarketHealth: 'WEAK' as const,
  priceIndexChange: -1.85,
  sectorImpacts: {
    logistics: -0.12,
    cyberware: 0.18,
    weapons: -0.05,
    food: 0.03,
  },
}

const demoHistory = [
  {
    eventId: 'evt-neo-860',
    name: 'Fuel Shortage',
    type: 'COMMODITY',
    severity: 'MAJOR',
    startDate: '2077-10-10',
    endDate: '2077-10-20',
    durationDays: 10,
    impactSummary: 'Logistics +35%, aerodyne routes restricted',
  },
]

const demoPredictions = [
  {
    predictedType: 'CRISIS',
    probability: 48,
    timeframe: 'Next 5-7 days',
    notes: 'Currency liquidity dropping across Night City banks',
  },
  {
    predictedType: 'CORPORATE',
    probability: 35,
    timeframe: 'Next 10 days',
    notes: 'Rumors of major M&A in biotech sector',
  },
]

export const EconomyEventsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [typeFilter, setTypeFilter] = useState<string>('ALL')
  const [severityFilter, setSeverityFilter] = useState<string>('ALL')

  const filteredEvents = demoEvents.filter((event) => {
    const typeMatch = typeFilter === 'ALL' || event.type === typeFilter
    const severityMatch = severityFilter === 'ALL' || event.severity === severityFilter
    return typeMatch && severityMatch
  })

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
        Economy Events
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Real-time crises, booms and trade wars. Управляй рисками и возможностями.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        label="Type"
        size="small"
        value={typeFilter}
        onChange={(event) => setTypeFilter(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {typeOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        label="Severity"
        size="small"
        value={severityFilter}
        onChange={(event) => setSeverityFilter(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {severityOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Создать событие
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Завершить событие
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Экспорт данных
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <EconomyImpactCard impact={demoImpact} />
      <EventHistoryCard history={demoHistory} />
      <EventPredictionsCard predictions={demoPredictions} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <CrisisAlertIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Economy Event Dashboard
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Мониторинг кризисов и торговых войн. Следите за влиянием на цены и открывайте возможности.
      </Alert>
      <Grid container spacing={1}>
        {filteredEvents.map((event) => (
          <Grid key={event.eventId} item xs={12} md={6}>
            <EconomyEventCard event={event} />
          </Grid>
        ))}
        {filteredEvents.length === 0 && (
          <Grid item xs={12}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Нет событий под выбранные фильтры
            </Typography>
          </Grid>
        )}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default EconomyEventsPage

