import React from 'react'
import { Box, Typography, LinearProgress, Stack, Chip } from '@mui/material'
import { WeaponMasteryProgress } from '@/api/generated/weapons/models'

interface MasteryDisplayProps {
  mastery: WeaponMasteryProgress
  compact?: boolean
}

const rankColors: Record<string, string> = {
  novice: '#9e9e9e',
  adept: '#4caf50',
  expert: '#2196f3',
  master: '#9c27b0',
  legend: '#ff9800',
}

const rankNames: Record<string, string> = {
  novice: 'Новичок',
  adept: 'Адепт',
  expert: 'Эксперт',
  master: 'Мастер',
  legend: 'Легенда',
}

/**
 * Компонент отображения прогресса владения оружием (Weapon Mastery)
 * Соответствует OpenAPI спецификации WeaponMasteryProgress
 */
export const MasteryDisplay: React.FC<MasteryDisplayProps> = ({ mastery, compact = false }) => {
  const rankKey = mastery.rank?.toLowerCase() || 'novice'
  const progress = mastery.kills_to_next_rank
    ? ((mastery.total_kills || 0) / ((mastery.total_kills || 0) + mastery.kills_to_next_rank)) * 100
    : 100

  if (compact) {
    return (
      <Box>
        <Box display="flex" alignItems="center" gap={1} mb={0.5}>
          <Chip
            label={rankNames[rankKey] || mastery.rank}
            size="small"
            sx={{
              bgcolor: rankColors[rankKey],
              color: 'white',
              height: 18,
              fontSize: '0.65rem',
              fontWeight: 'bold',
            }}
          />
          <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
            {mastery.total_kills || 0} убийств
          </Typography>
        </Box>
        <LinearProgress
          variant="determinate"
          value={progress}
          sx={{
            height: 4,
            borderRadius: 2,
            bgcolor: 'action.hover',
            '& .MuiLinearProgress-bar': {
              bgcolor: rankColors[rankKey],
            },
          }}
        />
        {mastery.kills_to_next_rank !== undefined && mastery.kills_to_next_rank > 0 && (
          <Typography variant="caption" fontSize="0.65rem" color="text.disabled">
            До следующего ранга: {mastery.kills_to_next_rank}
          </Typography>
        )}
      </Box>
    )
  }

  return (
    <Box>
      <Stack spacing={1.5}>
        {/* Ранг */}
        <Box>
          <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" mb={0.5}>
            Ранг владения
          </Typography>
          <Chip
            label={rankNames[rankKey] || mastery.rank}
            size="medium"
            sx={{
              bgcolor: rankColors[rankKey],
              color: 'white',
              fontWeight: 'bold',
              fontSize: '0.75rem',
            }}
          />
        </Box>

        {/* Прогресс */}
        <Box>
          <Box display="flex" justifyContent="space-between" mb={0.5}>
            <Typography variant="body2" fontSize="0.75rem">
              Прогресс
            </Typography>
            <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
              {mastery.total_kills || 0}{' '}
              {mastery.kills_to_next_rank !== undefined &&
                `/ ${(mastery.total_kills || 0) + mastery.kills_to_next_rank}`}
            </Typography>
          </Box>
          <LinearProgress
            variant="determinate"
            value={progress}
            sx={{
              height: 8,
              borderRadius: 4,
              bgcolor: 'action.hover',
              '& .MuiLinearProgress-bar': {
                bgcolor: rankColors[rankKey],
                borderRadius: 4,
              },
            }}
          />
          {mastery.kills_to_next_rank !== undefined && mastery.kills_to_next_rank > 0 && (
            <Typography variant="caption" fontSize="0.7rem" color="text.secondary" mt={0.5}>
              До следующего ранга: {mastery.kills_to_next_rank} убийств
            </Typography>
          )}
        </Box>

        {/* Бонусы */}
        {mastery.bonuses && mastery.bonuses.length > 0 && (
          <Box>
            <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" mb={0.5}>
              Активные бонусы
            </Typography>
            <Stack spacing={0.5}>
              {mastery.bonuses.map((bonus, index) => (
                <Box
                  key={index}
                  sx={{
                    bgcolor: 'action.hover',
                    p: 0.75,
                    borderRadius: 1,
                    borderLeft: 3,
                    borderColor: rankColors[rankKey],
                  }}
                >
                  <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
                    {bonus.name}
                  </Typography>
                  {bonus.value !== undefined && (
                    <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
                      +{bonus.value}
                      {bonus.description && ` - ${bonus.description}`}
                    </Typography>
                  )}
                </Box>
              ))}
            </Stack>
          </Box>
        )}
      </Stack>
    </Box>
  )
}

