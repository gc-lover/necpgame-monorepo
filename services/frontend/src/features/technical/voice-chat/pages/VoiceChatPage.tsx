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
import SurroundSoundIcon from '@mui/icons-material/SurroundSound'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { VoiceChannelCard } from '../components/VoiceChannelCard'
import { VoiceParticipantCard } from '../components/VoiceParticipantCard'
import { VoiceControlsCard } from '../components/VoiceControlsCard'
import { SpatialAudioCard } from '../components/SpatialAudioCard'
import { VoiceChannelSettingsCard } from '../components/VoiceChannelSettingsCard'
import { VoiceQualityCard } from '../components/VoiceQualityCard'

const channelTypes = ['party', 'guild', 'raid', 'proximity', 'custom'] as const
const ownership = ['player', 'party', 'guild', 'raid'] as const

const channels = [
  {
    channelId: 'voice-9234',
    channelName: 'Valentinos HQ',
    channelType: 'guild' as const,
    owner: 'Guild Valentinos',
    isActive: true,
    participants: 18,
    maxParticipants: 32,
  },
  {
    channelId: 'voice-5521',
    channelName: 'Night City Raid Prep',
    channelType: 'raid' as const,
    owner: 'Raid Alpha',
    isActive: true,
    participants: 24,
    maxParticipants: 40,
  },
]

const participants = [
  {
    playerId: 'player-5510',
    displayName: 'NovaRunner',
    role: 'leader' as const,
    muted: false,
    deafened: false,
    speaking: true,
    latencyMs: 32,
  },
  {
    playerId: 'player-7732',
    displayName: 'ChromeHealer',
    role: 'member' as const,
    muted: true,
    deafened: false,
    speaking: false,
    latencyMs: 48,
  },
]

const controlsState = {
  inputDevice: 'Rogue Mk.IV Microphone',
  outputDevice: 'Arasaka Link Headset',
  noiseSuppression: true,
  echoCancellation: true,
  spatialAudio: true,
}

const spatialMetrics = [
  { participantId: 'NovaRunner', angle: 15, distance: 3.2, volume: 80 },
  { participantId: 'ChromeHealer', angle: -35, distance: 5.1, volume: 55 },
  { participantId: 'AfterlifeSam', angle: 60, distance: 8.3, volume: 35 },
]

const channelSettings = {
  qualityPreset: 'medium' as const,
  autoCloseMinutes: 45,
  allowedRoles: ['captain', 'officer'],
  proximityEnabled: false,
}

const qualityProfile = {
  bitrateKbps: 96,
  packetLoss: 0.8,
  jitter: 4,
  status: 'good' as const,
}

export const VoiceChatPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [typeFilter, setTypeFilter] = useState<(typeof channelTypes)[number]>('guild')
  const [ownerFilter, setOwnerFilter] = useState<(typeof ownership)[number]>('guild')
  const [autoModeration, setAutoModeration] = useState<boolean>(true)

  const filteredChannels = useMemo(
    () => channels.filter((channel) => channel.channelType === typeFilter),
    [typeFilter],
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
        Voice Chat Ops
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Управление каналами, участниками и качеством связи.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        size="small"
        label="Channel type"
        value={typeFilter}
        onChange={(event) => setTypeFilter(event.target.value as typeof channelTypes[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {channelTypes.map((type) => (
          <MenuItem key={type} value={type} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {type}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        size="small"
        label="Owner type"
        value={ownerFilter}
        onChange={(event) => setOwnerFilter(event.target.value as typeof ownership[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {ownership.map((owner) => (
          <MenuItem key={owner} value={owner} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {owner}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={<Switch size="small" checked={autoModeration} onChange={(event) => setAutoModeration(event.target.checked)} />}
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Auto moderation</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Создать канал
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Пригласить участников
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Мутить всех участников
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <VoiceControlsCard controls={controlsState} />
      <SpatialAudioCard metrics={spatialMetrics} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <SurroundSoundIcon sx={{ fontSize: '1.4rem', color: 'secondary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Voice Chat Control Center
        </Typography>
        <Chip label={typeFilter.toUpperCase()} size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        WebRTC каналы: party, guild, raid, proximity. Управляем качеством, spatial audio и модерацией.
      </Alert>
      <Grid container spacing={1}>
        {filteredChannels.map((channel) => (
          <Grid item xs={12} md={6} key={channel.channelId}>
            <VoiceChannelCard channel={channel} />
          </Grid>
        ))}
        {participants.map((participant) => (
          <Grid item xs={12} md={6} key={participant.playerId}>
            <VoiceParticipantCard participant={participant} />
          </Grid>
        ))}
        <Grid item xs={12} md={6}>
          <VoiceChannelSettingsCard settings={channelSettings} />
        </Grid>
        <Grid item xs={12} md={6}>
          <VoiceQualityCard profile={qualityProfile} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default VoiceChatPage


