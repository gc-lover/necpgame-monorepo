import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import InsightsIcon from '@mui/icons-material/Insights'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ContentOverviewSummary {
  period: string
  totalQuests: number
  questsByType: {
    main: number
    side: number
    faction: number
  }
  totalLocations: number
  totalNPCs: number
  keyEvents: string[]
  implementedPercentage: number
}

export interface ContentOverviewCardProps {
  overview: ContentOverviewSummary
}

export const ContentOverviewCard: React.FC<ContentOverviewCardProps> = ({ overview }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <InsightsIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Content Overview {overview.period}
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {overview.implementedPercentage}% ready
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Quests: {overview.totalQuests} (Main {overview.questsByType.main} / Side {overview.questsByType.side} / Faction {overview.questsByType.faction})
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Locations: {overview.totalLocations} · NPCs: {overview.totalNPCs}
      </Typography>
      <Stack spacing={0.2}>
        {overview.keyEvents.slice(0, 3).map((event) => (
          <Typography key={event} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {event}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ContentOverviewCard


