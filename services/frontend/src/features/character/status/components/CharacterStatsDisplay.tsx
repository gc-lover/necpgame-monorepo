/**
 * Отображение характеристик персонажа
 * Данные из OpenAPI: CharacterStats
 */
import { Typography, Stack, Box } from '@mui/material'
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter'
import FlashOnIcon from '@mui/icons-material/FlashOn'
import PsychologyIcon from '@mui/icons-material/Psychology'
import BuildIcon from '@mui/icons-material/Build'
import AcUnitIcon from '@mui/icons-material/AcUnit'
import type { CharacterStats } from '@/api/generated/character-status/models'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

interface CharacterStatsDisplayProps {
  stats: CharacterStats
}

export function CharacterStatsDisplay({ stats }: CharacterStatsDisplayProps) {
  const statItems = [
    { label: 'Сила', value: stats.strength, icon: <FitnessCenterIcon />, color: 'error.main' },
    { label: 'Рефлексы', value: stats.reflexes, icon: <FlashOnIcon />, color: 'warning.main' },
    { label: 'Интеллект', value: stats.intelligence, icon: <PsychologyIcon />, color: 'info.main' },
    { label: 'Технические', value: stats.technical, icon: <BuildIcon />, color: 'success.main' },
    { label: 'Хладнокровие', value: stats.cool, icon: <AcUnitIcon />, color: 'primary.main' },
  ]

  return (
    <CompactCard color="purple" compact>
      <Typography variant="h6" sx={{ fontSize: '0.95rem', fontWeight: 'bold', mb: 2, color: 'primary.main' }}>
        Характеристики
      </Typography>

      <Stack spacing={1.5}>
        {statItems.map((stat) => (
          <Stack key={stat.label} direction="row" justifyContent="space-between" alignItems="center">
            <Stack direction="row" spacing={1} alignItems="center">
              <Box sx={{ color: stat.color, display: 'flex' }}>
                {stat.icon}
              </Box>
              <Typography variant="body2" sx={{ fontSize: '0.8rem' }}>
                {stat.label}
              </Typography>
            </Stack>
            <Box
              sx={{
                bgcolor: 'rgba(0, 247, 255, 0.1)',
                px: 1.5,
                py: 0.5,
                borderRadius: 1,
                minWidth: 40,
                textAlign: 'center',
              }}
            >
              <Typography variant="body2" sx={{ fontSize: '0.9rem', fontWeight: 'bold', color: stat.color }}>
                {stat.value}
              </Typography>
            </Box>
          </Stack>
        ))}
      </Stack>
    </CompactCard>
  )
}

