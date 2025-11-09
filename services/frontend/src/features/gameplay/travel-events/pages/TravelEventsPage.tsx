import React, { useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
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
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import DirectionsWalkIcon from '@mui/icons-material/DirectionsWalk';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { TravelEventCard } from '../components/TravelEventCard';
import { useGameState } from '@/features/game/hooks/useGameState';
import { TravelEventsPeriodCard } from '../components/TravelEventsPeriodCard';
import { TravelEventGenerationCard } from '../components/TravelEventGenerationCard';
import { TravelEncounterCard } from '../components/TravelEncounterCard';

const periods = ['2030-2045', '2045-2060', '2060-2077', '2077', '2078-2093'] as const;
const locationTypes = ['CITY', 'BADLANDS', 'HIGHWAY', 'CHECKPOINT', 'BORDER'] as const;
const transportModes = ['ON_FOOT', 'VEHICLE', 'FAST_TRAVEL'] as const;

const demoEvents = [
  {
    eventId: 'travel-scv-001',
    name: 'Badlands scav ambush',
    period: '2077',
    locationTypes: ['BADLANDS'],
    triggerChance: 0.32,
    description: 'Scavengers deploy spike strip, demand chrome tribute.',
    choices: ['Fight back', 'Pay tribute', 'Call Nomad escort'],
    outcomes: ['Combat encounter', 'Lose eurodollars', 'Gain Nomad favor'],
  },
  {
    eventId: 'travel-net-002',
    name: 'Netwatch checkpoint scan',
    period: '2060-2077',
    locationTypes: ['CHECKPOINT'],
    triggerChance: 0.46,
    description: 'Netwatch scans for illegal cyberware; bribery risk',
    choices: ['Comply', 'Hack console', 'Bribe officer'],
    outcomes: ['Pass safely', 'Launch mini-hack', 'Lose credits'],
  },
];

const demoPeriod = {
  period: '2077',
  eraCharacteristics: {
    turmoil: 'Corporate war spillover, roaming trauma teams',
    safeRoutes: ['Maglev to Watson', 'Afterlife shuttle'],
  },
  events: demoEvents,
};

const demoGeneration = {
  transportMode: 'VEHICLE',
  origin: 'Kabuki · Night City',
  destination: 'Nomad Camp · Badlands',
  timeOfDay: 'Night cycle 02:30',
  lastEncounter: 'Highway chase with Wraiths',
  eventGenerated: true,
  event: demoEvents[0],
};

const demoEncounter = {
  period: '2078-2093',
  mode: 'FAST_TRAVEL',
  riskLevel: 0.18,
  modifiers: ['City security HIGH', 'Blackwall breach nearby'],
  rewards: ['Hidden market access', 'Faction favor +10'],
};

export const TravelEventsPage: React.FC = () => {
  const navigate = useNavigate();
  const { selectedCharacterId } = useGameState();
  const [periodFilter, setPeriodFilter] = useState<(typeof periods)[number]>('2077');
  const [locationFilter, setLocationFilter] = useState<(typeof locationTypes)[number]>('BADLANDS');
  const [transportFilter, setTransportFilter] = useState<(typeof transportModes)[number]>('VEHICLE');
  const [autoGenerate, setAutoGenerate] = useState(true);

  const filteredEvents = useMemo(
    () => demoEvents.filter((event) => event.period === periodFilter),
    [periodFilter],
  );

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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info">
        Travel Events
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Random travel generator по эпохам
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Период
      </Typography>
      <TextField
        select
        size="small"
        label="Period"
        value={periodFilter}
        onChange={(event) => setPeriodFilter(event.target.value as typeof periods[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {periods.map((period) => (
          <MenuItem key={period} value={period} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {period}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        size="small"
        label="Location"
        value={locationFilter}
        onChange={(event) => setLocationFilter(event.target.value as typeof locationTypes[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {locationTypes.map((location) => (
          <MenuItem key={location} value={location} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {location}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        size="small"
        label="Transport"
        value={transportFilter}
        onChange={(event) => setTransportFilter(event.target.value as typeof transportModes[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {transportModes.map((mode) => (
          <MenuItem key={mode} value={mode} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {mode}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={autoGenerate} onChange={(event) => setAutoGenerate(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Auto-generate</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Сгенерировать событие
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Запросить Nomad эскорт
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Travel лог
      </CyberpunkButton>
    </Stack>
  );

  const rightPanel = (
    <Stack spacing={2}>
      <TravelEventGenerationCard {...demoGeneration} autoGenerate={autoGenerate} />
      <TravelEncounterCard encounter={demoEncounter} />
    </Stack>
  );

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <DirectionsWalkIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Travel Events
        </Typography>
        <Chip label={`Era ${periodFilter}`} size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Travel events зависят от эпохи: Early Corpo Wars → корпоративные караваны, Time of the Red → Nomad конвои, Corporate Control → охрана Arasaka.
      </Alert>
      <TravelEventsPeriodCard period={demoPeriod} />
      <Grid container spacing={1}>
        {filteredEvents.map((event) => (
          <Grid item xs={12} md={6} key={event.eventId}>
            <TravelEventCard event={event} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default TravelEventsPage;

