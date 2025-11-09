import React from 'react'
import { Card, CardContent, Typography, Stack, Chip } from '@mui/material'

export const WorldStateCard: React.FC<{ state: any }> = ({ state }) => (
  <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">{state.category || 'Категория'}</Typography>
        <Chip label={state.level || 'Individual'} size="small" color="info" sx={{ height: 18, fontSize: '0.65rem', width: 'fit-content' }} />
        <Typography variant="caption" fontSize="0.7rem">{state.description || 'Влияние игрока на мир'}</Typography>
      </Stack>
    </CardContent>
  </Card>
)

