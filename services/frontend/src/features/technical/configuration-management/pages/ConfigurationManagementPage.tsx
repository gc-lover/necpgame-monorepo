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
import SettingsSuggestIcon from '@mui/icons-material/SettingsSuggest'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { ServiceConfigCard } from '../components/ServiceConfigCard'
import { SecretsCard } from '../components/SecretsCard'
import { EnvironmentSummaryCard } from '../components/EnvironmentSummaryCard'
import { ReloadControlCard } from '../components/ReloadControlCard'

const services = ['matchmaking-service', 'party-service', 'voice-lobby-service'] as const
const environments = ['production', 'staging', 'night-city-lab'] as const

const serviceConfigs = [
  {
    serviceName: 'matchmaking-service',
    environment: 'production',
    version: 'v2025.11.08-rc1',
    configuration: { latencyCapMs: 90, readyCheckSeconds: 45, mmrBucketSize: 120 },
  },
  {
    serviceName: 'party-service',
    environment: 'production',
    version: 'v2025.11.07',
    configuration: { maxPartySize: 6, voiceBridge: 'voice-lobby-service', fallback: 'tokyo' },
  },
]

const secretList = [
  { secretName: 'mm-redis-password', createdAt: '2025-10-01', updatedAt: '2025-11-05' },
  { secretName: 'party-oauth-client', createdAt: '2025-07-18', updatedAt: '2025-11-03' },
  { secretName: 'voice-turn-credentials', createdAt: '2025-06-11', updatedAt: '2025-10-29' },
]

const environmentSummaries = [
  { name: 'production', services: 42, overrides: 12, driftAlerts: 1 },
  { name: 'staging', services: 37, overrides: 18, driftAlerts: 0 },
  { name: 'night-city-lab', services: 15, overrides: 9, driftAlerts: 3 },
]

const reloadStatus = {
  lastReload: '2025-11-08 04:40 NCST',
  triggeredBy: 'automation@ops',
  status: 'SUCCESS' as const,
}

export const ConfigurationManagementPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [serviceFilter, setServiceFilter] = useState<(typeof services)[number]>('matchmaking-service')
  const [environmentFilter, setEnvironmentFilter] = useState<(typeof environments)[number]>('production')
  const [autoReload, setAutoReload] = useState<boolean>(true)

  const filteredConfigs = useMemo(
    () =>
      serviceConfigs.filter(
        (config) => config.serviceName === serviceFilter && config.environment === environmentFilter,
      ),
    [serviceFilter, environmentFilter],
  )

  const environmentInfo = useMemo(
    () => environmentSummaries.find((env) => env.name === environmentFilter) ?? environmentSummaries[0],
    [environmentFilter],
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
        Config Management
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Централизованное управление конфигами, секретами и hot reload.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        size="small"
        label="Service"
        value={serviceFilter}
        onChange={(event) => setServiceFilter(event.target.value as typeof services[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {services.map((service) => (
          <MenuItem key={service} value={service} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {service}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        size="small"
        label="Environment"
        value={environmentFilter}
        onChange={(event) => setEnvironmentFilter(event.target.value as typeof environments[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {environments.map((environment) => (
          <MenuItem key={environment} value={environment} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {environment}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={autoReload} onChange={(event) => setAutoReload(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Auto reload after update</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Создать новую версию
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Экспортировать конфиг
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Управление секретами
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <EnvironmentSummaryCard summary={environmentInfo} />
      <ReloadControlCard status={{ ...reloadStatus, status: autoReload ? 'SUCCESS' : reloadStatus.status }} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <SettingsSuggestIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Configuration Command Center
        </Typography>
        <Chip label={environmentFilter} size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Управляем конфигами сервисов, шифруем секреты, отслеживаем drift. Поддерживает hot reload без рестартов.
      </Alert>
      <Grid container spacing={1}>
        {filteredConfigs.map((config) => (
          <Grid item xs={12} md={6} key={config.serviceName}>
            <ServiceConfigCard config={config} />
          </Grid>
        ))}
        <Grid item xs={12} md={6}>
          <SecretsCard secrets={secretList} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default ConfigurationManagementPage


