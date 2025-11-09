import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ProfitabilityCardProps {
  chainName: string
  profitPerCycle: number
  roiPercent: number
  cycleTime: string
  recommendations?: string[]
}

export const ProfitabilityCard: React.FC<ProfitabilityCardProps> = ({
  chainName,
  profitPerCycle,
  roiPercent,
  cycleTime,
  recommendations = [],
}) => (
  <CompactCard color="pink" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <TrendingUpIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Profitability
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Cycle: {cycleTime}
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        {chainName}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Profit / cycle: {profitPerCycle.toLocaleString()}¥
      </Typography>
      <Typography
        variant="caption"
        fontSize={cyberpunkTokens.fonts.xs}
        sx={{ color: roiPercent >= 0 ? 'success.main' : 'error.main' }}
      >
        ROI: {roiPercent.toFixed(1)}%
      </Typography>
      {recommendations.length > 0 && (
        <Stack spacing={0.2}>
          {recommendations.slice(0, 3).map((rec, index) => (
            <Typography key={`${rec}-${index}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • {rec}
            </Typography>
          ))}
        </Stack>
      )}
    </Stack>
  </CompactCard>
)

export default ProfitabilityCard


