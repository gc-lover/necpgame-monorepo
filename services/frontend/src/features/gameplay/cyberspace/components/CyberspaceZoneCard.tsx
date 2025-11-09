import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box } from '@mui/material'
import type { CyberspaceZone } from '@/api/generated/cyberspace/models'

interface CyberspaceZoneCardProps {
  zone: CyberspaceZone
  onNavigate?: (zoneId: string) => void
}

export const CyberspaceZoneCard: React.FC<CyberspaceZoneCardProps> = ({ zone, onNavigate }) => (
  <Card
    sx={{
      border: '1px solid',
      borderColor: 'primary.main',
      background: 'linear-gradient(135deg, rgba(0,247,255,0.05) 0%, rgba(0,0,0,0) 100%)',
      cursor: onNavigate ? 'pointer' : 'default',
      '&:hover': onNavigate ? { borderColor: 'primary.light', transform: 'translateY(-2px)' } : {},
      transition: 'all 0.2s',
    }}
    onClick={() => onNavigate && zone.zone_id && onNavigate(zone.zone_id)}
  >
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem" color="primary">
            {zone.name}
          </Typography>
          <Stack direction="row" spacing={0.5}>
            <Chip label={zone.type || 'hub'} size="small" color="primary" sx={{ height: 18, fontSize: '0.65rem' }} />
            {zone.is_pvp && <Chip label="PvP" size="small" color="error" sx={{ height: 18, fontSize: '0.65rem' }} />}
          </Stack>
        </Box>
        <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
          {zone.description || 'Киберпространство'}
        </Typography>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="caption" fontSize="0.7rem">
            Игроков: {zone.player_count || 0}
          </Typography>
          <Chip label={zone.access_level || 'basic'} size="small" variant="outlined" sx={{ height: 18, fontSize: '0.65rem' }} />
        </Box>
      </Stack>
    </CardContent>
  </Card>
)

