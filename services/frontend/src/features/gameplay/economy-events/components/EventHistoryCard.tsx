import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TimelineIcon from '@mui/icons-material/Timeline'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface EventHistoryEntry {
  eventId: string
  name: string
  type: string
  severity: string
  startDate: string
  endDate: string
  durationDays: number
  impactSummary: string
}

export interface EventHistoryCardProps {
  history: EventHistoryEntry[]
}

export const EventHistoryCard: React.FC<EventHistoryCardProps> = ({ history }) => (
  <CompactCard color="pink" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TimelineIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Recent Economy Events
        </Typography>
      </Box>
      <Stack spacing={0.3}>
        {history.map((entry) => (
          <Box key={entry.eventId} display="flex" flexDirection="column" gap={0.1}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
              {entry.name} · {entry.type} · {entry.severity}
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              {entry.startDate} → {entry.endDate} ({entry.durationDays}d)
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              {entry.impactSummary}
            </Typography>
          </Box>
        ))}
        {history.length === 0 && (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            История пуста
          </Typography>
        )}
      </Stack>
    </Stack>
  </CompactCard>
)

export default EventHistoryCard


