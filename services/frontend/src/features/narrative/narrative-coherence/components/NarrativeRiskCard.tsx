import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import WarningIcon from '@mui/icons-material/Warning'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface NarrativeRiskEntry {
  riskId: string
  description: string
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  impactedThreads: string[]
  mitigation: string
}

const severityColor: Record<NarrativeRiskEntry['severity'], string> = {
  LOW: '#05ffa1',
  MEDIUM: '#fef86c',
  HIGH: '#ff8f2a',
  CRITICAL: '#ff2a6d',
}

export interface NarrativeRiskCardProps {
  risk: NarrativeRiskEntry
}

export const NarrativeRiskCard: React.FC<NarrativeRiskCardProps> = ({ risk }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <WarningIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Narrative Risk
          </Typography>
        </Box>
        <Chip
          label={risk.severity}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${severityColor[risk.severity]}`,
            color: severityColor[risk.severity],
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {risk.description}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Threads: {risk.impactedThreads.join(', ')}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Mitigation: {risk.mitigation}
      </Typography>
    </Stack>
  </CompactCard>
)

export default NarrativeRiskCard


