import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import AccessTimeFilledIcon from '@mui/icons-material/AccessTimeFilled'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { AfkWarning } from '../types'

export interface AfkWarningCardProps {
  warning: AfkWarning
}

export const AfkWarningCard: React.FC<AfkWarningCardProps> = ({ warning }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <AccessTimeFilledIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          AFK Warning
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Triggered: {warning.triggeredAt}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Timeout in: {warning.timeoutSeconds}s
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Reason: {warning.reason}
      </Typography>
    </Stack>
  </CompactCard>
)

export default AfkWarningCard


