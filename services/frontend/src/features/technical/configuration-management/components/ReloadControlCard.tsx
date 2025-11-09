import React from 'react'
import { Typography, Stack } from '@mui/material'
import ReplayIcon from '@mui/icons-material/Replay'
import { CompactCard } from '@/shared/ui/cards'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ReloadStatus } from '../types'

export interface ReloadControlCardProps {
  status: ReloadStatus
  onReload?: () => void
}

const statusColor: Record<ReloadStatus['status'], 'success' | 'info' | 'warning' | 'error'> = {
  IDLE: 'info',
  IN_PROGRESS: 'warning',
  SUCCESS: 'success',
  FAILED: 'error',
}

export const ReloadControlCard: React.FC<ReloadControlCardProps> = ({ status, onReload }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Stack direction="row" spacing={0.6} alignItems="center">
        <ReplayIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Configuration Reload
        </Typography>
      </Stack>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Last reload: {status.lastReload} • by {status.triggeredBy}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={`${statusColor[status.status]}.main`}>
        Status: {status.status}
      </Typography>
      <CyberpunkButton variant="primary" size="small" onClick={onReload}>
        Выполнить reload
      </CyberpunkButton>
    </Stack>
  </CompactCard>
)

export default ReloadControlCard


