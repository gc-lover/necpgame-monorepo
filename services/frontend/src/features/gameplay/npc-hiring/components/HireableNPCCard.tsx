import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box, Button } from '@mui/material'
import PersonAddIcon from '@mui/icons-material/PersonAdd'
import type { HireableNPC } from '@/api/generated/npc-hiring/models'

interface HireableNPCCardProps {
  npc: HireableNPC
  onHire?: (npcId: string) => void
  canAfford?: boolean
}

export const HireableNPCCard: React.FC<HireableNPCCardProps> = ({ npc, onHire, canAfford = true }) => {
  const getTierColor = (tier?: number) => {
    if (!tier) return 'default'
    if (tier >= 5) return 'error'
    if (tier >= 3) return 'warning'
    return 'success'
  }

  const getTypeColor = (type?: string) => {
    switch (type) {
      case 'combat':
        return 'error'
      case 'vendor':
        return 'warning'
      case 'specialist':
        return 'primary'
      default:
        return 'default'
    }
  }

  return (
    <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">
              {npc.name}
            </Typography>
            <Chip label={`T${npc.tier || 1}`} size="small" color={getTierColor(npc.tier)} sx={{ height: 18, fontSize: '0.65rem' }} />
          </Box>
          <Box display="flex" gap={0.5}>
            <Chip label={npc.type || 'NPC'} size="small" color={getTypeColor(npc.type)} sx={{ height: 16, fontSize: '0.6rem' }} />
            <Chip label={npc.role || 'role'} size="small" variant="outlined" sx={{ height: 16, fontSize: '0.6rem' }} />
          </Box>
          <Box display="flex" justifyContent="space-between" mt={0.5}>
            <Typography variant="caption" fontSize="0.7rem">
              Стоимость:
            </Typography>
            <Typography variant="body2" fontSize="0.75rem" fontWeight="bold" color="warning.main">
              €${npc.cost_daily}/день
            </Typography>
          </Box>
          {npc.reputation_required && (
            <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
              Требуется репутация: {npc.reputation_required}
            </Typography>
          )}
          {npc.skills && (
            <Box display="flex" gap={0.5} flexWrap="wrap" mt={0.5}>
              {Object.entries(npc.skills).map(([skill, value]) => (
                <Chip key={skill} label={`${skill}: ${value}`} size="small" sx={{ height: 16, fontSize: '0.6rem' }} />
              ))}
            </Box>
          )}
          {onHire && (
            <Button
              startIcon={<PersonAddIcon />}
              onClick={() => npc.npc_id && onHire(npc.npc_id)}
              size="small"
              variant="contained"
              fullWidth
              disabled={!canAfford}
              sx={{ mt: 1, fontSize: '0.7rem' }}
            >
              Нанять
            </Button>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

