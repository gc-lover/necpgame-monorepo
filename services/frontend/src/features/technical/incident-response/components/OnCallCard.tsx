import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import SupportAgentIcon from '@mui/icons-material/SupportAgent'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { OnCallInfo } from '../types'

export interface OnCallCardProps {
  info: OnCallInfo
}

export const OnCallCard: React.FC<OnCallCardProps> = ({ info }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <SupportAgentIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          On-call Rotation
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Current: {info.currentResponder} ({info.timeRemaining} left)
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Rotation: {info.rotation}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Next up: {info.nextUp}
      </Typography>
    </Stack>
  </CompactCard>
)

export default OnCallCard


