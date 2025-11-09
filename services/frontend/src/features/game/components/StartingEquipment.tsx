/**
 * Компонент отображения стартового снаряжения
 */
import { Typography, List, ListItem, ListItemIcon, ListItemText, Paper } from '@mui/material'
import InventoryIcon from '@mui/icons-material/Inventory'
import type { GameStartingItem } from '@/api/generated/game/models'

interface StartingEquipmentProps {
  equipment: GameStartingItem[]
}

export function StartingEquipment({ equipment }: StartingEquipmentProps) {
  if (!equipment || equipment.length === 0) {
    return null
  }

  return (
    <Paper
      elevation={2}
      sx={{
        p: 1.5,
        backgroundColor: 'background.paper',
        border: '1px solid',
        borderColor: 'divider',
      }}
    >
      <Typography 
        variant="subtitle2" 
        gutterBottom 
        sx={{ 
          color: 'primary.main',
          fontSize: '0.75rem',
          textTransform: 'uppercase',
          letterSpacing: '0.05em',
          mb: 1,
        }}
      >
        Снаряжение
      </Typography>

      <List dense disablePadding>
        {equipment.map((item, index) => (
          <ListItem key={`${item.itemId}-${index}`} sx={{ py: 0.5, px: 0 }}>
            <ListItemIcon sx={{ minWidth: 28 }}>
              <InventoryIcon color="primary" sx={{ fontSize: '0.875rem' }} />
            </ListItemIcon>
            <ListItemText
              primary={item.itemId}
              secondary={`x${item.quantity}`}
              primaryTypographyProps={{ color: 'text.primary', fontSize: '0.75rem' }}
              secondaryTypographyProps={{ color: 'text.secondary', fontSize: '0.65rem' }}
            />
          </ListItem>
        ))}
      </List>
    </Paper>
  )
}

