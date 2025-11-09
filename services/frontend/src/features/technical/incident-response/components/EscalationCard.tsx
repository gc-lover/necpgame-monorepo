import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { EscalationStatus } from '../types'

const statusColor: Record<EscalationStatus['status'], string> = {
  pending: '#fef86c',
  engaged: '#ff9f43',
  completed: '#05ffa1',
}

export interface EscalationCardProps {
  escalation: EscalationStatus
}

export const EscalationCard: React.FC<EscalationCardProps> = ({ escalation }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TrendingUpIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Escalation • {escalation.level}
        </Typography>
        <Chip
          label={escalation.status}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${statusColor[escalation.status]}`, color: statusColor[escalation.status], ml: 'auto' }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Target: {escalation.target}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Triggered: {escalation.triggeredAt} • Channel: {escalation.channel}
      </Typography>
    </Stack>
  </CompactCard>
)

export default EscalationCard


