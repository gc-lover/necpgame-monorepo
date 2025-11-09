import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box } from '@mui/material'

export const LeagueInfoCard: React.FC<{ league: any }> = ({ league }) => (
  <Card sx={{ border: '1px solid', borderColor: 'primary.main' }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">Лига {league.season || '73 года'}</Typography>
          <Chip label={league.phase || 'Active'} size="small" color="primary" sx={{ height: 18, fontSize: '0.65rem' }} />
        </Box>
        <Typography variant="caption" fontSize="0.7rem">{league.description || '2020-2093, 3-6 мес реал'}</Typography>
      </Stack>
    </CardContent>
  </Card>
)

