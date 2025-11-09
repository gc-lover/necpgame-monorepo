import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import ShieldIcon from '@mui/icons-material/Shield'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { DrStatus } from '../types'

export interface DrStatusCardProps {
  status: DrStatus
}

export const DrStatusCard: React.FC<DrStatusCardProps> = ({ status }) => (
  <CompactCard color={status.ready ? 'green' : 'pink'} glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <ShieldIcon sx={{ fontSize: '1rem', color: status.ready ? 'success.main' : 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          DR Readiness
        </Typography>
        <Chip
          label={status.ready ? 'READY' : 'AT RISK'}
          size="small"
          color={status.ready ? 'success' : 'error'}
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Last backup: {status.lastBackup} • Cadence: {status.backupFrequency}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Failover ready: {status.failoverReady ? 'Yes' : 'No'}
      </Typography>
      <ProgressBar
        value={Math.min(100, status.rpoMinutes === 0 ? 100 : (60 / status.rpoMinutes) * 100)}
        compact
        color="cyan"
        label="RPO"
        customText={`${status.rpoMinutes} min`}
      />
      <ProgressBar
        value={Math.min(100, status.rtoMinutes === 0 ? 100 : (120 / status.rtoMinutes) * 100)}
        compact
        color="yellow"
        label="RTO"
        customText={`${status.rtoMinutes} min`}
      />
    </Stack>
  </CompactCard>
)

export default DrStatusCard

