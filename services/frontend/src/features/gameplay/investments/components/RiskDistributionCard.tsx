import React from 'react'
import { Typography, Stack, Box, LinearProgress } from '@mui/material'
import WarningIcon from '@mui/icons-material/Warning'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface RiskSlice {
  level: 'LOW' | 'MEDIUM' | 'HIGH' | 'VERY_HIGH'
  percent: number
}

const riskColor: Record<RiskSlice['level'], string> = {
  LOW: '#05ffa1',
  MEDIUM: '#00f7ff',
  HIGH: '#fef86c',
  VERY_HIGH: '#ff2a6d',
}

export interface RiskDistributionCardProps {
  distribution: RiskSlice[]
}

export const RiskDistributionCard: React.FC<RiskDistributionCardProps> = ({ distribution }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <WarningIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Risk Distribution
        </Typography>
      </Box>
      <Stack spacing={0.3}>
        {distribution.map((slice) => (
          <Box key={slice.level}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                {slice.level}
              </Typography>
              <Typography
                variant="caption"
                fontSize={cyberpunkTokens.fonts.xs}
                sx={{ color: riskColor[slice.level] }}
              >
                {slice.percent}%
              </Typography>
            </Box>
            <LinearProgress
              variant="determinate"
              value={Math.min(100, slice.percent)}
              sx={{ height: 4, borderRadius: 1, bgcolor: 'rgba(255,255,255,0.05)', mt: 0.2 }}
            />
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default RiskDistributionCard


