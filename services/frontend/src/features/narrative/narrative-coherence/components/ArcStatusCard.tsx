import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import AutoStoriesIcon from '@mui/icons-material/AutoStories'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ArcStatusSummary {
  arcName: string
  phase: 'PHASE_1' | 'PHASE_2' | 'PHASE_3'
  episodesReleased: number
  totalEpisodes: number
  branchingPoints: number
  coherenceDelta: number
}

const phaseLabel: Record<ArcStatusSummary['phase'], string> = {
  PHASE_1: 'Phase 1',
  PHASE_2: 'Phase 2',
  PHASE_3: 'Phase 3',
}

export interface ArcStatusCardProps {
  arc: ArcStatusSummary
}

export const ArcStatusCard: React.FC<ArcStatusCardProps> = ({ arc }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <AutoStoriesIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {arc.arcName}
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {phaseLabel[arc.phase]}
        </Typography>
      </Box>
      <ProgressBar
        value={(arc.episodesReleased / Math.max(arc.totalEpisodes, 1)) * 100}
        label="Episodes"
        color="cyan"
        compact
        customText={`${arc.episodesReleased}/${arc.totalEpisodes}`}
      />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Branching points: {arc.branchingPoints}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={arc.coherenceDelta >= 0 ? 'success.main' : 'error.main'}>
        Coherence drift: {arc.coherenceDelta >= 0 ? '+' : ''}{arc.coherenceDelta}%
      </Typography>
    </Stack>
  </CompactCard>
)

export default ArcStatusCard


