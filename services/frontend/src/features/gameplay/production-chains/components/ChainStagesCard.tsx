import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import PrecisionManufacturingIcon from '@mui/icons-material/PrecisionManufacturing'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ChainStage {
  stageId: string
  name: string
  duration: string
  inputs: string
  outputs: string
}

export interface ChainStagesCardProps {
  stages: ChainStage[]
  finalProduct?: string
}

export const ChainStagesCard: React.FC<ChainStagesCardProps> = ({ stages, finalProduct }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <PrecisionManufacturingIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Production Stages
          </Typography>
        </Box>
        {finalProduct && (
          <Chip
            label={finalProduct}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        )}
      </Box>
      <Stack spacing={0.3}>
        {stages.map((stage, index) => (
          <Box key={stage.stageId} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                {index + 1}. {stage.name}
              </Typography>
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                {stage.duration}
              </Typography>
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Inputs: {stage.inputs}
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Outputs: {stage.outputs}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ChainStagesCard

