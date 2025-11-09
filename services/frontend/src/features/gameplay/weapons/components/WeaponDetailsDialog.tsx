import React from 'react'
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Typography,
  Box,
  Stack,
  Chip,
  Divider,
  Grid,
} from '@mui/material'
import { WeaponDetails } from '@/api/generated/weapons/models'

interface WeaponDetailsDialogProps {
  open: boolean
  weapon: WeaponDetails | null
  onClose: () => void
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
 * Диалог с детальной информацией об оружии
 * Соответствует OpenAPI спецификации WeaponDetails
 */
export const WeaponDetailsDialog: React.FC<WeaponDetailsDialogProps> = ({
  open,
  weapon,
  onClose,
}) => {
  if (!weapon) return null

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
      <DialogTitle sx={{ pb: 1 }}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="h6" fontSize="1rem">
            {weapon.name}
          </Typography>
          <Chip
            label={weapon.rarity}
            size="small"
            color={rarityColors[weapon.rarity] || 'default'}
            sx={{ textTransform: 'uppercase' }}
          />
        </Box>
        {weapon.brand && (
          <Typography variant="caption" color="primary" fontSize="0.75rem" fontWeight="bold">
            {weapon.brand.toUpperCase()}
          </Typography>
        )}
      </DialogTitle>
      <DialogContent sx={{ pt: 1 }}>
        <Stack spacing={2}>
          {/* Описание */}
          {weapon.description && (
            <Box>
              <Typography variant="body2" fontSize="0.75rem">
                {weapon.description}
              </Typography>
            </Box>
          )}

          {/* Лор */}
          {weapon.lore && (
            <Box sx={{ bgcolor: 'action.hover', p: 1.5, borderRadius: 1 }}>
              <Typography variant="caption" color="text.secondary" fontSize="0.7rem" fontStyle="italic">
                {weapon.lore}
              </Typography>
            </Box>
          )}

          <Divider />

          {/* Класс и подкласс */}
          <Box>
            <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" mb={0.5}>
              Тип оружия
            </Typography>
            <Typography variant="body2" fontSize="0.75rem">
              {classNames[weapon.weapon_class] || weapon.weapon_class}
              {weapon.subclass && ` (${weapon.subclass})`}
            </Typography>
          </Box>

          {/* Характеристики */}
          {weapon.stats && (
            <Box>
              <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" mb={1}>
                Характеристики
              </Typography>
              <Grid container spacing={1}>
                {weapon.stats.damage !== undefined && (
                  <Grid item xs={6}>
                    <Typography variant="caption" color="text.disabled" fontSize="0.7rem">
                      Урон:
                    </Typography>
                    <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
                      {weapon.stats.damage}
                    </Typography>
                  </Grid>
                )}
                {weapon.stats.fire_rate !== undefined && (
                  <Grid item xs={6}>
                    <Typography variant="caption" color="text.disabled" fontSize="0.7rem">
                      Скорострельность:
                    </Typography>
                    <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
                      {weapon.stats.fire_rate}/с
                    </Typography>
                  </Grid>
                )}
                {weapon.stats.magazine_size !== undefined && (
                  <Grid item xs={6}>
                    <Typography variant="caption" color="text.disabled" fontSize="0.7rem">
                      Обойма:
                    </Typography>
                    <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
                      {weapon.stats.magazine_size}
                    </Typography>
                  </Grid>
                )}
                {weapon.stats.reload_time !== undefined && (
                  <Grid item xs={6}>
                    <Typography variant="caption" color="text.disabled" fontSize="0.7rem">
                      Перезарядка:
                    </Typography>
                    <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
                      {weapon.stats.reload_time}с
                    </Typography>
                  </Grid>
                )}
                {weapon.stats.accuracy !== undefined && (
                  <Grid item xs={6}>
                    <Typography variant="caption" color="text.disabled" fontSize="0.7rem">
                      Точность:
                    </Typography>
                    <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
                      {weapon.stats.accuracy}%
                    </Typography>
                  </Grid>
                )}
                {weapon.stats.range_effective !== undefined && (
                  <Grid item xs={6}>
                    <Typography variant="caption" color="text.disabled" fontSize="0.7rem">
                      Эфф. дальность:
                    </Typography>
                    <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
                      {weapon.stats.range_effective}м
                    </Typography>
                  </Grid>
                )}
                {weapon.stats.crit_chance !== undefined && (
                  <Grid item xs={6}>
                    <Typography variant="caption" color="text.disabled" fontSize="0.7rem">
                      Шанс крита:
                    </Typography>
                    <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
                      {weapon.stats.crit_chance}%
                    </Typography>
                  </Grid>
                )}
                {weapon.stats.crit_damage !== undefined && (
                  <Grid item xs={6}>
                    <Typography variant="caption" color="text.disabled" fontSize="0.7rem">
                      Урон крита:
                    </Typography>
                    <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
                      {weapon.stats.crit_damage}%
                    </Typography>
                  </Grid>
                )}
              </Grid>
            </Box>
          )}

          {/* Специальные способности */}
          {weapon.special_abilities && weapon.special_abilities.length > 0 && (
            <Box>
              <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" mb={1}>
                Специальные способности
              </Typography>
              <Stack spacing={1}>
                {weapon.special_abilities.map((ability, index) => (
                  <Box key={index} sx={{ bgcolor: 'action.hover', p: 1, borderRadius: 1 }}>
                    <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
                      {ability.name}
                      {ability.cooldown !== undefined && ` (КД: ${ability.cooldown}с)`}
                    </Typography>
                    {ability.description && (
                      <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
                        {ability.description}
                      </Typography>
                    )}
                  </Box>
                ))}
              </Stack>
            </Box>
          )}

          {/* Слоты для модов */}
          {weapon.mod_slots !== undefined && weapon.mod_slots > 0 && (
            <Box>
              <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
                Слотов для модов: {weapon.mod_slots}
              </Typography>
            </Box>
          )}

          {/* Требования */}
          {weapon.requirements && (
            <Box>
              <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold" mb={0.5}>
                Требования
              </Typography>
              {weapon.requirements.level && (
                <Typography variant="body2" fontSize="0.75rem">
                  Уровень: {weapon.requirements.level}
                </Typography>
              )}
              {weapon.requirements.attributes && (
                <Typography variant="body2" fontSize="0.75rem">
                  Атрибуты: {JSON.stringify(weapon.requirements.attributes)}
                </Typography>
              )}
            </Box>
          )}
        </Stack>
      </DialogContent>
      <DialogActions sx={{ p: 2, pt: 0 }}>
        <Button onClick={onClose} variant="contained" size="small" sx={{ fontSize: '0.75rem' }}>
          Закрыть
        </Button>
      </DialogActions>
    </Dialog>
  )
}

