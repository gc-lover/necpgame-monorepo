import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import SecurityIcon from '@mui/icons-material/Security'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ConvoyEscort {
  escortId: string
  members: number
  firepower: 'LOW' | 'MEDIUM' | 'HIGH'
  status: 'READY' | 'EN_ROUTE' | 'RESTING'
}

const statusColor: Record<ConvoyEscort['status'], string> = {
  READY: '#05ffa1',
  EN_ROUTE: '#00f7ff',
  RESTING: '#fef86c',
}

export interface ConvoyStatusCardProps {
  convoyStrength: string
  escorts: ConvoyEscort[]
}

export const ConvoyStatusCard: React.FC<ConvoyStatusCardProps> = ({ convoyStrength, escorts }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <SecurityIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Convoy & Escort
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Strength: {convoyStrength}
        </Typography>
      </Box>
      <Stack spacing={0.2}>
        {escorts.map((escort) => (
          <Box key={escort.escortId} display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Squad {escort.escortId} · Members: {escort.members} · Firepower: {escort.firepower}
            </Typography>
            <Typography
              variant="caption"
              fontSize={cyberpunkTokens.fonts.xs}
              sx={{ color: statusColor[escort.status] }}
            >
              {escort.status}
            </Typography>
          </Box>
        ))}
        {escorts.length === 0 && (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Конвой без сопровождения
          </Typography>
        )}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ConvoyStatusCard


