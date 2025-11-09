import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box, Divider } from '@mui/material'
import BusinessIcon from '@mui/icons-material/Business'
import EventIcon from '@mui/icons-material/Event'

interface ModernEraEventProps {
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
  }
}

export const ModernEraEventCard: React.FC<ModernEraEventProps> = ({ event }) => {
  return (
    <Card sx={{ border: '1px solid', borderColor: '#FFD700', bgcolor: 'rgba(255, 215, 0, 0.05)' }}>
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box display="flex" alignItems="center" gap={0.5}>
              <BusinessIcon sx={{ fontSize: '1rem', color: '#FFD700' }} />
              <Typography variant="caption" fontSize="0.75rem" fontWeight="bold" color="#FFD700">
                {event.event_type?.toUpperCase().replace('_', ' ')}
              </Typography>
            </Box>
            <Chip label={event.era} size="small" sx={{ bgcolor: '#FFD700', color: 'black', height: 16, fontSize: '0.6rem' }} />
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
        </Stack>
      </CardContent>
    </Card>
  )
}

