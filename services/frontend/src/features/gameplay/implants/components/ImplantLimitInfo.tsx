/**
 * Компонент отображения лимитов имплантов
 * Данные из OpenAPI: ImplantLimits
 */
import { Paper, Typography, LinearProgress, Stack, Chip } from '@mui/material'
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline'
import RemoveCircleOutlineIcon from '@mui/icons-material/RemoveCircleOutline'
import type { ImplantLimits } from '@/api/generated/gameplay/combat/models'

interface ImplantLimitInfoProps {
  limits: ImplantLimits
}

export function ImplantLimitInfo({ limits }: ImplantLimitInfoProps) {
  const usagePercent = (limits.used_slots / limits.current_limit) * 100

  return (
    <Paper
      elevation={2}
      sx={{
        p: 1.5,
        backgroundColor: 'background.paper',
        border: '1px solid',
        borderColor: 'divider',
      }}
    >
      <Typography
        variant="subtitle2"
        gutterBottom
        sx={{
          color: 'primary.main',
          fontSize: '0.75rem',
          textTransform: 'uppercase',
          letterSpacing: '0.05em',
          mb: 1,
        }}
      >
        Лимит имплантов
      </Typography>

      {/* Прогресс использования */}
      <Stack spacing={0.5} sx={{ mb: 1.5 }}>
        <Stack direction="row" justifyContent="space-between">
          <Typography variant="caption" sx={{ fontSize: '0.7rem' }}>
            Использовано: {limits.used_slots} / {limits.current_limit}
          </Typography>
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'success.main' }}>
            Свободно: {limits.available_slots}
          </Typography>
        </Stack>
        <LinearProgress
          variant="determinate"
          value={Math.min(usagePercent, 100)}
          sx={{ height: 4, borderRadius: 1 }}
          color={usagePercent > 80 ? 'error' : usagePercent > 50 ? 'warning' : 'success'}
        />
      </Stack>

      {/* Детали лимита */}
      <Stack spacing={0.5}>
        <Stack direction="row" justifyContent="space-between" alignItems="center">
          <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
            Базовый:
          </Typography>
          <Typography variant="caption" sx={{ fontSize: '0.7rem', fontWeight: 'bold' }}>
            {limits.base_limit}
          </Typography>
        </Stack>

        {limits.bonus_from_class !== undefined && limits.bonus_from_class > 0 && (
          <Stack direction="row" justifyContent="space-between" alignItems="center">
            <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
              Бонус (класс):
            </Typography>
            <Chip
              icon={<AddCircleOutlineIcon sx={{ fontSize: '0.7rem' }} />}
              label={`+${limits.bonus_from_class}`}
              size="small"
              color="success"
              sx={{ height: 16, fontSize: '0.6rem' }}
            />
          </Stack>
        )}

        {limits.bonus_from_progression !== undefined && limits.bonus_from_progression > 0 && (
          <Stack direction="row" justifyContent="space-between" alignItems="center">
            <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
              Бонус (прокачка):
            </Typography>
            <Chip
              icon={<AddCircleOutlineIcon sx={{ fontSize: '0.7rem' }} />}
              label={`+${limits.bonus_from_progression}`}
              size="small"
              color="info"
              sx={{ height: 16, fontSize: '0.6rem' }}
            />
          </Stack>
        )}

        {limits.humanity_penalty !== undefined && limits.humanity_penalty < 0 && (
          <Stack direction="row" justifyContent="space-between" alignItems="center">
            <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
              Штраф (человечность):
            </Typography>
            <Chip
              icon={<RemoveCircleOutlineIcon sx={{ fontSize: '0.7rem' }} />}
              label={limits.humanity_penalty}
              size="small"
              color="error"
              sx={{ height: 16, fontSize: '0.6rem' }}
            />
          </Stack>
        )}
      </Stack>
    </Paper>
  )
}

