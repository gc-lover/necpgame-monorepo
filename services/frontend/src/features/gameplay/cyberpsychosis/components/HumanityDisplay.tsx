/**
 * Компонент отображения уровня человечности
 * Данные из OpenAPI: HumanityInfo
 */
import { Paper, Typography, LinearProgress, Stack, Chip } from '@mui/material'
import FavoriteIcon from '@mui/icons-material/Favorite'
import WarningAmberIcon from '@mui/icons-material/WarningAmber'
import type { HumanityInfo } from '@/api/generated/gameplay/cyberpsychosis/models'

interface HumanityDisplayProps {
  humanity: HumanityInfo
}

export function HumanityDisplay({ humanity }: HumanityDisplayProps) {
  const getStageColor = (stage: string) => {
    switch (stage) {
      case 'stable':
        return 'success'
      case 'anxious':
        return 'info'
      case 'dissociative':
        return 'warning'
      case 'cyberpsycho':
        return 'error'
      default:
        return 'default'
    }
  }

  const getStageName = (stage: string) => {
    switch (stage) {
      case 'stable':
        return 'Стабильно'
      case 'anxious':
        return 'Тревожность'
      case 'dissociative':
        return 'Диссоциация'
      case 'cyberpsycho':
        return 'Киберпсихоз!'
      default:
        return stage
    }
  }

  return (
    <Paper
      elevation={2}
      sx={{
        p: 1.5,
        backgroundColor: 'background.paper',
        border: '2px solid',
        borderColor: humanity.loss_percentage > 50 ? 'error.main' : 'divider',
      }}
    >
      <Stack direction="row" spacing={0.5} alignItems="center" sx={{ mb: 1 }}>
        <FavoriteIcon sx={{ fontSize: '0.875rem' }} color="error" />
        <Typography
          variant="subtitle2"
          sx={{
            color: 'primary.main',
            fontSize: '0.75rem',
            textTransform: 'uppercase',
            letterSpacing: '0.05em',
          }}
        >
          Человечность
        </Typography>
        {humanity.loss_percentage > 70 && (
          <WarningAmberIcon sx={{ fontSize: '0.875rem' }} color="error" />
        )}
      </Stack>

      {/* Прогресс человечности */}
      <Stack spacing={0.5} sx={{ mb: 1 }}>
        <Stack direction="row" justifyContent="space-between">
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
            Уровень:
          </Typography>
          <Typography variant="caption" sx={{ fontSize: '0.7rem', fontWeight: 'bold' }}>
            {humanity.current} / {humanity.max}
          </Typography>
        </Stack>
        <LinearProgress
          variant="determinate"
          value={(humanity.current / humanity.max) * 100}
          sx={{ height: 6, borderRadius: 1 }}
          color={humanity.current > 50 ? 'success' : humanity.current > 25 ? 'warning' : 'error'}
        />
      </Stack>

      {/* Потеря человечности */}
      <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 1 }}>
        <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
          Потеря:
        </Typography>
        <Chip
          label={`${humanity.loss_percentage.toFixed(1)}%`}
          size="small"
          color={humanity.loss_percentage > 50 ? 'error' : 'warning'}
          sx={{ height: 18, fontSize: '0.65rem' }}
        />
      </Stack>

      {/* Стадия киберпсихоза */}
      <Stack direction="row" justifyContent="space-between" alignItems="center">
        <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
          Стадия:
        </Typography>
        <Chip
          label={getStageName(humanity.stage)}
          size="small"
          color={getStageColor(humanity.stage)}
          sx={{ height: 18, fontSize: '0.65rem', fontWeight: 'bold' }}
        />
      </Stack>
    </Paper>
  )
}

