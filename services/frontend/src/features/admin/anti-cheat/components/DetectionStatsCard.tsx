import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import RadarIcon from '@mui/icons-material/Radar'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface DetectionMetrics {
  autoBansLast24h: number
  suspiciousPatterns: number
  manualQueue: number
  falsePositivesRate: number
}

export interface DetectionStatsCardProps {
  metrics: DetectionMetrics
}

export const DetectionStatsCard: React.FC<DetectionStatsCardProps> = ({ metrics }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <RadarIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Detection Stats
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Auto bans (24h): {metrics.autoBansLast24h}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Live patterns: {metrics.suspiciousPatterns}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Manual queue: {metrics.manualQueue}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        False positives: {metrics.falsePositivesRate.toFixed(2)}%
      </Typography>
    </Stack>
  </CompactCard>
)

export default DetectionStatsCard


