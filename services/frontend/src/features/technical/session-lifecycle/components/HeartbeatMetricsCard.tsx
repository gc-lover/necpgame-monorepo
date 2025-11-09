import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import FavoriteIcon from '@mui/icons-material/Favorite'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { HeartbeatMetric } from '../types'

export interface HeartbeatMetricsCardProps {
  metrics: HeartbeatMetric[]
}

export const HeartbeatMetricsCard: React.FC<HeartbeatMetricsCardProps> = ({ metrics }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <FavoriteIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Heartbeat Timeline
        </Typography>
      </Box>
      {metrics.slice(0, 4).map((metric) => (
        <ProgressBar
          key={metric.timestamp}
          value={Math.min(100, (metric.latencyMs / 1000) * 100)}
          compact
          color={metric.warning ? 'pink' : 'cyan'}
          label={`${metric.timestamp} • ${metric.activity}`}
          customText={`${metric.latencyMs} ms${metric.warning ? ` • ${metric.warning}` : ''}`}
        />
      ))}
      {metrics.length === 0 && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Heartbeat данных нет.
        </Typography>
      )}
    </Stack>
  </CompactCard>
)

export default HeartbeatMetricsCard


