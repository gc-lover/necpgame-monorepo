import React from 'react'
import { Card, CardContent, Typography, LinearProgress, Box, Chip } from '@mui/material'

export const CombatParticipantCard: React.FC<{ participant: any }> = ({ participant }) => (
  <Card sx={{ border: '1px solid', borderColor: participant.isAlive ? 'divider' : 'error.main', opacity: participant.isAlive ? 1 : 0.6 }}>
    <CardContent sx={{ p: 1, '&:last-child': { pb: 1 } }}>
      <Box display="flex" justifyContent="space-between" mb={0.5}>
        <Typography variant="caption" fontWeight="bold" fontSize="0.75rem">{participant.name}</Typography>
        <Chip label={participant.type} size="small" sx={{ height: 16, fontSize: '0.6rem' }} />
      </Box>
      <Typography variant="caption" fontSize="0.65rem">HP: {participant.health}/{participant.maxHealth}</Typography>
      <LinearProgress variant="determinate" value={(participant.health/participant.maxHealth)*100} color={participant.health > 50 ? 'success' : 'error'} sx={{ height: 4, mt: 0.5 }} />
    </CardContent>
  </Card>
)

