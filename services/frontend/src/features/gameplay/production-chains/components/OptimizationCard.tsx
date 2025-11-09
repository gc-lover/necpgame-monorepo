import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import AutoFixHighIcon from '@mui/icons-material/AutoFixHigh'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface OptimizationTip {
  label: string
  value?: string
}

export interface OptimizationCardProps {
  goal: string
  expectedImprovement: string
  tips?: OptimizationTip[]
}

export const OptimizationCard: React.FC<OptimizationCardProps> = ({ goal, expectedImprovement, tips = [] }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <AutoFixHighIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            AI Optimization
          </Typography>
        </Box>
        <Chip
          label={expectedImprovement}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Goal: {goal}
      </Typography>
      <Stack spacing={0.2}>
        {tips.length === 0 ? (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Нет рекомендаций. Запустите оптимизацию.
          </Typography>
        ) : (
          tips.slice(0, 4).map((tip) => (
            <Typography key={tip.label} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • {tip.label}: {tip.value ?? '—'}
            </Typography>
          ))
        )}
      </Stack>
    </Stack>
  </CompactCard>
)

export default OptimizationCard


