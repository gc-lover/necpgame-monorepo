import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import TimelineIcon from '@mui/icons-material/Timeline'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface PlotThreadSummary {
  threadId: string
  title: string
  faction: string
  arcStage: 'SETUP' | 'CONFLICT' | 'CLIMAX' | 'RESOLUTION'
  coherenceScore: number
  openBeats: number
  resolvedBeats: number
  synopsis: string
}

const stageColor: Record<PlotThreadSummary['arcStage'], string> = {
  SETUP: '#00f7ff',
  CONFLICT: '#fef86c',
  CLIMAX: '#ff2a6d',
  RESOLUTION: '#05ffa1',
}

export interface PlotThreadCardProps {
  thread: PlotThreadSummary
}

export const PlotThreadCard: React.FC<PlotThreadCardProps> = ({ thread }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <TimelineIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {thread.title}
          </Typography>
        </Box>
        <Chip
          label={thread.arcStage}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${stageColor[thread.arcStage]}`,
            color: stageColor[thread.arcStage],
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Faction: {thread.faction}
      </Typography>
      <ProgressBar
        value={thread.coherenceScore}
        label="Coherence"
        color="cyan"
        compact
        customText={`${thread.coherenceScore}%`}
      />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Beats: {thread.resolvedBeats} resolved · {thread.openBeats} open
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {thread.synopsis}
      </Typography>
    </Stack>
  </CompactCard>
)

export default PlotThreadCard


