import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import BalanceIcon from '@mui/icons-material/Balance'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface SanctionMetrics {
  warnings: number
  temporaryBans: number
  permanentBans: number
  reinstated: number
}

export interface SanctionStatsCardProps {
  metrics: SanctionMetrics
}

export const SanctionStatsCard: React.FC<SanctionStatsCardProps> = ({ metrics }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <BalanceIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Sanction Metrics
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Warnings: {metrics.warnings}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Temporary bans: {metrics.temporaryBans}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Permanent bans: {metrics.permanentBans}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Reinstated accounts: {metrics.reinstated}
      </Typography>
    </Stack>
  </CompactCard>
)

export default SanctionStatsCard


