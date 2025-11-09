import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box, Divider } from '@mui/material'
import Brightness3Icon from '@mui/icons-material/Brightness3'
import EventIcon from '@mui/icons-material/Event'

interface RedEraEventProps {
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

export const RedEraEventCard: React.FC<RedEraEventProps> = ({ event }) => {
  return (
    <Card sx={{ border: '1px solid', borderColor: '#8B0000', bgcolor: 'rgba(139, 0, 0, 0.05)' }}>
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box display="flex" alignItems="center" gap={0.5}>
              <Brightness3Icon sx={{ fontSize: '1rem', color: '#8B0000' }} />
              <Typography variant="caption" fontSize="0.75rem" fontWeight="bold" color="#8B0000">
                {event.event_type?.toUpperCase().replace('_', ' ')}
              </Typography>
            </Box>
            <Chip label={event.era} size="small" sx={{ bgcolor: '#8B0000', color: 'white', height: 16, fontSize: '0.6rem' }} />
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

