import React from 'react'
import { Card, CardContent, Typography, Chip, Stack, LinearProgress } from '@mui/material'

const TIERS = ['hated', 'hostile', 'unfriendly', 'neutral', 'friendly', 'trusted', 'honored', 'legendary']

export const ReputationTierCard: React.FC<{ faction: string; tier: string; points: number }> = ({ faction, tier, points }) => {
  const tierIndex = TIERS.indexOf(tier)
  const progress = (tierIndex / (TIERS.length - 1)) * 100
  
  return (
    <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">{faction}</Typography>
          <Chip label={tier.toUpperCase()} size="small" color={tierIndex > 3 ? 'success' : tierIndex < 3 ? 'error' : 'default'} sx={{ height: 18, fontSize: '0.65rem', width: 'fit-content' }} />
          <Typography variant="caption" fontSize="0.7rem">Очки: {points}</Typography>
          <LinearProgress variant="determinate" value={progress} sx={{ height: 4, mt: 0.5 }} />
        </Stack>
      </CardContent>
    </Card>
  )
}

