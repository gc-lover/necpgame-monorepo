import React from 'react'
import { Card, CardContent, Typography, Box, Stack } from '@mui/material'

export const EventCard: React.FC<{ event: any }> = ({ event }) => (
  <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Typography variant="caption" fontWeight="bold" fontSize="0.75rem">{event.event_type}</Typography>
        <Typography variant="caption" color="text.secondary" fontSize="0.7rem">{event.timestamp}</Typography>
      </Stack>
    </CardContent>
  </Card>
)

