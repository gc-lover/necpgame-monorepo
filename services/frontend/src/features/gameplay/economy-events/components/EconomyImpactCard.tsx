import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface EconomyImpactSummary {
  timestamp: string
  activeEventsCount: number
  overallMarketHealth: 'STRONG' | 'STABLE' | 'WEAK' | 'CRISIS'
  priceIndexChange: number
  sectorImpacts: Record<string, number>
}

const healthColor: Record<EconomyImpactSummary['overallMarketHealth'], string> = {
  STRONG: '#05ffa1',
  STABLE: '#00f7ff',
  WEAK: '#fef86c',
  CRISIS: '#ff2a6d',
}

export interface EconomyImpactCardProps {
  impact: EconomyImpactSummary
}

export const EconomyImpactCard: React.FC<EconomyImpactCardProps> = ({ impact }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TrendingUpIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Market Impact Snapshot
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Timestamp: {impact.timestamp}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Active events: {impact.activeEventsCount}
      </Typography>
      <Typography
        variant="caption"
        fontSize={cyberpunkTokens.fonts.xs}
        sx={{ color: healthColor[impact.overallMarketHealth] }}
      >
        Market health: {impact.overallMarketHealth}
      </Typography>
      <Typography
        variant="caption"
        fontSize={cyberpunkTokens.fonts.xs}
        color={impact.priceIndexChange >= 0 ? 'success.main' : 'error.main'}
      >
        Price index: {impact.priceIndexChange >= 0 ? '+' : ''}
        {impact.priceIndexChange.toFixed(2)}%
      </Typography>
      <Stack spacing={0.2}>
        {Object.entries(impact.sectorImpacts).slice(0, 4).map(([sector, value]) => (
          <Typography key={sector} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            {sector}: {value >= 0 ? '+' : ''}
            {(value * 100).toFixed(1)}%
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default EconomyImpactCard


