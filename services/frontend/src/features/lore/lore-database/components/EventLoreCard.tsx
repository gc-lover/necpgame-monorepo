import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import CrisisAlertIcon from '@mui/icons-material/CrisisAlert'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface EventPhaseSummary {
  phase: string
  description: string
}

export interface EventLoreSummary {
  name: string
  years: string
  participants: string[]
  outcome: string
  phases: EventPhaseSummary[]
}

export interface EventLoreCardProps {
  event: EventLoreSummary
}

export const EventLoreCard: React.FC<EventLoreCardProps> = ({ event }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <CrisisAlertIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {event.name}
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Years: {event.years}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Outcome: {event.outcome}
      </Typography>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        {event.participants.slice(0, 4).map((participant) => (
          <Typography key={participant} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            #{participant}
          </Typography>
        ))}
      </Box>
      <Stack spacing={0.1}>
        {event.phases.slice(0, 2).map((phase) => (
          <Typography key={phase.phase} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {phase.phase}: {phase.description}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default EventLoreCard


