import React from 'react'
import { Typography, Stack, Box, Chip, Divider } from '@mui/material'
import PublicIcon from '@mui/icons-material/Public'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface EconomyEventSummary {
  eventId: string
  name: string
  type: 'CRISIS' | 'INFLATION' | 'RECESSION' | 'BOOM' | 'TRADE_WAR' | 'CORPORATE' | 'COMMODITY'
  severity: 'MINOR' | 'MODERATE' | 'MAJOR' | 'CATASTROPHIC'
  startDate: string
  endDate?: string | null
  isActive: boolean
  affectedRegions: string[]
  affectedSectors: string[]
}

const severityColor: Record<EconomyEventSummary['severity'], string> = {
  MINOR: '#05ffa1',
  MODERATE: '#00f7ff',
  MAJOR: '#fef86c',
  CATASTROPHIC: '#ff2a6d',
}

export interface EconomyEventCardProps {
  event: EconomyEventSummary
}

export const EconomyEventCard: React.FC<EconomyEventCardProps> = ({ event }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <PublicIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {event.name}
          </Typography>
        </Box>
        <Chip
          label={event.severity}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            bgcolor: 'rgba(255,255,255,0.05)',
            border: `1px solid ${severityColor[event.severity]}`,
            color: severityColor[event.severity],
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        #{event.eventId.slice(0, 8)} · Type: {event.type} · {event.isActive ? 'ACTIVE' : 'ENDED'}
      </Typography>
      <Divider sx={{ my: 0.3 }} />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Start: {event.startDate} {event.endDate ? `· End: ${event.endDate}` : ''}
      </Typography>
      <Box display="flex" gap={0.4} flexWrap="wrap">
        {event.affectedRegions.slice(0, 3).map((region) => (
          <Chip key={region} label={region} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        ))}
        {event.affectedRegions.length > 3 && (
          <Chip
            label={`+${event.affectedRegions.length - 3}`}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        )}
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Sectors: {event.affectedSectors.join(', ') || '—'}
      </Typography>
    </Stack>
  </CompactCard>
)

export default EconomyEventCard


