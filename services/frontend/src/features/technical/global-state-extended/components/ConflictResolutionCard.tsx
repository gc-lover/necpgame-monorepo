import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import GavelIcon from '@mui/icons-material/Gavel'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ConflictSummary {
  conflictId: string
  component: string
  detectedAt: string
  status: 'RESOLVED' | 'RESOLVING' | 'PENDING'
  strategy: string
}

const statusColor: Record<ConflictSummary['status'], string> = {
  RESOLVED: '#05ffa1',
  RESOLVING: '#fef86c',
  PENDING: '#ff2a6d',
}

export interface ConflictResolutionCardProps {
  conflicts: ConflictSummary[]
}

export const ConflictResolutionCard: React.FC<ConflictResolutionCardProps> = ({ conflicts }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <GavelIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Conflict Resolution
        </Typography>
      </Box>
      <Stack spacing={0.2}>
        {conflicts.slice(0, 3).map((conflict) => (
          <Box key={conflict.conflictId} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
                {conflict.component}
              </Typography>
              <Chip
                label={conflict.status}
                size="small"
                sx={{
                  height: 16,
                  fontSize: cyberpunkTokens.fonts.xs,
                  border: `1px solid ${statusColor[conflict.status]}`,
                  color: statusColor[conflict.status],
                }}
              />
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Strategy: {conflict.strategy}
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Detected: {conflict.detectedAt}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ConflictResolutionCard


