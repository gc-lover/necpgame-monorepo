/**
 * Компонент отображения энергетического пула
 * Данные из OpenAPI: EnergyPoolInfo
 */
import { Paper, Typography, LinearProgress, Stack } from '@mui/material'
import BoltIcon from '@mui/icons-material/Bolt'
import type { EnergyPoolInfo } from '@/api/generated/gameplay/combat/models'

interface EnergyPoolDisplayProps {
  energy: EnergyPoolInfo
}

export function EnergyPoolDisplay({ energy }: EnergyPoolDisplayProps) {
  const usagePercent = (energy.used / energy.total_pool) * 100
  const currentPercent = energy.max_level
    ? (energy.current_level / energy.max_level) * 100
    : 100

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
      <Stack direction="row" spacing={0.5} alignItems="center" sx={{ mb: 1 }}>
        <BoltIcon sx={{ fontSize: '0.875rem' }} color="warning" />
        <Typography
          variant="subtitle2"
          sx={{
            color: 'primary.main',
            fontSize: '0.75rem',
            textTransform: 'uppercase',
            letterSpacing: '0.05em',
          }}
        >
          Энергия
        </Typography>
      </Stack>

      {/* Текущий уровень энергии */}
      <Stack spacing={0.5} sx={{ mb: 1 }}>
        <Stack direction="row" justifyContent="space-between">
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
            Уровень:
          </Typography>
          <Typography variant="caption" sx={{ fontSize: '0.7rem', fontWeight: 'bold' }}>
            {energy.current_level}
            {energy.max_level && ` / ${energy.max_level}`}
          </Typography>
        </Stack>
        <LinearProgress
          variant="determinate"
          value={currentPercent}
          sx={{ height: 4, borderRadius: 1 }}
          color="warning"
        />
      </Stack>

      {/* Энергетический пул */}
      <Stack spacing={0.5}>
        <Stack direction="row" justifyContent="space-between">
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
            Пул:
          </Typography>
          <Typography variant="caption" sx={{ fontSize: '0.7rem' }}>
            {energy.used} / {energy.total_pool}
          </Typography>
        </Stack>
        <LinearProgress
          variant="determinate"
          value={Math.min(usagePercent, 100)}
          sx={{ height: 4, borderRadius: 1 }}
          color={usagePercent > 80 ? 'error' : 'primary'}
        />
      </Stack>

      {/* Регенерация */}
      <Typography
        variant="caption"
        sx={{ fontSize: '0.65rem', color: 'text.secondary', mt: 1, display: 'block' }}
      >
        Регенерация: {energy.regen_rate}/сек
      </Typography>
    </Paper>
  )
}

