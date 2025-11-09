import React from 'react'
import {
  Card,
  CardContent,
  Typography,
  Chip,
  Box,
  Stack,
} from '@mui/material'
import WarningIcon from '@mui/icons-material/Warning'
import InfoIcon from '@mui/icons-material/Info'
import DangerousIcon from '@mui/icons-material/Dangerous'
import { RandomEvent, RandomEventDangerLevel } from '@/api/generated/events/models'

interface RandomEventCardProps {
  event: RandomEvent
  onClick?: () => void
}

const dangerLevelConfig: Record<
  RandomEventDangerLevel,
  { label: string; color: 'success' | 'warning' | 'error'; icon: React.ReactNode }
> = {
  LOW: {
    label: 'Низкая опасность',
    color: 'success',
    icon: <InfoIcon fontSize="small" />,
  },
  MEDIUM: {
    label: 'Средняя опасность',
    color: 'warning',
    icon: <WarningIcon fontSize="small" />,
  },
  HIGH: {
    label: 'Высокая опасность',
    color: 'error',
    icon: <DangerousIcon fontSize="small" />,
  },
}

/**
 * Компонент карточки случайного события
 * Отображает информацию о событии согласно OpenAPI спецификации
 */
export const RandomEventCard: React.FC<RandomEventCardProps> = ({ event, onClick }) => {
  const dangerConfig = event.dangerLevel
    ? dangerLevelConfig[event.dangerLevel]
    : undefined

  return (
    <Card
      onClick={onClick}
      sx={{
        cursor: onClick ? 'pointer' : 'default',
        transition: 'all 0.2s',
        border: '1px solid',
        borderColor: 'divider',
        '&:hover': onClick
          ? {
              borderColor: 'primary.main',
              transform: 'translateY(-2px)',
              boxShadow: 2,
            }
          : {},
      }}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={1}>
          {/* Заголовок и уровень опасности */}
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">
              {event.name}
            </Typography>
            {dangerConfig && (
              <Chip
                icon={dangerConfig.icon}
                label={dangerConfig.label}
                size="small"
                color={dangerConfig.color}
                sx={{ height: 20, fontSize: '0.65rem' }}
              />
            )}
          </Box>

          {/* Описание */}
          <Typography variant="body2" color="text.secondary" fontSize="0.75rem">
            {event.description}
          </Typography>

          {/* Ограничение по времени */}
          {event.timeLimit && (
            <Box display="flex" alignItems="center" gap={0.5}>
              <Typography variant="caption" color="warning.main" fontSize="0.7rem">
                ⏱ Ограничение времени: {event.timeLimit} сек
              </Typography>
            </Box>
          )}

          {/* Количество вариантов */}
          <Typography variant="caption" color="text.disabled" fontSize="0.7rem">
            Вариантов действий: {event.options.length}
          </Typography>
        </Stack>
      </CardContent>
    </Card>
  )
}

