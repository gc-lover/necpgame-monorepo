/**
 * Карточка NPC
 * Данные из OpenAPI: Npc
 */
import { Card, CardContent, Typography, Chip, Stack, Avatar, Box } from '@mui/material'
import PersonIcon from '@mui/icons-material/Person'
import StoreIcon from '@mui/icons-material/Store'
import AssignmentIcon from '@mui/icons-material/Assignment'
import WarningIcon from '@mui/icons-material/Warning'
import type { Npc } from '@/api/generated/npcs/models'

interface NPCCardProps {
  npc: Npc
  onClick: () => void
}

export function NPCCard({ npc, onClick }: NPCCardProps) {
  const getTypeIcon = () => {
    switch (npc.type) {
      case 'trader': return <StoreIcon />
      case 'quest_giver': return <AssignmentIcon />
      case 'enemy': return <WarningIcon color="error" />
      default: return <PersonIcon />
    }
  }

  const getTypeColor = () => {
    switch (npc.type) {
      case 'trader': return 'success'
      case 'quest_giver': return 'primary'
      case 'enemy': return 'error'
      default: return 'default'
    }
  }

  return (
    <Card
      sx={{
        cursor: 'pointer',
        border: '1px solid',
        borderColor: npc.isHostile ? 'error.main' : 'divider',
        '&:hover': {
          borderColor: 'primary.main',
          boxShadow: 2,
          transform: 'translateY(-2px)',
          transition: 'all 0.2s',
        },
      }}
      onClick={onClick}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack direction="row" spacing={1.5} alignItems="center">
          <Avatar sx={{ width: 40, height: 40, bgcolor: `${getTypeColor()}.main` }}>
            {getTypeIcon()}
          </Avatar>
          
          <Box sx={{ flex: 1, minWidth: 0 }}>
            <Typography variant="subtitle2" sx={{ fontSize: '0.875rem', fontWeight: 'bold' }} noWrap>
              {npc.name}
            </Typography>
            
            {npc.description && (
              <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary', display: 'block' }} noWrap>
                {npc.description}
              </Typography>
            )}

            <Stack direction="row" spacing={0.5} sx={{ mt: 0.5 }} flexWrap="wrap">
              <Chip 
                label={npc.type} 
                size="small" 
                color={getTypeColor() as any}
                sx={{ height: 16, fontSize: '0.6rem' }} 
              />
              {npc.level && (
                <Chip 
                  label={`Ур.${npc.level}`} 
                  size="small" 
                  variant="outlined" 
                  sx={{ height: 16, fontSize: '0.6rem' }} 
                />
              )}
              {npc.faction && (
                <Chip 
                  label={npc.faction} 
                  size="small" 
                  color="secondary" 
                  sx={{ height: 16, fontSize: '0.6rem' }} 
                />
              )}
              {npc.isHostile && (
                <Chip 
                  label="Враждебен" 
                  size="small" 
                  color="error" 
                  sx={{ height: 16, fontSize: '0.6rem' }} 
                />
              )}
            </Stack>
          </Box>
        </Stack>
      </CardContent>
    </Card>
  )
}

