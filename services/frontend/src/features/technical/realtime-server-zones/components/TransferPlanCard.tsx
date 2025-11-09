import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import SwapHorizIcon from '@mui/icons-material/SwapHoriz'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ZoneTransferPlan } from '../types'

const priorityColor: Record<ZoneTransferPlan['priority'], string> = {
  low: '#05ffa1',
  medium: '#fef86c',
  high: '#ff2a6d',
}

export interface TransferPlanCardProps {
  plan: ZoneTransferPlan
}

export const TransferPlanCard: React.FC<TransferPlanCardProps> = ({ plan }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <SwapHorizIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Zone Transfer Plan
        </Typography>
        <Chip
          label={plan.priority.toUpperCase()}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${priorityColor[plan.priority]}`, color: priorityColor[plan.priority], ml: 'auto' }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Target instance: {plan.targetInstanceId}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Strategy: {plan.drainStrategy} • Scheduled: {plan.scheduledFor}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Reason: {plan.reason}
      </Typography>
    </Stack>
  </CompactCard>
)

export default TransferPlanCard


