/**
 * Компонент списка NPC
 */
import {
  Box,
  Typography,
  Card,
  CardContent,
  CardActionArea,
  Chip,
  Stack,
  Grid,
} from '@mui/material'
import PersonIcon from '@mui/icons-material/Person'
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart'
import AssignmentIcon from '@mui/icons-material/Assignment'
import WarningIcon from '@mui/icons-material/Warning'
import type { GameNPC } from '@/api/generated/game/models'

interface NPCListProps {
  npcs: GameNPC[]
  onSelectNPC?: (npc: GameNPC) => void
}

export function NPCList({ npcs, onSelectNPC }: NPCListProps) {
  const getNPCIcon = (type: string) => {
    switch (type) {
      case 'trader':
        return <ShoppingCartIcon color="primary" />
      case 'quest_giver':
        return <AssignmentIcon color="secondary" />
      case 'enemy':
        return <WarningIcon color="error" />
      default:
        return <PersonIcon color="action" />
    }
  }

  const getNPCTypeLabel = (type: string) => {
    switch (type) {
      case 'trader':
        return 'Торговец'
      case 'quest_giver':
        return 'Квестодатель'
      case 'citizen':
        return 'Житель'
      case 'enemy':
        return 'Враг'
      default:
        return type
    }
  }

  const getNPCTypeColor = (type: string) => {
    switch (type) {
      case 'trader':
        return 'primary'
      case 'quest_giver':
        return 'secondary'
      case 'citizen':
        return 'default'
      case 'enemy':
        return 'error'
      default:
        return 'default'
    }
  }

  if (!npcs || npcs.length === 0) {
    return (
      <Box sx={{ p: 2 }}>
        <Typography variant="body2" sx={{ color: 'text.secondary' }}>
          В этой локации нет доступных персонажей
        </Typography>
      </Box>
    )
  }

  return (
    <Box>
      <Typography variant="h6" gutterBottom sx={{ color: 'primary.main', mb: 2 }}>
        Персонажи в локации
      </Typography>

      <Grid container spacing={2}>
        {npcs.map((npc) => (
          <Grid item xs={12} sm={6} md={4} key={npc.id}>
            <Card
              elevation={2}
              sx={{
                height: '100%',
                border: '1px solid',
                borderColor: 'divider',
                '&:hover': onSelectNPC
                  ? {
                      borderColor: 'primary.main',
                      boxShadow: 4,
                    }
                  : undefined,
              }}
            >
              <CardActionArea
                onClick={() => onSelectNPC?.(npc)}
                disabled={!onSelectNPC}
                sx={{ height: '100%' }}
              >
                <CardContent>
                  <Stack direction="row" spacing={1} alignItems="center" sx={{ mb: 1 }}>
                    {getNPCIcon(npc.type)}
                    <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                      {npc.name}
                    </Typography>
                  </Stack>

                  <Stack direction="row" spacing={1} sx={{ mb: 1.5 }}>
                    <Chip
                      label={getNPCTypeLabel(npc.type)}
                      size="small"
                      color={getNPCTypeColor(npc.type)}
                    />
                    {npc.faction && (
                      <Chip label={npc.faction} size="small" variant="outlined" />
                    )}
                  </Stack>

                  {npc.description && (
                    <Typography variant="body2" sx={{ mb: 1.5, color: 'text.secondary' }}>
                      {npc.description}
                    </Typography>
                  )}

                  <Typography
                    variant="caption"
                    sx={{
                      color: 'text.secondary',
                      fontStyle: 'italic',
                      display: 'block',
                      mt: 1,
                    }}
                  >
                    "{npc.greeting}"
                  </Typography>

                  {npc.availableQuests && npc.availableQuests.length > 0 && (
                    <Chip
                      label={`${npc.availableQuests.length} квест(ов)`}
                      size="small"
                      color="success"
                      sx={{ mt: 1 }}
                    />
                  )}
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  )
}

