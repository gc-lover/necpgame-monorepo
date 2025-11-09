/**
 * Сетка инвентаря
 * Данные из OpenAPI: InventoryItem[]
 */
import { Grid, Typography, Paper } from '@mui/material'
import { ItemCard } from './ItemCard'
import type { InventoryItem } from '@/api/generated/inventory/models'

interface InventoryGridProps {
  items: InventoryItem[]
  onEquip?: (item: InventoryItem) => void
  onUse?: (item: InventoryItem) => void
  onDrop?: (item: InventoryItem) => void
}

export function InventoryGrid({ items, onEquip, onUse, onDrop }: InventoryGridProps) {
  if (items.length === 0) {
    return (
      <Paper elevation={1} sx={{ p: 2, textAlign: 'center' }}>
        <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.75rem' }}>
          Инвентарь пуст
        </Typography>
      </Paper>
    )
  }

  return (
    <Grid container spacing={1}>
      {items.map((item) => (
        <Grid item xs={12} sm={6} md={4} lg={3} key={item.id}>
          <ItemCard
            item={item}
            onEquip={onEquip ? () => onEquip(item) : undefined}
            onUse={onUse ? () => onUse(item) : undefined}
            onDrop={onDrop ? () => onDrop(item) : undefined}
          />
        </Grid>
      ))}
    </Grid>
  )
}

