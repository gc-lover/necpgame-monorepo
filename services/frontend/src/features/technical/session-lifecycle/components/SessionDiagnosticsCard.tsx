import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import HealthAndSafetyIcon from '@mui/icons-material/HealthAndSafety'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { DiagnosticsEntry } from '../types'

const statusColor: Record<DiagnosticsEntry['status'], string> = {
  ok: '#05ffa1',
  warn: '#fef86c',
  error: '#ff2a6d',
}

export interface SessionDiagnosticsCardProps {
  diagnostics: DiagnosticsEntry[]
}

export const SessionDiagnosticsCard: React.FC<SessionDiagnosticsCardProps> = ({ diagnostics }) => (
  <CompactCard color='purple' glowIntensity='weak' compact>
    <Stack spacing={0.5}>
      <Box display='flex' alignItems='center' gap={0.6}>
        <HealthAndSafetyIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant='caption' fontSize={cyberpunkTokens.fonts.sm} fontWeight='bold'>
          Session Diagnostics
        </Typography>
      </Box>
      {diagnostics.slice(0, 4).map((entry) => (
        <Box key={entry.label} display='flex' alignItems='center' gap={0.4}>
          <Chip label={entry.status} size='small' sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${statusColor[entry.status]}`, color: statusColor[entry.status] }} />
          <Typography variant='caption' fontSize={cyberpunkTokens.fonts.xs} color='text.secondary'>
            {entry.label}: {entry.value}
          </Typography>
        </Box>
      ))}
      {diagnostics.length === 0 && (
        <Typography variant='caption' fontSize={cyberpunkTokens.fonts.xs} color='text.secondary'>
          Диагностика не доступна.
        </Typography>
      )}
    </Stack>
  </CompactCard>
)

export default SessionDiagnosticsCard


