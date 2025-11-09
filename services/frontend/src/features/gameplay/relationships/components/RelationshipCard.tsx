import React from 'react'
import { Card, CardContent, Typography, Stack, LinearProgress, Box } from '@mui/material'

export const RelationshipCard: React.FC<{ relationship: any }> = ({ relationship }) => (
  <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">{relationship.name || 'NPC'}</Typography>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="caption" fontSize="0.7rem">Отношения:</Typography>
          <Typography variant="caption" fontSize="0.7rem" fontWeight="bold">{relationship.level || 0}/100</Typography>
        </Box>
        <LinearProgress variant="determinate" value={relationship.level || 0} sx={{ height: 4 }} />
      </Stack>
    </CardContent>
  </Card>
)

