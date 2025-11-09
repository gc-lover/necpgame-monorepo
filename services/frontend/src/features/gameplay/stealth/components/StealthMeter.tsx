import React from 'react'
import { Box, Typography, LinearProgress, Stack, Chip } from '@mui/material'
import VisibilityIcon from '@mui/icons-material/Visibility'
import VolumeUpIcon from '@mui/icons-material/VolumeUp'
import LightModeIcon from '@mui/icons-material/LightMode'
import type { StealthStatus } from '@/api/generated/stealth/models'

interface StealthMeterProps {
  status: StealthStatus
}

export const StealthMeter: React.FC<StealthMeterProps> = ({ status }) => {
  const getStealthLevelColor = (level?: string) => {
    switch (level) {
      case 'hidden':
        return 'success'
      case 'suspicious':
        return 'warning'
      case 'detected':
        return 'error'
      case 'combat':
        return 'error'
      default:
        return 'default'
    }
  }

  const getStealthLevelLabel = (level?: string) => {
    switch (level) {
      case 'hidden':
        return 'Скрыт'
      case 'suspicious':
        return 'Подозрение'
      case 'detected':
        return 'Обнаружен'
      case 'combat':
        return 'Бой'
      default:
        return 'Неизвестно'
    }
  }

  return (
    <Stack spacing={1}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
          Скрытность
        </Typography>
        <Chip
          label={getStealthLevelLabel(status.stealth_level)}
          size="small"
          color={getStealthLevelColor(status.stealth_level)}
          sx={{ height: 20, fontSize: '0.7rem' }}
        />
      </Box>

      {/* Visibility */}
      <Box>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={0.3}>
          <Box display="flex" alignItems="center" gap={0.5}>
            <VisibilityIcon sx={{ fontSize: '0.9rem' }} />
            <Typography variant="caption" fontSize="0.7rem">
              Видимость
            </Typography>
          </Box>
          <Typography variant="caption" fontSize="0.7rem" fontWeight="bold">
            {status.visibility || 0}%
          </Typography>
        </Box>
        <LinearProgress variant="determinate" value={status.visibility || 0} color={status.visibility && status.visibility > 70 ? 'error' : 'primary'} sx={{ height: 4 }} />
      </Box>

      {/* Noise */}
      <Box>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={0.3}>
          <Box display="flex" alignItems="center" gap={0.5}>
            <VolumeUpIcon sx={{ fontSize: '0.9rem' }} />
            <Typography variant="caption" fontSize="0.7rem">
              Шум
            </Typography>
          </Box>
          <Typography variant="caption" fontSize="0.7rem" fontWeight="bold">
            {status.noise_level || 0}%
          </Typography>
        </Box>
        <LinearProgress variant="determinate" value={status.noise_level || 0} color={status.noise_level && status.noise_level > 70 ? 'warning' : 'info'} sx={{ height: 4 }} />
      </Box>

      {/* Light */}
      <Box>
        <Box display="flex" justifyContent="space-between" alignItems="center" mb={0.3}>
          <Box display="flex" alignItems="center" gap={0.5}>
            <LightModeIcon sx={{ fontSize: '0.9rem' }} />
            <Typography variant="caption" fontSize="0.7rem">
              Освещение
            </Typography>
          </Box>
          <Typography variant="caption" fontSize="0.7rem" fontWeight="bold">
            {status.light_exposure || 0}%
          </Typography>
        </Box>
        <LinearProgress variant="determinate" value={status.light_exposure || 0} sx={{ height: 4 }} />
      </Box>

      {/* Enemies */}
      {status.enemies_aware && (
        <Box display="flex" justifyContent="space-between">
          <Typography variant="caption" fontSize="0.7rem">
            Врагов в курсе:
          </Typography>
          <Typography variant="caption" fontSize="0.7rem" fontWeight="bold" color={status.enemies_aware.total ? 'error.main' : 'success.main'}>
            {status.enemies_aware.total || 0} (ищут: {status.enemies_aware.searching || 0})
          </Typography>
        </Box>
      )}

      {/* Active Effects */}
      {status.active_effects && status.active_effects.length > 0 && (
        <Box display="flex" gap={0.5} flexWrap="wrap">
          {status.active_effects.map((effect, i) => (
            <Chip key={i} label={effect} size="small" color="primary" sx={{ height: 18, fontSize: '0.65rem' }} />
          ))}
        </Box>
      )}
    </Stack>
  )
}

