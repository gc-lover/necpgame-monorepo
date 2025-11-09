/**
 * Карточка предмета в инвентаре
 * Данные из OpenAPI: InventoryItem
 */
import { Card, CardContent, Typography, Chip, Stack, IconButton, Tooltip } from '@mui/material'
import EquipmentIcon from '@mui/icons-material/Category'
import UseIcon from '@mui/icons-material/MedicalServices'
import DeleteIcon from '@mui/icons-material/Delete'
import type { InventoryItem } from '@/api/generated/inventory/models'

interface ItemCardProps {
  item: InventoryItem
  onEquip?: () => void
  onUse?: () => void
  onDrop?: () => void
}

export function ItemCard({ item, onEquip, onUse, onDrop }: ItemCardProps) {
  const getRarityColor = (rarity?: string) => {
    switch (rarity) {
      case 'legendary': return '#ff9800'
      case 'epic': return '#9c27b0'
      case 'rare': return '#2196f3'
      case 'uncommon': return '#4caf50'
      case 'common': return '#757575'
      default: return '#757575'
    }
  }

  return (
    <Card
      sx={{
        width: '100%',
        minHeight: 120,
        border: '1px solid',
        borderColor: 'divider',
        borderLeft: '3px solid',
        borderLeftColor: getRarityColor(item.rarity),
        '&:hover': {
          borderColor: 'primary.main',
          boxShadow: 2,
        },
      }}
    >
      <CardContent sx={{ p: 1, '&:last-child': { pb: 1 } }}>
        <Stack spacing={0.5}>
          <Stack direction="row" justifyContent="space-between" alignItems="flex-start">
            <Tooltip title={item.description} arrow>
              <Typography variant="subtitle2" sx={{ fontSize: '0.8rem', fontWeight: 'bold' }}>
                {item.name}
              </Typography>
            </Tooltip>
            {item.stackable && item.quantity > 1 && (
              <Chip label={`x${item.quantity}`} size="small" sx={{ height: 16, fontSize: '0.6rem' }} />
            )}
          </Stack>

          <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
            {item.category}
          </Typography>

          <Stack direction="row" spacing={0.3} flexWrap="wrap" sx={{ mt: 0.5 }}>
            {item.questItem && (
              <Chip label="Квест" size="small" color="warning" sx={{ height: 14, fontSize: '0.55rem' }} />
            )}
            {item.rarity && (
              <Chip
                label={item.rarity}
                size="small"
                sx={{
                  height: 14,
                  fontSize: '0.55rem',
                  bgcolor: getRarityColor(item.rarity),
                  color: '#fff',
                }}
              />
            )}
            <Chip label={`${item.weight}кг`} size="small" variant="outlined" sx={{ height: 14, fontSize: '0.55rem' }} />
            {item.value && (
              <Chip label={`${item.value}€$`} size="small" color="success" sx={{ height: 14, fontSize: '0.55rem' }} />
            )}
          </Stack>

          <Stack direction="row" spacing={0.5} sx={{ mt: 0.5 }}>
            {item.equippable && onEquip && (
              <IconButton size="small" onClick={onEquip} color="primary" sx={{ p: 0.3 }}>
                <EquipmentIcon sx={{ fontSize: '0.9rem' }} />
              </IconButton>
            )}
            {item.usable && onUse && (
              <IconButton size="small" onClick={onUse} color="success" sx={{ p: 0.3 }}>
                <UseIcon sx={{ fontSize: '0.9rem' }} />
              </IconButton>
            )}
            {onDrop && !item.questItem && (
              <IconButton size="small" onClick={onDrop} color="error" sx={{ p: 0.3 }}>
                <DeleteIcon sx={{ fontSize: '0.9rem' }} />
              </IconButton>
            )}
          </Stack>
        </Stack>
      </CardContent>
    </Card>
  )
}

