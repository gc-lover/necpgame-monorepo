import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import ExitToAppIcon from '@mui/icons-material/ExitToApp'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ForceLogoutPlan } from '../types'

export interface ForceLogoutCardProps {
  plan: ForceLogoutPlan
}

export const ForceLogoutCard: React.FC<ForceLogoutCardProps> = ({ plan }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <ExitToAppIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Force Logout
        </Typography>
        <Chip label={plan.notify ? 'Notify ON' : 'Notify OFF'} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Player: {plan.playerId} {plan.accountId ? `• Account: ${plan.accountId}` : ''}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Scheduled: {plan.scheduledFor}
      </Typography>
    </Stack>
  </CompactCard>
)

export default ForceLogoutCard


