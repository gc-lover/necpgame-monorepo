import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box } from '@mui/material'

export const PerkCard: React.FC<{ perk: any; unlocked?: boolean }> = ({ perk, unlocked = false }) => (
  <Card sx={{ border: '1px solid', borderColor: unlocked ? 'success.main' : 'divider', opacity: unlocked ? 1 : 0.7 }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">{perk.name || 'Перк'}</Typography>
          {unlocked && <Chip label="Разблокирован" size="small" color="success" sx={{ height: 18, fontSize: '0.65rem' }} />}
        </Box>
        <Typography variant="caption" fontSize="0.7rem" color="text.secondary">{perk.description || 'Требует детализации API'}</Typography>
      </Stack>
    </CardContent>
  </Card>
)

