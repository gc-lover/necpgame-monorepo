import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import DashboardIcon from '@mui/icons-material/Dashboard'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface GlobalStateSummaryCardProps {
  worldVersion: number
  factionVersion: number
  economyVersion: number
  activeSessions: number
  mutationQueue: number
  globalCoherence: number
}

export const GlobalStateSummaryCard: React.FC<GlobalStateSummaryCardProps> = ({
  worldVersion,
  factionVersion,
  economyVersion,
  activeSessions,
  mutationQueue,
  globalCoherence,
}) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.4}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <DashboardIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Global State Overview
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        World version: {worldVersion}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Faction version: {factionVersion}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Economy version: {economyVersion}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Active sessions: {activeSessions}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Mutation queue: {mutationQueue}
      </Typography>
      <ProgressBar
        value={globalCoherence}
        label="Coherence"
        color="cyan"
        compact
        customText={`${globalCoherence}%`}
      />
    </Stack>
  </CompactCard>
)

export default GlobalStateSummaryCard


