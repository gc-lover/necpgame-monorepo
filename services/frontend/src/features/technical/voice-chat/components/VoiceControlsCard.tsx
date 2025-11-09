import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import SettingsVoiceIcon from '@mui/icons-material/SettingsVoice'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { VoiceControlsState } from '../types'

export interface VoiceControlsCardProps {
  controls: VoiceControlsState
}

export const VoiceControlsCard: React.FC<VoiceControlsCardProps> = ({ controls }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <SettingsVoiceIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Voice Controls
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Input: {controls.inputDevice}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Output: {controls.outputDevice}
      </Typography>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        <Chip label={`Noise suppr: ${controls.noiseSuppression ? 'ON' : 'OFF'}`} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        <Chip label={`Echo cancel: ${controls.echoCancellation ? 'ON' : 'OFF'}`} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        <Chip label={`Spatial: ${controls.spatialAudio ? 'ON' : 'OFF'}`} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
    </Stack>
  </CompactCard>
)

export default VoiceControlsCard


