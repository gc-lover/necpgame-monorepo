import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import FlashOnIcon from '@mui/icons-material/FlashOn'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface TriggerSummary {
  relationshipId: string
  shouldTrigger: boolean
  eventId?: string
  triggerProbability: number
  blockingReasons: string[]
}

export interface TriggerPredictionCardProps {
  trigger: TriggerSummary
}

export const TriggerPredictionCard: React.FC<TriggerPredictionCardProps> = ({ trigger }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <FlashOnIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Trigger Prediction
        </Typography>
        <Chip
          label={trigger.shouldTrigger ? 'trigger' : 'hold'}
          size="small"
          color={trigger.shouldTrigger ? 'success' : 'default'}
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Relationship: {trigger.relationshipId}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Event: {trigger.eventId ?? '—'}
      </Typography>
      <ProgressBar value={trigger.triggerProbability * 100} compact color="yellow" label="Probability" />
      {!trigger.shouldTrigger && trigger.blockingReasons.length > 0 && (
        <Stack spacing={0.2}>
          {trigger.blockingReasons.slice(0, 3).map((reason) => (
            <Typography key={reason} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              • {reason}
            </Typography>
          ))}
        </Stack>
      )}
    </Stack>
  </CompactCard>
)

export default TriggerPredictionCard


