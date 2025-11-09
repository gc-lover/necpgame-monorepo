import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import TimelineIcon from '@mui/icons-material/Timeline'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { TravelEvent } from '../types'

export interface TravelEventsPeriodCardProps {
  period: {
    period: string
    eraCharacteristics: Record<string, string>
    events: TravelEvent[]
  }
}

export const TravelEventsPeriodCard: React.FC<TravelEventsPeriodCardProps> = ({ period }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TimelineIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Era Overview · {period.period}
        </Typography>
        <Chip label={`${period.events.length} events`} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      {Object.entries(period.eraCharacteristics).map(([key, value]) => (
        <Typography key={key} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {key}: {value}
        </Typography>
      ))}
    </Stack>
  </CompactCard>
)

export default TravelEventsPeriodCard


