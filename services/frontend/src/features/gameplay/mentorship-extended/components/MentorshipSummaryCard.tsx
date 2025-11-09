import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import InsightsIcon from '@mui/icons-material/Insights'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface MentorshipSummaryCardProps {
  activeMentors: number
  pendingRequests: number
  activeLessons: number
  uniqueAbilitiesUnlocked: number
  worldImpactScore: number
}

export const MentorshipSummaryCard: React.FC<MentorshipSummaryCardProps> = ({ activeMentors, pendingRequests, activeLessons, uniqueAbilitiesUnlocked, worldImpactScore }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.4}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <InsightsIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Mentorship Overview
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Active mentors: {activeMentors}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Pending requests: {pendingRequests}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Active lessons: {activeLessons}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Unlocked abilities: {uniqueAbilitiesUnlocked}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        World impact score: {worldImpactScore}
      </Typography>
    </Stack>
  </CompactCard>
)

export default MentorshipSummaryCard


