/**
 * Компонент списка слотов имплантов по типу
 * Данные из OpenAPI: ImplantSlots
 */
import { Box, Typography, Grid } from '@mui/material'
import { ImplantSlotItem } from './ImplantSlotItem'
import type { SlotInfo } from '@/api/generated/gameplay/combat/models'

interface ImplantSlotsListProps {
  slots: SlotInfo[]
  typeName: string
  onSlotClick?: (slot: SlotInfo) => void
}

export function ImplantSlotsList({ slots, typeName, onSlotClick }: ImplantSlotsListProps) {
  if (!slots || slots.length === 0) {
    return (
      <Box sx={{ p: 2, textAlign: 'center' }}>
        <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.75rem' }}>
          Нет доступных слотов для {typeName}
        </Typography>
      </Box>
    )
  }

  const occupiedCount = slots.filter((s) => s.is_occupied).length

  return (
    <Box>
      <Typography variant="subtitle1" sx={{ mb: 1.5, fontSize: '0.9rem', fontWeight: 'bold' }}>
        {typeName} ({occupiedCount}/{slots.length})
      </Typography>

      <Grid container spacing={1}>
        {slots.map((slot) => (
          <Grid item xs={6} sm={4} md={3} key={slot.slot_id}>
            <ImplantSlotItem slot={slot} onClick={() => onSlotClick?.(slot)} />
          </Grid>
        ))}
      </Grid>
    </Box>
  )
}

