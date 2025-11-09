import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box, Divider } from '@mui/material'
import WarningIcon from '@mui/icons-material/Warning'
import EventIcon from '@mui/icons-material/Event'

interface WorldEventProps {
  event: {
    event_id?: string
    event_type?: string
    era?: string
    description?: string
    location_id?: string
    dc_scaling?: {
      social?: number
      tech_hack?: number
      combat?: number
    }
    consequences?: string[]
    quest_hooks?: string[]
  }
}

export const WorldEventCard: React.FC<WorldEventProps> = ({ event }) => {
  const getEventTypeColor = (type?: string) => {
    switch (type) {
      case 'rad_zone':
        return 'error'
      case 'rescue_ops':
        return 'success'
      case 'combat_zones':
        return 'warning'
      case 'negotiations':
        return 'info'
      default:
        return 'default'
    }
  }

  return (
    <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box display="flex" alignItems="center" gap={0.5}>
              <EventIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
              <Typography variant="caption" fontSize="0.75rem" fontWeight="bold">
                {event.event_type?.toUpperCase().replace('_', ' ')}
              </Typography>
            </Box>
            <Chip label={event.era} size="small" color="primary" sx={{ height: 16, fontSize: '0.6rem' }} />
          </Box>
          <Typography variant="body2" fontSize="0.7rem">
            {event.description}
          </Typography>
          <Divider sx={{ my: 0.5 }} />
          <Box display="flex" gap={0.3} flexWrap="wrap">
            {event.dc_scaling && (
              <>
                <Chip label={`SOC ${event.dc_scaling.social}`} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />
                <Chip label={`TECH ${event.dc_scaling.tech_hack}`} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />
                <Chip label={`CBT ${event.dc_scaling.combat}`} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />
              </>
            )}
          </Box>
          {event.consequences && event.consequences.length > 0 && (
            <>
              <Typography variant="caption" fontSize="0.65rem" fontWeight="bold">
                Последствия:
              </Typography>
              {event.consequences.map((c, i) => (
                <Typography key={i} variant="caption" fontSize="0.6rem">
                  • {c}
                </Typography>
              ))}
            </>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

