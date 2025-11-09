import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import ListAltIcon from '@mui/icons-material/ListAlt'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { IncidentLogEntry } from '../types'

export interface IncidentLogCardProps {
  incidents: IncidentLogEntry[]
}

export const IncidentLogCard: React.FC<IncidentLogCardProps> = ({ incidents }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <ListAltIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Incident Timeline
        </Typography>
      </Box>
      {incidents.slice(0, 4).map((incident) => (
        <Box key={incident.timestamp} display="flex" flexDirection="column" gap={0.1}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            {incident.timestamp} • {incident.type}
          </Typography>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            {incident.summary} ({incident.status})
          </Typography>
        </Box>
      ))}
    </Stack>
  </CompactCard>
)

export default IncidentLogCard


