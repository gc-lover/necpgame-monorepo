import React from 'react'
import { Typography, Stack, Box, Chip, LinearProgress } from '@mui/material'
import AccountBalanceIcon from '@mui/icons-material/AccountBalance'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface PortfolioAnalyticsCardProps {
  characterId?: string
  totalValue?: number
  totalInvested?: number
  profitLoss?: number
  roiPercent?: number
  diversification?: Record<string, number>
  topPerformers?: { itemName?: string; roi?: number }[]
}

export const PortfolioAnalyticsCard: React.FC<PortfolioAnalyticsCardProps> = ({
  characterId,
  totalValue = 0,
  totalInvested = 0,
  profitLoss = 0,
  roiPercent = 0,
  diversification = {},
  topPerformers = [],
}) => (
  <CompactCard color="green" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <AccountBalanceIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Portfolio Analytics
          </Typography>
        </Box>
        {characterId && (
          <Chip
            label={characterId}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        )}
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Total Value: {totalValue.toLocaleString()}¥ | Profit: {profitLoss.toLocaleString()}¥
      </Typography>
      <Stack spacing={0.2}>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          ROI: {roiPercent.toFixed(1)}%
        </Typography>
        <LinearProgress
          variant="determinate"
          value={Math.min(Math.max(roiPercent + 50, 0), 100)}
          sx={{ height: 4, borderRadius: 1, bgcolor: 'rgba(255,255,255,0.08)' }}
        />
      </Stack>
      {Object.keys(diversification).length > 0 && (
        <Stack spacing={0.1}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Diversification
          </Typography>
          {Object.entries(diversification).map(([sector, percent]) => (
            <Typography key={sector} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • {sector}: {percent}%
            </Typography>
          ))}
        </Stack>
      )}
      {topPerformers.length > 0 && (
        <Stack spacing={0.1}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Top Performers
          </Typography>
          {topPerformers.slice(0, 3).map((asset, index) => (
            <Typography key={`${asset.itemName}-${index}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • {asset.itemName ?? 'Asset'}: {asset.roi ?? 0}% ROI
            </Typography>
          ))}
        </Stack>
      )}
    </Stack>
  </CompactCard>
)

export default PortfolioAnalyticsCard

