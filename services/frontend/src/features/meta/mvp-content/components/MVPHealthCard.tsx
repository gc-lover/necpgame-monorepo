import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import HealthAndSafetyIcon from '@mui/icons-material/HealthAndSafety'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface MVPHealthStatus {
  status: 'HEALTHY' | 'DEGRADED' | 'DOWN'
  systems: Record<string, 'HEALTHY' | 'DEGRADED' | 'DOWN'>
}

const statusColor: Record<MVPHealthStatus['status'], string> = {
  HEALTHY: '#05ffa1',
  DEGRADED: '#fef86c',
  DOWN: '#ff2a6d',
}

export interface MVPHealthCardProps {
  health: MVPHealthStatus
}

export const MVPHealthCard: React.FC<MVPHealthCardProps> = ({ health }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <HealthAndSafetyIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            MVP Systems Health
          </Typography>
        </Box>
        <Typography
          variant="caption"
          fontSize={cyberpunkTokens.fonts.xs}
          sx={{ color: statusColor[health.status] }}
        >
          {health.status}
        </Typography>
      </Box>
      <Stack spacing={0.2}>
        {Object.entries(health.systems).map(([system, status]) => (
          <Typography key={system} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={statusColor[status]}>
            {system.replace('_', ' ')}: {status}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default MVPHealthCard


