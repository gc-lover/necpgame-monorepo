import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import ChecklistIcon from '@mui/icons-material/Checklist'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { RcaSummary } from '../types'

export interface RcaCardProps {
  rca: RcaSummary
}

export const RcaCard: React.FC<RcaCardProps> = ({ rca }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <ChecklistIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Post-Incident RCA
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Root cause: {rca.rootCause}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Contributing: {rca.contributingFactors.slice(0, 2).join(', ') || '—'}
      </Typography>
      <Stack spacing={0.1}>
        {rca.correctiveActions.slice(0, 2).map((action, index) => (
          <Typography key={`${action.description}-${index}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {action.description} — {action.owner} ({action.status})
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default RcaCard


