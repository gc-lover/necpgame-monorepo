import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import RouteIcon from '@mui/icons-material/AltRoute'
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { TravelEvent } from '../types'

export interface TravelEventGenerationCardProps {
  transportMode: string
  origin: string
  destination: string
  timeOfDay: string
  lastEncounter: string
  eventGenerated: boolean
  event?: TravelEvent
  autoGenerate: boolean
}

export const TravelEventGenerationCard: React.FC<TravelEventGenerationCardProps> = ({
  transportMode,
  origin,
  destination,
  timeOfDay,
  lastEncounter,
  eventGenerated,
  event,
  autoGenerate,
}) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <RouteIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Event Generation
        </Typography>
        {autoGenerate && <AutoAwesomeIcon sx={{ fontSize: '1rem', color: 'secondary.main', ml: 'auto' }} />}
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {origin} → {destination}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Mode: {transportMode} · Time: {timeOfDay}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Last encounter: {lastEncounter}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={eventGenerated ? 'success.main' : 'text.secondary'}>
        Generated: {eventGenerated ? event?.name : 'No event'}
      </Typography>
    </Stack>
  </CompactCard>
)

export default TravelEventGenerationCard


