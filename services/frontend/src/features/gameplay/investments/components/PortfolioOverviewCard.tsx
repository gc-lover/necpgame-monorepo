import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import PieChartIcon from '@mui/icons-material/PieChart'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface PortfolioOverview {
  totalValue: number
  investedCapital: number
  unrealizedProfit: number
  roiPercent: number
}

export interface PortfolioOverviewCardProps {
  portfolio: PortfolioOverview
}

export const PortfolioOverviewCard: React.FC<PortfolioOverviewCardProps> = ({ portfolio }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <PieChartIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Portfolio Overview
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Total value: {portfolio.totalValue.toLocaleString()}¥
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Invested: {portfolio.investedCapital.toLocaleString()}¥
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="success.main">
        Unrealized P/L: {portfolio.unrealizedProfit >= 0 ? '+' : ''}
        {portfolio.unrealizedProfit.toLocaleString()}¥
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="success.main">
        ROI: {portfolio.roiPercent.toFixed(1)}%
      </Typography>
    </Stack>
  </CompactCard>
)

export default PortfolioOverviewCard


