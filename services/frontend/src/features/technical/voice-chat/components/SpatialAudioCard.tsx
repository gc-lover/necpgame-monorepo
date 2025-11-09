import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import RadarIcon from '@mui/icons-material/Radar'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { SpatialAudioMetric } from '../types'

export interface SpatialAudioCardProps {
  metrics: SpatialAudioMetric[]
}

export const SpatialAudioCard: React.FC<SpatialAudioCardProps> = ({ metrics }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <RadarIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Spatial Audio
        </Typography>
      </Box>
      {metrics.slice(0, 3).map((metric) => (
        <ProgressBar
          key={metric.participantId}
          value={Math.min(100, metric.volume)}
          compact
          color="pink"
          label={`${metric.participantId} • ${Math.round(metric.angle)}°`}
          customText={`${Math.round(metric.distance)}m`}
        />
      ))}
    </Stack>
  </CompactCard>
)

export default SpatialAudioCard


