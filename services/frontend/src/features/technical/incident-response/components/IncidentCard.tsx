import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import WarningIcon from '@mui/icons-material/Warning'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { IncidentSummary } from '../types'

const severityColor: Record<IncidentSummary['severity'], string> = {
  critical: '#ff2a6d',
  high: '#ff9f43',
  medium: '#fef86c',
  low: '#05ffa1',
}

const statusColor: Record<IncidentSummary['status'], string> = {
  detected: '#fef86c',
  acknowledged: '#ff9f43',
  mitigated: '#05ffa1',
  resolved: '#00f7ff',
}

export interface IncidentCardProps {
  incident: IncidentSummary
}

export const IncidentCard: React.FC<IncidentCardProps> = ({ incident }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <WarningIcon sx={{ fontSize: '1rem', color: severityColor[incident.severity] }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {incident.title}
        </Typography>
        <Chip
          label={incident.severity.toUpperCase()}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${severityColor[incident.severity]}`, color: severityColor[incident.severity], ml: 'auto' }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={statusColor[incident.status]}>
        Status: {incident.status.toUpperCase()}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Detected: {incident.detectedAt} • Commander: {incident.commander}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Services: {incident.affectedServices.join(', ')}
      </Typography>
    </Stack>
  </CompactCard>
)

export default IncidentCard


