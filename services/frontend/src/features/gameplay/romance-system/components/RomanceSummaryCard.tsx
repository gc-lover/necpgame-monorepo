import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import InsightsIcon from '@mui/icons-material/Insights'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface RomanceSummaryCardProps {
  activeRelationships: number
  maxConcurrent: number
  jealousyAlerts: number
  conflicts: number
  commitmentRate: number
}

export const RomanceSummaryCard: React.FC<RomanceSummaryCardProps> = ({ activeRelationships, maxConcurrent, jealousyAlerts, conflicts, commitmentRate }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <InsightsIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Romance Overview
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Active romances: {activeRelationships}/{maxConcurrent}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={jealousyAlerts > 0 ? 'warning.main' : 'text.secondary'}>
        Jealousy alerts: {jealousyAlerts}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={conflicts > 0 ? 'error.main' : 'text.secondary'}>
        Active conflicts: {conflicts}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Commitment rate: {commitmentRate}%
      </Typography>
    </Stack>
  </CompactCard>
)

export default RomanceSummaryCard


