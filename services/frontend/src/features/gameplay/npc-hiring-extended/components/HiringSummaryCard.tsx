import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import AnalyticsIcon from '@mui/icons-material/Analytics'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface HiringSummaryCardProps {
  activeHires: number
  pendingContracts: number
  weeklyUpkeep: number
  squadStrength: number
  missionCoverage: number
}

export const HiringSummaryCard: React.FC<HiringSummaryCardProps> = ({ activeHires, pendingContracts, weeklyUpkeep, squadStrength, missionCoverage }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.4}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <AnalyticsIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Hiring Overview
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Active hires: {activeHires}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Pending contracts: {pendingContracts}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Weekly upkeep: {weeklyUpkeep}¥
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Squad strength index: {squadStrength}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Mission coverage: {missionCoverage}%
      </Typography>
    </Stack>
  </CompactCard>
)

export default HiringSummaryCard


