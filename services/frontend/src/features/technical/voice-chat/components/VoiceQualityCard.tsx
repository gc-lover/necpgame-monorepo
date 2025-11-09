import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import EqualizerIcon from '@mui/icons-material/Equalizer'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { QualityProfile } from '../types'

const statusColor: Record<QualityProfile['status'], 'success' | 'info' | 'warning' | 'error'> = {
  excellent: 'success',
  good: 'info',
  degraded: 'warning',
  critical: 'error',
}

export interface VoiceQualityCardProps {
  profile: QualityProfile
}

export const VoiceQualityCard: React.FC<VoiceQualityCardProps> = ({ profile }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <EqualizerIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Voice Quality
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={`${statusColor[profile.status]}.main`}>
        Status: {profile.status.toUpperCase()}
      </Typography>
      <ProgressBar value={Math.min(100, (profile.bitrateKbps / 128) * 100)} compact color="cyan" label="Bitrate" customText={`${profile.bitrateKbps} kbps`} />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Packet loss: {profile.packetLoss}% • Jitter: {profile.jitter} ms
      </Typography>
    </Stack>
  </CompactCard>
)

export default VoiceQualityCard


