/**
 * Компонент кнопок действий в игре
 */
import { Box, Button, Stack, Typography } from '@mui/material'
import SearchIcon from '@mui/icons-material/Search'
import ChatIcon from '@mui/icons-material/Chat'
import DirectionsWalkIcon from '@mui/icons-material/DirectionsWalk'
import HotelIcon from '@mui/icons-material/Hotel'
import InventoryIcon from '@mui/icons-material/Inventory'
import type { GameAction } from '@/api/generated/game/models'

interface ActionButtonsProps {
  actions: GameAction[]
  onActionClick?: (action: GameAction) => void
}

export function ActionButtons({ actions, onActionClick }: ActionButtonsProps) {
  const getActionIcon = (actionId: string) => {
    switch (actionId) {
      case 'look-around':
        return <SearchIcon />
      case 'talk-to-npc':
        return <ChatIcon />
      case 'move':
        return <DirectionsWalkIcon />
      case 'rest':
        return <HotelIcon />
      case 'inventory':
        return <InventoryIcon />
      default:
        return null
    }
  }

  if (!actions || actions.length === 0) {
    return null
  }

  return (
    <Box>
      <Typography variant="h6" gutterBottom sx={{ color: 'primary.main', mb: 2 }}>
        Доступные действия
      </Typography>

      <Stack direction="row" spacing={2} flexWrap="wrap" sx={{ gap: 2 }}>
        {actions.map((action) => (
          <Button
            key={action.id}
            variant="outlined"
            startIcon={getActionIcon(action.id)}
            onClick={() => onActionClick?.(action)}
            disabled={action.enabled === false}
            sx={{
              minWidth: 150,
              textTransform: 'none',
            }}
            title={action.description}
          >
            {action.label}
          </Button>
        ))}
      </Stack>
    </Box>
  )
}

