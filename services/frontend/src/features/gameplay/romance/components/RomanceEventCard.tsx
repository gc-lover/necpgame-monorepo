import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box } from '@mui/material'

export const RomanceEventCard: React.FC<{ event: any }> = ({ event }) => (
  <Card sx={{ border: '1px solid', borderColor: 'error.main', background: 'linear-gradient(135deg, rgba(255,20,147,0.05) 0%, rgba(0,0,0,0) 100%)' }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">{event.npc_name || 'Romance'}</Typography>
          <Chip label="♥" size="small" color="error" sx={{ height: 18, fontSize: '0.65rem' }} />
        </Box>
        <Typography variant="caption" fontSize="0.7rem" color="text.secondary">{event.description || 'Baldur Gate 3 стиль'}</Typography>
      </Stack>
    </CardContent>
  </Card>
)

