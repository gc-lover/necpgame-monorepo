import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import CloudUploadIcon from '@mui/icons-material/CloudUpload'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { BackupPlan } from '../types'

export interface BackupPlanCardProps {
  plan: BackupPlan
}

export const BackupPlanCard: React.FC<BackupPlanCardProps> = ({ plan }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <CloudUploadIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {plan.name}
        </Typography>
        <Chip label={plan.cadence} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Retention: {plan.retention}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Last run: {plan.lastRun}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Next run: {plan.nextRun}
      </Typography>
    </Stack>
  </CompactCard>
)

export default BackupPlanCard


