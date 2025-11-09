import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import LocationCityIcon from '@mui/icons-material/LocationCity'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface CityTimelineSnippet {
  year: number
  event: string
}

export interface CityLoreSummary {
  name: string
  region: string
  population: string
  controllingFaction: string
  dangerLevel: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  timeline: CityTimelineSnippet[]
}

const dangerColor: Record<CityLoreSummary['dangerLevel'], string> = {
  LOW: '#05ffa1',
  MEDIUM: '#fef86c',
  HIGH: '#ff9f43',
  CRITICAL: '#ff2a6d',
}

export interface CityLoreCardProps {
  city: CityLoreSummary
}

export const CityLoreCard: React.FC<CityLoreCardProps> = ({ city }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <LocationCityIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {city.name}
        </Typography>
        <Chip
          label={city.dangerLevel}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${dangerColor[city.dangerLevel]}`,
            color: dangerColor[city.dangerLevel],
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Region: {city.region} · Population: {city.population}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Control: {city.controllingFaction}
      </Typography>
      <Stack spacing={0.2}>
        {city.timeline.slice(0, 3).map((item) => (
          <Typography key={`${city.name}-${item.year}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {item.year}: {item.event}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default CityLoreCard


