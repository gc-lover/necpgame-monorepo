import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import FavoriteIcon from '@mui/icons-material/Favorite'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface LoyaltyTier {
  tier: string
  benefits: string
  requiredPoints: number
  unlocked: boolean
}

export interface LoyaltyProgramCardProps {
  programName: string
  currentPoints: number
  nextTierPoints: number
  tiers: LoyaltyTier[]
}

export const LoyaltyProgramCard: React.FC<LoyaltyProgramCardProps> = ({ programName, currentPoints, nextTierPoints, tiers }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <FavoriteIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Loyalty — {programName}
        </Typography>
      </Box>
      <ProgressBar
        value={Math.min(100, (currentPoints / Math.max(nextTierPoints, 1)) * 100)}
        label="Next tier"
        color="pink"
        compact
        customText={`${currentPoints}/${nextTierPoints} pts`}
      />
      <Stack spacing={0.2}>
        {tiers.map((tier) => (
          <Typography
            key={tier.tier}
            variant="caption"
            fontSize={cyberpunkTokens.fonts.xs}
            color={tier.unlocked ? 'success.main' : 'text.secondary'}
          >
            {tier.unlocked ? '✓' : '•'} {tier.tier} — {tier.benefits} ({tier.requiredPoints} pts)
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default LoyaltyProgramCard

