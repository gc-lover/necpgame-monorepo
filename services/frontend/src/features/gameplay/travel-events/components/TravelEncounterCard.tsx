import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import TravelExploreIcon from '@mui/icons-material/TravelExplore'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface TravelEncounterSummary {
  period: string
  mode: string
  riskLevel: number
  modifiers: string[]
  rewards: string[]
}

export interface TravelEncounterCardProps {
  encounter: TravelEncounterSummary
}

export const TravelEncounterCard: React.FC<TravelEncounterCardProps> = ({ encounter }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TravelExploreIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Travel Encounter
        </Typography>
        <Chip label={encounter.mode} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Era: {encounter.period}
      </Typography>
      <ProgressBar value={encounter.riskLevel * 100} compact color="purple" label="Risk" customText={`${Math.round(encounter.riskLevel * 100)}%`} />
      <Stack spacing={0.1}>
        {encounter.modifiers.slice(0, 2).map((modifier) => (
          <Typography key={modifier} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {modifier}
          </Typography>
        ))}
      </Stack>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        {encounter.rewards.slice(0, 2).map((reward) => (
          <Chip key={reward} label={reward} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        ))}
      </Box>
    </Stack>
  </CompactCard>
)

export default TravelEncounterCard


