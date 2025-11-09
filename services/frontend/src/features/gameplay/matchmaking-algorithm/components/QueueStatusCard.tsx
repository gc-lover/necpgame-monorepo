import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import QueueIcon from '@mui/icons-material/Queue'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { QueueStatus } from '../types'

export interface QueueStatusCardProps {
  status: QueueStatus
}

export const QueueStatusCard: React.FC<QueueStatusCardProps> = ({ status }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <QueueIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {status.mode} Queue
        </Typography>
        <Chip label={`${status.population} online`} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Estimated wait: {status.estimatedWait}
      </Typography>
      <ProgressBar
        value={Math.min(100, (status.inReadyCheck / Math.max(1, status.population)) * 100)}
        compact
        color="yellow"
        label="Ready-check"
        customText={`${status.inReadyCheck}`}
      />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Active tickets: {status.activeTickets}
      </Typography>
    </Stack>
  </CompactCard>
)

export default QueueStatusCard


