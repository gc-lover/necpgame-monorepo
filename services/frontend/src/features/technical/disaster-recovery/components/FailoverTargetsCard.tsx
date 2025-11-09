import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import SwapHorizIcon from '@mui/icons-material/SwapHoriz'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { FailoverTarget } from '../types'

export interface FailoverTargetsCardProps {
  targets: FailoverTarget[]
}

export const FailoverTargetsCard: React.FC<FailoverTargetsCardProps> = ({ targets }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <SwapHorizIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Failover Targets
        </Typography>
      </Box>
      {targets.slice(0, 3).map((target) => (
        <Box key={target.datacenter} display="flex" flexDirection="column" gap={0.2}>
          <Box display="flex" alignItems="center" gap={0.4}>
            <Chip label={target.datacenter} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              {target.region} • {target.latencyMs} ms
            </Typography>
          </Box>
          <ProgressBar value={target.capacityPercent} compact color="pink" label="Capacity" customText={`${target.capacityPercent}%`} />
        </Box>
      ))}
    </Stack>
  </CompactCard>
)

export default FailoverTargetsCard


