import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TimelineIcon from '@mui/icons-material/Timeline'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { TimelineEvent } from '../types'

const categoryColor: Record<TimelineEvent['category'], string> = {
  detection: '#fef86c',
  mitigation: '#05ffa1',
  communication: '#00f7ff',
  recovery: '#ff9f43',
}

export interface TimelineCardProps {
  events: TimelineEvent[]
}

export const TimelineCard: React.FC<TimelineCardProps> = ({ events }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TimelineIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Incident Timeline
        </Typography>
      </Box>
      {events.slice(0, 4).map((event) => (
        <Box key={`${event.timestamp}-${event.actor}`} display="flex" flexDirection="column" gap={0.1}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={categoryColor[event.category]}>
            {event.timestamp} • {event.category.toUpperCase()}
          </Typography>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            {event.actor}: {event.description}
          </Typography>
        </Box>
      ))}
    </Stack>
  </CompactCard>
)

export default TimelineCard


