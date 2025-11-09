import React from 'react'
import { Typography, Stack } from '@mui/material'
import ExitToAppIcon from '@mui/icons-material/ExitToApp'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { EvacuationPlan } from '../types'

export interface EvacuationPlanCardProps {
  plan: EvacuationPlan
}

export const EvacuationPlanCard: React.FC<EvacuationPlanCardProps> = ({ plan }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Stack direction="row" spacing={0.6} alignItems="center">
        <ExitToAppIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Zone Evacuation
        </Typography>
      </Stack>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Target zone: {plan.targetZoneId}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Batch size: {plan.batchSize} • Interval: {plan.intervalMs}ms
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Notify players: {plan.notifyPlayers ? 'YES' : 'NO'} • Timeout: {plan.timeoutSeconds}s
      </Typography>
    </Stack>
  </CompactCard>
)

export default EvacuationPlanCard


