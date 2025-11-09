import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import TuneIcon from '@mui/icons-material/Tune'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ChannelSettings } from '../types'

export interface VoiceChannelSettingsCardProps {
  settings: ChannelSettings
}

export const VoiceChannelSettingsCard: React.FC<VoiceChannelSettingsCardProps> = ({ settings }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TuneIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Channel Settings
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Quality preset: {settings.qualityPreset}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Auto close: {settings.autoCloseMinutes} min
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Proximity: {settings.proximityEnabled ? 'ENABLED' : 'DISABLED'}
      </Typography>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        {settings.allowedRoles.map((role) => (
          <Chip key={role} label={role} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        ))}
      </Box>
    </Stack>
  </CompactCard>
)

export default VoiceChannelSettingsCard


