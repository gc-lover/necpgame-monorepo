import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TimelineIcon from '@mui/icons-material/Timeline'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { TickMetric } from '../types'

export interface TickRateChartProps {
  metrics: TickMetric[]
}

export const TickRateChart: React.FC<TickRateChartProps> = ({ metrics }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TimelineIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Tick Duration
        </Typography>
      </Box>
      {metrics.slice(0, 4).map((metric) => (
        <ProgressBar
          key={metric.timestamp}
          value={Math.min(100, (metric.tickDurationMs / 60) * 100)}
          compact
          color={metric.tickDurationMs > 50 ? 'pink' : 'cyan'}
          label={`${metric.timestamp}`}
          customText={`${metric.tickDurationMs}ms`}
        />
      ))}
    </Stack>
  </CompactCard>
)

export default TickRateChart


