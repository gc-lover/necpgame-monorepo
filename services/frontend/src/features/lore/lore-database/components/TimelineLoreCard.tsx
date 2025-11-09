import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TimelineIcon from '@mui/icons-material/Timeline'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface TimelineEventSummary {
  year: number
  title: string
  impact: string
}

export interface TimelineLoreSummary {
  arc: string
  eraRange: string
  highlightEvents: TimelineEventSummary[]
}

export interface TimelineLoreCardProps {
  timeline: TimelineLoreSummary
}

export const TimelineLoreCard: React.FC<TimelineLoreCardProps> = ({ timeline }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TimelineIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {timeline.arc}
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Era: {timeline.eraRange}
      </Typography>
      <Stack spacing={0.2}>
        {timeline.highlightEvents.slice(0, 4).map((event) => (
          <Typography key={`${timeline.arc}-${event.year}-${event.title}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {event.year}: {event.title} — {event.impact}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default TimelineLoreCard


