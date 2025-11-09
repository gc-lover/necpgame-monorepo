import React from 'react'
import { Card, CardContent, Typography, Chip, Box, Stack } from '@mui/material'
import { Ability } from '@/api/generated/abilities/models'

interface AbilityCardProps {
  ability: Ability
  onClick?: () => void
}

const typeColors: Record<string, 'primary' | 'secondary' | 'success' | 'warning' | 'error'> = {
  offensive: 'error',
  defensive: 'primary',
  utility: 'secondary',
  mobility: 'success',
  control: 'warning',
}

const sourceLabels: Record<string, string> = {
  equipment: 'Экипировка',
  implants: 'Импланты',
  skills: 'Навыки',
  cyberdeck: 'Кибердека',
}

/**
 * Компактная карточка способности
 * Отображает информацию согласно OpenAPI спецификации
 */
export const AbilityCard: React.FC<AbilityCardProps> = ({ ability, onClick }) => {
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
          {/* Название и тип */}
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">
              {ability.name}
            </Typography>
            {ability.type && (
              <Chip
                label={ability.type}
                size="small"
                color={typeColors[ability.type] || 'default'}
                sx={{ height: 20, fontSize: '0.65rem', textTransform: 'uppercase' }}
              />
            )}
          </Box>

          {/* Описание */}
          <Typography variant="body2" color="text.secondary" fontSize="0.7rem" noWrap>
            {ability.description}
          </Typography>

          {/* Источник */}
          {ability.source?.type && (
            <Typography variant="caption" color="primary" fontSize="0.7rem">
              {sourceLabels[ability.source.type] || ability.source.type}
            </Typography>
          )}

          {/* Кулдаун и стоимость */}
          <Box display="flex" gap={2}>
            {ability.cooldown?.base !== undefined && (
              <Typography variant="caption" fontSize="0.65rem" color="text.disabled">
                ⏱ {ability.cooldown.base}с
              </Typography>
            )}
            {ability.cost && (
              <Typography variant="caption" fontSize="0.65rem" color="warning.main">
                ⚡ {ability.cost.amount} {ability.cost.resource}
              </Typography>
            )}
          </Box>
        </Stack>
      </CardContent>
    </Card>
  )
}

