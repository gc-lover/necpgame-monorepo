import React from 'react'
import { Box, Typography, LinearProgress, Stack } from '@mui/material'
import BoltIcon from '@mui/icons-material/Bolt'

interface StaminaMeterProps {
  current: number
  max: number
  regenRate: number
  availableActions?: {
    jump?: boolean
    climb?: boolean
    slide?: boolean
    grapple?: boolean
  }
}

export const StaminaMeter: React.FC<StaminaMeterProps> = ({ current, max, regenRate, availableActions }) => {
  const staminaPercent = (current / max) * 100

  return (
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.5}>
          <BoltIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
          <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
            Выносливость (STA)
          </Typography>
        </Box>
        <Typography variant="caption" fontSize="0.7rem" fontWeight="bold">
          {current.toFixed(0)} / {max}
        </Typography>
      </Box>
      <LinearProgress variant="determinate" value={staminaPercent} color={staminaPercent > 30 ? 'success' : 'error'} sx={{ height: 8, borderRadius: 1 }} />
      <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
        Восстановление: {regenRate}/сек
      </Typography>
      {availableActions && (
        <Box display="flex" gap={0.5} flexWrap="wrap" mt={0.5}>
          {availableActions.jump !== false && <Typography variant="caption" fontSize="0.65rem" color="success.main">✓ Jump</Typography>}
          {availableActions.climb !== false && <Typography variant="caption" fontSize="0.65rem" color="success.main">✓ Climb</Typography>}
          {availableActions.slide !== false && <Typography variant="caption" fontSize="0.65rem" color="success.main">✓ Slide</Typography>}
          {availableActions.grapple !== false && <Typography variant="caption" fontSize="0.65rem" color="success.main">✓ Grapple</Typography>}
        </Box>
      )}
    </Stack>
  )
}

