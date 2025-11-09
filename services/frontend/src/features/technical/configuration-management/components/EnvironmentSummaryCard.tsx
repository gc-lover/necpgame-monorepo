import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import HubIcon from '@mui/icons-material/Hub'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { EnvironmentSummary } from '../types'

export interface EnvironmentSummaryCardProps {
  summary: EnvironmentSummary
}

export const EnvironmentSummaryCard: React.FC<EnvironmentSummaryCardProps> = ({ summary }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <HubIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {summary.name} Environment
        </Typography>
        <Chip label={`${summary.services} services`} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Overrides: {summary.overrides}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Drift alerts: {summary.driftAlerts}
      </Typography>
    </Stack>
  </CompactCard>
)

export default EnvironmentSummaryCard


