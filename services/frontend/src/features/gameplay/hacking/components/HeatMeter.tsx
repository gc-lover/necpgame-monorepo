import React from 'react'
import { Box, Typography, LinearProgress, Stack } from '@mui/material'
import WhatshotIcon from '@mui/icons-material/Whatshot'

interface HeatMeterProps {
  currentHeat: number
  maxHeat: number
  coolingRate: number
  overheatThreshold: number
}

export const HeatMeter: React.FC<HeatMeterProps> = ({ currentHeat, maxHeat, coolingRate, overheatThreshold }) => {
  const heatPercent = (currentHeat / maxHeat) * 100
  const isOverheating = currentHeat >= overheatThreshold

  const getHeatColor = () => {
    if (isOverheating) return 'error'
    if (heatPercent >= 70) return 'warning'
    return 'primary'
  }

  return (
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.5}>
          <WhatshotIcon sx={{ fontSize: '1rem', color: isOverheating ? 'error.main' : 'warning.main' }} />
          <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" color={isOverheating ? 'error.main' : 'inherit'}>
            Перегрев кибердека
          </Typography>
        </Box>
        <Typography variant="caption" fontSize="0.7rem" fontWeight="bold">
          {currentHeat.toFixed(1)}% / {maxHeat}%
        </Typography>
      </Box>
      <LinearProgress variant="determinate" value={heatPercent} color={getHeatColor()} sx={{ height: 8, borderRadius: 1 }} />
      <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
        Охлаждение: {coolingRate}%/сек | Порог: {overheatThreshold}%
      </Typography>
      {isOverheating && (
        <Typography variant="caption" fontSize="0.7rem" color="error.main" fontWeight="bold">
          WARNING ПЕРЕГРЕВ! Quickhacks недоступны
        </Typography>
      )}
    </Stack>
  )
}

