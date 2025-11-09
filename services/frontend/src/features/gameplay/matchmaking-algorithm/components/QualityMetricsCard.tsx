import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import InsightsIcon from '@mui/icons-material/Insights'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { QualityMetric } from '../types'

const statusColor: Record<QualityMetric['status'], string> = {
  OK: '#05ffa1',
  WARN: '#fef86c',
  ALERT: '#ff2a6d',
}

export interface QualityMetricsCardProps {
  metrics: QualityMetric[]
}

export const QualityMetricsCard: React.FC<QualityMetricsCardProps> = ({ metrics }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <InsightsIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Match Quality KPIs
        </Typography>
      </Box>
      {metrics.slice(0, 4).map((metric) => (
        <Box key={metric.name} display="flex" alignItems="center" gap={0.4}>
          <Chip
            label={metric.status}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${statusColor[metric.status]}`, color: statusColor[metric.status] }}
          />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            {metric.name}: target {metric.target}, current {metric.current}
          </Typography>
        </Box>
      ))}
    </Stack>
  </CompactCard>
)

export default QualityMetricsCard


