/**
 * Обзор статуса персонажа
 * Данные из OpenAPI: CharacterStatus
 */
import { Typography, Stack, LinearProgress, Box, Chip } from '@mui/material'
import FavoriteIcon from '@mui/icons-material/Favorite'
import BatteryChargingFullIcon from '@mui/icons-material/BatteryChargingFull'
import PsychologyIcon from '@mui/icons-material/Psychology'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import type { CharacterStatus } from '@/api/generated/character-status/models'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

interface StatusOverviewProps {
  status: CharacterStatus
}

export function StatusOverview({ status }: StatusOverviewProps) {
  const healthPercent = (status.health / status.maxHealth) * 100
  const energyPercent = (status.energy / status.maxEnergy) * 100
  const humanityPercent = (status.humanity / (status.maxHumanity || 100)) * 100
  const expPercent = ((status.experience / (status.nextLevelExperience || 1000)) * 100)

  return (
    <CompactCard color="cyan" compact>
      <Typography variant="h6" sx={{ fontSize: '0.95rem', fontWeight: 'bold', mb: 2, color: 'primary.main' }}>
        Статус персонажа
      </Typography>

      <Stack spacing={2}>
        {/* Здоровье */}
        <Box>
          <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 0.5 }}>
            <Stack direction="row" spacing={0.5} alignItems="center">
              <FavoriteIcon color="error" sx={{ fontSize: '0.9rem' }} />
              <Typography variant="caption" sx={{ fontSize: '0.75rem' }}>Здоровье:</Typography>
            </Stack>
            <Typography variant="caption" sx={{ fontSize: '0.75rem', fontWeight: 'bold' }}>
              {status.health} / {status.maxHealth}
            </Typography>
          </Stack>
          <LinearProgress
            variant="determinate"
            value={healthPercent}
            color="error"
            sx={{ height: 6, borderRadius: 1 }}
          />
        </Box>

        {/* Энергия */}
        <Box>
          <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 0.5 }}>
            <Stack direction="row" spacing={0.5} alignItems="center">
              <BatteryChargingFullIcon color="primary" sx={{ fontSize: '0.9rem' }} />
              <Typography variant="caption" sx={{ fontSize: '0.75rem' }}>Энергия:</Typography>
            </Stack>
            <Typography variant="caption" sx={{ fontSize: '0.75rem', fontWeight: 'bold' }}>
              {status.energy} / {status.maxEnergy}
            </Typography>
          </Stack>
          <LinearProgress
            variant="determinate"
            value={energyPercent}
            color="primary"
            sx={{ height: 6, borderRadius: 1 }}
          />
        </Box>

        {/* Человечность */}
        <Box>
          <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 0.5 }}>
            <Stack direction="row" spacing={0.5} alignItems="center">
              <PsychologyIcon color="secondary" sx={{ fontSize: '0.9rem' }} />
              <Typography variant="caption" sx={{ fontSize: '0.75rem' }}>Человечность:</Typography>
            </Stack>
            <Typography variant="caption" sx={{ fontSize: '0.75rem', fontWeight: 'bold' }}>
              {status.humanity} / {status.maxHumanity || 100}
            </Typography>
          </Stack>
          <LinearProgress
            variant="determinate"
            value={humanityPercent}
            color="secondary"
            sx={{ height: 6, borderRadius: 1 }}
          />
        </Box>

        {/* Уровень и опыт */}
        <Box>
          <Stack direction="row" justifyContent="space-between" alignItems="center" sx={{ mb: 0.5 }}>
            <Stack direction="row" spacing={0.5} alignItems="center">
              <TrendingUpIcon color="success" sx={{ fontSize: '0.9rem' }} />
              <Typography variant="caption" sx={{ fontSize: '0.75rem' }}>Уровень:</Typography>
            </Stack>
            <Chip label={status.level} size="small" color="success" sx={{ height: 18, fontSize: '0.7rem' }} />
          </Stack>
          <LinearProgress
            variant="determinate"
            value={expPercent}
            color="success"
            sx={{ height: 6, borderRadius: 1 }}
          />
          <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary', display: 'block', mt: 0.3 }}>
            Опыт: {status.experience} / {status.nextLevelExperience || '?'}
          </Typography>
        </Box>
      </Stack>
    </CompactCard>
  )
}

