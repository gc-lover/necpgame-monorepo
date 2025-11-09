import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import SecurityIcon from '@mui/icons-material/Security'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface BanMetrics {
  active: number
  pendingAppeals: number
  autoBans: number
  manualBans: number
}

export interface BanOverviewCardProps {
  metrics: BanMetrics
}

export const BanOverviewCard: React.FC<BanOverviewCardProps> = ({ metrics }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <SecurityIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Ban Overview
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Active bans: {metrics.active}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Pending appeals: {metrics.pendingAppeals}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Auto bans: {metrics.autoBans}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Manual bans: {metrics.manualBans}
      </Typography>
    </Stack>
  </CompactCard>
)

export default BanOverviewCard


