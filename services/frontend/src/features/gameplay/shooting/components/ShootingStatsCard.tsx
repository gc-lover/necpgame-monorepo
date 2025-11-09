import React from 'react'
import { Card, CardContent, Typography, Box, Stack, Chip } from '@mui/material'

export const ShootingStatsCard: React.FC<{ stats: any }> = ({ stats }) => (
  <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">Статы оружия</Typography>
        <Box display="flex" gap={1} flexWrap="wrap">
          {stats?.damage && <Chip label={`Урон: ${stats.damage}`} size="small" color="error" sx={{ height: 18, fontSize: '0.65rem' }} />}
          {stats?.accuracy && <Chip label={`Точность: ${stats.accuracy}`} size="small" color="success" sx={{ height: 18, fontSize: '0.65rem' }} />}
          {stats?.fire_rate && <Chip label={`Скорострельность: ${stats.fire_rate}`} size="small" color="info" sx={{ height: 18, fontSize: '0.65rem' }} />}
        </Box>
      </Stack>
    </CardContent>
  </Card>
)

