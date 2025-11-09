/**
 * Отображение слота экипировки
 * Данные из OpenAPI: EquipmentSlot
 */
import { Paper, Typography, Stack, IconButton, Tooltip, Box } from '@mui/material'
import RemoveCircleIcon from '@mui/icons-material/RemoveCircle'
import type { EquipmentSlot } from '@/api/generated/inventory/models'

interface EquipmentSlotDisplayProps {
  slot: EquipmentSlot
  onUnequip?: () => void
}

export function EquipmentSlotDisplay({ slot, onUnequip }: EquipmentSlotDisplayProps) {
  return (
    <Paper
      elevation={slot.isEmpty ? 0 : 2}
      sx={{
        p: 1,
        border: '1px solid',
        borderColor: slot.isEmpty ? 'divider' : 'primary.main',
        bgcolor: slot.isEmpty ? 'transparent' : 'rgba(0, 247, 255, 0.05)',
        minHeight: 80,
      }}
    >
      <Stack spacing={0.5}>
        <Typography variant="caption" sx={{ fontSize: '0.65rem', textTransform: 'uppercase', color: 'text.secondary' }}>
          {slot.slotName}
        </Typography>

        {slot.isEmpty ? (
          <Typography variant="caption" sx={{ fontSize: '0.65rem', fontStyle: 'italic', color: 'text.disabled' }}>
            Пусто
          </Typography>
        ) : slot.item ? (
          <>
            <Stack direction="row" justifyContent="space-between" alignItems="center">
              <Typography variant="subtitle2" sx={{ fontSize: '0.75rem', fontWeight: 'bold' }}>
                {slot.item.name}
              </Typography>
              {onUnequip && (
                <IconButton size="small" onClick={onUnequip} color="error" sx={{ p: 0.2 }}>
                  <RemoveCircleIcon sx={{ fontSize: '0.8rem' }} />
                </IconButton>
              )}
            </Stack>

            {slot.bonuses && Object.keys(slot.bonuses).length > 0 && (
              <Box sx={{ mt: 0.5 }}>
                <Typography variant="caption" sx={{ fontSize: '0.6rem', color: 'success.main' }}>
                  Бонусы:
                </Typography>
                {Object.entries(slot.bonuses).map(([stat, value]) => (
                  <Tooltip key={stat} title={stat} arrow>
                    <Typography variant="caption" sx={{ fontSize: '0.55rem', color: 'text.secondary', display: 'block' }}>
                      +{value} {stat}
                    </Typography>
                  </Tooltip>
                ))}
              </Box>
            )}
          </>
        ) : null}
      </Stack>
    </Paper>
  )
}

