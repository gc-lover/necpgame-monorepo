import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface DecisionOption {
  action: string
  probability: number
  rationale: string
}

export interface NPCDecisionSummary {
  context: string
  primaryAction: string
  confidence: number
  options: DecisionOption[]
}

export interface NPCDecisionCardProps {
  decision: NPCDecisionSummary
}

export const NPCDecisionCard: React.FC<NPCDecisionCardProps> = ({ decision }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <AutoAwesomeIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          NPC Decision Engine
        </Typography>
        <Chip label={`${Math.round(decision.confidence * 100)}%`} size="small" color="info" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Context: {decision.context}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Primary Action: {decision.primaryAction}
      </Typography>
      <Stack spacing={0.2}>
        {decision.options.slice(0, 3).map((option) => (
          <Typography key={option.action} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {option.action} ({Math.round(option.probability * 100)}%) — {option.rationale}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default NPCDecisionCard


