import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import GridOnIcon from '@mui/icons-material/GridOn'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ZoneCellMetric } from '../types'

export interface CellHeatmapCardProps {
  cells: ZoneCellMetric[]
}

export const CellHeatmapCard: React.FC<CellHeatmapCardProps> = ({ cells }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <GridOnIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Cell Heatmap
        </Typography>
      </Box>
      {cells.slice(0, 4).map((cell) => (
        <ProgressBar
          key={cell.cellKey}
          value={Math.min(100, (cell.playerCount / 100) * 100)}
          compact
          color="pink"
          label={`${cell.cellKey} • Latency ${cell.latencyMs}ms`}
          customText={`${cell.playerCount} players`}
        />
      ))}
    </Stack>
  </CompactCard>
)

export default CellHeatmapCard


