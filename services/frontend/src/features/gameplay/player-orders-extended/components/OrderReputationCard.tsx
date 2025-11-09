import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import StarIcon from '@mui/icons-material/Star'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ExecutorReputationSummary {
  executorId: string
  name: string
  tier: 'BRONZE' | 'SILVER' | 'GOLD' | 'PLATINUM'
  score: number
  completedOrders: number
  cancelRate: number
  reviewsPositive: number
  reviewsNegative: number
}

const tierColor: Record<ExecutorReputationSummary['tier'], string> = {
  BRONZE: '#b08d57',
  SILVER: '#c0c0c0',
  GOLD: '#f5c518',
  PLATINUM: '#e5e4e2',
}

export interface OrderReputationCardProps {
  reputation: ExecutorReputationSummary
}

export const OrderReputationCard: React.FC<OrderReputationCardProps> = ({ reputation }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <StarIcon sx={{ fontSize: '1rem', color: '#f5c518' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {reputation.name}
          </Typography>
        </Box>
        <Chip
          label={reputation.tier}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${tierColor[reputation.tier]}`,
            color: tierColor[reputation.tier],
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Score: {reputation.score} · Completed: {reputation.completedOrders}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={reputation.cancelRate > 5 ? 'warning.main' : 'text.secondary'}>
        Cancel rate: {reputation.cancelRate}%
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Reviews: +{reputation.reviewsPositive} / -{reputation.reviewsNegative}
      </Typography>
    </Stack>
  </CompactCard>
)

export default OrderReputationCard


