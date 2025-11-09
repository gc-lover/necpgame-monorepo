import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box } from '@mui/material'
import PublicIcon from '@mui/icons-material/Public'
import type { GlobalEvent } from '@/api/generated/global-events/models'

interface GlobalEventCardProps {
  event: GlobalEvent
  onClick?: (eventId: string) => void
}

export const GlobalEventCard: React.FC<GlobalEventCardProps> = ({ event, onClick }) => {
  const getTypeColor = (type?: string) => {
    switch (type) {
      case 'political':
        return 'error'
      case 'economic':
        return 'warning'
      case 'technological':
        return 'primary'
      case 'environmental':
        return 'success'
      case 'social':
        return 'info'
      default:
        return 'default'
    }
  }

  return (
    <Card
      sx={{
        border: '1px solid',
        borderColor: event.is_active ? 'warning.main' : 'divider',
        background: event.is_active ? 'rgba(255, 193, 7, 0.05)' : 'transparent',
        cursor: onClick ? 'pointer' : 'default',
        '&:hover': onClick ? { borderColor: 'primary.main' } : {},
      }}
      onClick={() => onClick && event.event_id && onClick(event.event_id)}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box display="flex" alignItems="center" gap={0.5}>
              <PublicIcon sx={{ fontSize: '1rem', color: event.is_active ? 'warning.main' : 'text.secondary' }} />
              <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">
                {event.name}
              </Typography>
            </Box>
            {event.is_active && <Chip label="АКТИВНО" size="small" color="warning" sx={{ height: 18, fontSize: '0.65rem' }} />}
          </Box>
          <Box display="flex" gap={0.5} flexWrap="wrap">
            <Chip label={event.type || 'event'} size="small" color={getTypeColor(event.type)} sx={{ height: 16, fontSize: '0.6rem' }} />
            <Chip label={event.era || 'era'} size="small" variant="outlined" sx={{ height: 16, fontSize: '0.6rem' }} />
            {event.year_start && <Chip label={event.year_end ? `${event.year_start}-${event.year_end}` : event.year_start} size="small" sx={{ height: 16, fontSize: '0.6rem' }} />}
          </Box>
          {event.short_description && (
            <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
              {event.short_description}
            </Typography>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

