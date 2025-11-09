import React from 'react'
import { Card, CardContent, Typography, Chip, Box, Stack } from '@mui/material'

export const ExtractionZoneCard: React.FC<{ zone: any; onClick?: () => void }> = ({ zone, onClick }) => (
  <Card onClick={onClick} sx={{ cursor: onClick ? 'pointer' : 'default', border: '1px solid', borderColor: 'divider', '&:hover': onClick ? { borderColor: 'warning.main', boxShadow: 2 } : {} }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">{zone.name}</Typography>
          {zone.risk_level && <Chip label={zone.risk_level} size="small" color="warning" sx={{ height: 18, fontSize: '0.65rem' }} />}
        </Box>
        <Typography variant="body2" color="text.secondary" fontSize="0.7rem" noWrap>{zone.description}</Typography>
        {zone.time_limit && <Typography variant="caption" fontSize="0.65rem">⏱ {zone.time_limit} мин</Typography>}
      </Stack>
    </CardContent>
  </Card>
)

