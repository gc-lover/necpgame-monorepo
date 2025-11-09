import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TimelineIcon from '@mui/icons-material/Timeline'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ProgressionStep {
  step: number
  questId: string
  questName: string
  estimatedLevel: number
}

export interface StarterProgressionCardProps {
  progression: ProgressionStep[]
}

export const StarterProgressionCard: React.FC<StarterProgressionCardProps> = ({ progression }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TimelineIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Starter Progression
        </Typography>
      </Box>
      <Stack spacing={0.1}>
        {progression.slice(0, 5).map((step) => (
          <Typography key={step.questId} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            {step.step}. {step.questName} · Lv {step.estimatedLevel}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default StarterProgressionCard



