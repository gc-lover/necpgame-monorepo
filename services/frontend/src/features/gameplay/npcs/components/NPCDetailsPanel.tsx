/**
 * Панель с деталями NPC
 * Данные из OpenAPI: Npc
 */
import { Paper, Typography, Stack, Chip, Avatar, Box, Divider } from '@mui/material'
import PersonIcon from '@mui/icons-material/Person'
import LocationOnIcon from '@mui/icons-material/LocationOn'
import type { Npc } from '@/api/generated/npcs/models'

interface NPCDetailsPanelProps {
  npc: Npc
}

export function NPCDetailsPanel({ npc }: NPCDetailsPanelProps) {
  return (
    <Paper elevation={2} sx={{ p: 2 }}>
      <Stack spacing={2}>
        {/* Аватар и имя */}
        <Stack direction="row" spacing={1.5} alignItems="center">
          <Avatar sx={{ width: 50, height: 50 }}>
            <PersonIcon />
          </Avatar>
          <Box sx={{ flex: 1 }}>
            <Typography variant="h6" sx={{ fontSize: '0.95rem', fontWeight: 'bold' }}>
              {npc.name}
            </Typography>
            <Chip 
              label={npc.type} 
              size="small" 
              color="primary" 
              sx={{ height: 18, fontSize: '0.65rem', mt: 0.5 }} 
            />
          </Box>
        </Stack>

        <Divider />

        {/* Информация */}
        <Stack spacing={1}>
          {npc.description && (
            <Box>
              <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
                Описание:
              </Typography>
              <Typography variant="body2" sx={{ fontSize: '0.8rem' }}>
                {npc.description}
              </Typography>
            </Box>
          )}

          <Stack direction="row" spacing={0.5} alignItems="center">
            <LocationOnIcon sx={{ fontSize: '0.9rem', color: 'text.secondary' }} />
            <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
              Локация: {npc.locationId}
            </Typography>
          </Stack>

          {npc.level && (
            <Box>
              <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
                Уровень: {npc.level}
              </Typography>
            </Box>
          )}

          {npc.faction && (
            <Box>
              <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
                Фракция: <Chip label={npc.faction} size="small" sx={{ height: 16, fontSize: '0.6rem' }} />
              </Typography>
            </Box>
          )}

          {npc.isHostile && (
            <Chip 
              label="⚠️ Враждебен" 
              size="small" 
              color="error" 
              sx={{ fontSize: '0.7rem' }} 
            />
          )}
        </Stack>
      </Stack>
    </Paper>
  )
}

