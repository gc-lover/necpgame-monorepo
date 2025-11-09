import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TimelineIcon from '@mui/icons-material/Timeline'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface AlgorithmMetricSummary {
  latencyMs: number
  throughputPerMin: number
  cacheHitRate: number
  queueDepth: number
  incidents24h: number
}

export interface AlgorithmMetricsCardProps {
  metrics: AlgorithmMetricSummary
}

export const AlgorithmMetricsCard: React.FC<AlgorithmMetricsCardProps> = ({ metrics }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TimelineIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Algorithm Metrics
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Latency: {metrics.latencyMs}ms · Throughput: {metrics.throughputPerMin}/min
      </Typography>
      <ProgressBar value={metrics.cacheHitRate} compact color="green" label="Cache hit rate" customText={`${metrics.cacheHitRate}%`} />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Queue depth: {metrics.queueDepth}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={metrics.incidents24h > 0 ? 'warning.main' : 'text.secondary'}>
        Incidents (24h): {metrics.incidents24h}
      </Typography>
    </Stack>
  </CompactCard>
)

export default AlgorithmMetricsCard


