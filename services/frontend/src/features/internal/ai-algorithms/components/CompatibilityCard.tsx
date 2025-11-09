import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import FavoriteIcon from '@mui/icons-material/Favorite'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface CompatibilityFactor {
  name: string
  weight: number
  contribution: number
}

export interface CompatibilitySummary {
  score: number
  recommendation: string
  stage: string
  factors: CompatibilityFactor[]
}

export interface CompatibilityCardProps {
  summary: CompatibilitySummary
}

export const CompatibilityCard: React.FC<CompatibilityCardProps> = ({ summary }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <FavoriteIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Romance Compatibility
        </Typography>
        <Chip
          label={`${summary.score}%`}
          size="small"
          color={summary.score >= 75 ? 'success' : summary.score >= 50 ? 'warning' : 'default'}
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Stage: {summary.stage}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Recommendation: {summary.recommendation}
      </Typography>
      <Stack spacing={0.2}>
        {summary.factors.slice(0, 4).map((factor) => (
          <Box key={factor.name} display="flex" alignItems="center" gap={0.6}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary" sx={{ flexGrow: 1 }}>
              {factor.name}
            </Typography>
            <ProgressBar value={factor.contribution * 100} compact color="pink" customText={`${Math.round(factor.weight * 100)}%`} />
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default CompatibilityCard


