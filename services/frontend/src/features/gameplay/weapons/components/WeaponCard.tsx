import React from 'react'
import { Card, CardContent, Typography, Chip, Box, Stack } from '@mui/material'
import { WeaponSummary } from '@/api/generated/weapons/models'

interface WeaponCardProps {
  weapon: WeaponSummary
  onClick?: () => void
}

const rarityColors: Record<string, 'default' | 'success' | 'info' | 'warning' | 'error'> = {
  common: 'default',
  uncommon: 'success',
  rare: 'info',
  epic: 'warning',
  legendary: 'error',
  iconic: 'error',
}

const classNames: Record<string, string> = {
  pistol: 'Пистолет',
  assault_rifle: 'Штурмовая винтовка',
  shotgun: 'Дробовик',
  sniper_rifle: 'Снайперская винтовка',
  smg: 'Пистолет-пулемёт',
  lmg: 'Лёгкий пулемёт',
  melee: 'Ближний бой',
  cyberware: 'Кибероружие',
}

/**
 * Компактная карточка оружия для каталога
 * Отображает основную информацию согласно OpenAPI спецификации
 */
export const WeaponCard: React.FC<WeaponCardProps> = ({ weapon, onClick }) => {
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
          {/* Название и редкость */}
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">
              {weapon.name}
            </Typography>
            <Chip
              label={weapon.rarity}
              size="small"
              color={rarityColors[weapon.rarity] || 'default'}
              sx={{ height: 20, fontSize: '0.65rem', textTransform: 'uppercase' }}
            />
          </Box>

          {/* Класс оружия */}
          <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
            {classNames[weapon.weapon_class] || weapon.weapon_class}
          </Typography>

          {/* Бренд */}
          {weapon.brand && (
            <Typography variant="caption" color="primary" fontSize="0.7rem" fontWeight="bold">
              {weapon.brand.toUpperCase()}
            </Typography>
          )}

          {/* Характеристики */}
          {(weapon.damage !== undefined || weapon.fire_rate !== undefined) && (
            <Box display="flex" gap={2}>
              {weapon.damage !== undefined && (
                <Box>
                  <Typography variant="caption" color="text.disabled" fontSize="0.65rem">
                    Урон:
                  </Typography>
                  <Typography variant="caption" fontSize="0.7rem" fontWeight="bold" ml={0.5}>
                    {weapon.damage}
                  </Typography>
                </Box>
              )}
              {weapon.fire_rate !== undefined && (
                <Box>
                  <Typography variant="caption" color="text.disabled" fontSize="0.65rem">
                    Скорость:
                  </Typography>
                  <Typography variant="caption" fontSize="0.7rem" fontWeight="bold" ml={0.5}>
                    {weapon.fire_rate}/с
                  </Typography>
                </Box>
              )}
            </Box>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

