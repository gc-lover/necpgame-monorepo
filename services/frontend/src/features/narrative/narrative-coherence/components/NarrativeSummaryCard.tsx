import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import InsightsIcon from '@mui/icons-material/Insights'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface NarrativeSummaryCardProps {
  activeThreads: number
  arcsTracked: number
  unresolvedBeats: number
  totalCoherence: number
  lastSync: string
}

export const NarrativeSummaryCard: React.FC<NarrativeSummaryCardProps> = ({ activeThreads, arcsTracked, unresolvedBeats, totalCoherence, lastSync }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.4}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <InsightsIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Narrative Overview
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Active threads: {activeThreads}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Arcs tracked: {arcsTracked}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Unresolved beats: {unresolvedBeats}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Global coherence: {totalCoherence}%
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Last sync: {lastSync}
      </Typography>
    </Stack>
  </CompactCard>
)

export default NarrativeSummaryCard


