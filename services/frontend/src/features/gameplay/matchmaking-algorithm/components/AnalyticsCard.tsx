import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import QueryStatsIcon from '@mui/icons-material/QueryStats'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { AnalyticsSnapshot } from '../types'

export interface AnalyticsCardProps {
  snapshot: AnalyticsSnapshot
}

export const AnalyticsCard: React.FC<AnalyticsCardProps> = ({ snapshot }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <QueryStatsIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Daily Analytics
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Matches: {snapshot.matchesToday} • Avg wait: {snapshot.averageWait}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Cancellations: {snapshot.cancellations} • Dodges: {snapshot.dodges}
      </Typography>
    </Stack>
  </CompactCard>
)

export default AnalyticsCard


