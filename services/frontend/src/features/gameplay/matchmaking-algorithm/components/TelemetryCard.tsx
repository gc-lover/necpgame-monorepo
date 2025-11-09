import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TimelineIcon from '@mui/icons-material/Timeline'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { TelemetryPoint } from '../types'

export interface TelemetryCardProps {
  telemetry: TelemetryPoint[]
}

export const TelemetryCard: React.FC<TelemetryCardProps> = ({ telemetry }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TimelineIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Latency Telemetry
        </Typography>
      </Box>
      {telemetry.slice(0, 4).map((point) => (
        <ProgressBar
          key={point.label}
          value={Math.min(100, point.value)}
          compact
          color="pink"
          label={`${point.label} • P${point.percentile}`}
          customText={`${point.value} ms`}
        />
      ))}
    </Stack>
  </CompactCard>
)

export default TelemetryCard


