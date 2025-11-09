import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import EventIcon from '@mui/icons-material/Event'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface RomanceEventSummary {
  eventId: string
  name: string
  stage: string
  description: string
  location: string
  durationMinutes: number
  affectionImpact: {
    min: number
    max: number
  }
  requiredAffection: number
}

export interface RomanceEventCardProps {
  event: RomanceEventSummary
}

export const RomanceEventCard: React.FC<RomanceEventCardProps> = ({ event }) => (
  <CompactCard color="cyan" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <EventIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {event.name}
          </Typography>
        </Box>
        <Chip
          label={event.stage}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {event.description}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Location: {event.location} · Duration: {event.durationMinutes}m
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Affection: {event.affectionImpact.min} → {event.affectionImpact.max} · Required {event.requiredAffection}+
      </Typography>
    </Stack>
  </CompactCard>
)

export default RomanceEventCard


