import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box, LinearProgress } from '@mui/material'
import type { Daemon } from '@/api/generated/hacking/models'

interface DaemonCardProps {
  daemon: Daemon
  onUse?: (daemonId: string) => void
  disabled?: boolean
}

export const DaemonCard: React.FC<DaemonCardProps> = ({ daemon, onUse, disabled = false }) => {
  const getTierColor = (tier?: number) => {
    if (!tier) return 'default'
    if (tier >= 5) return 'error'
    if (tier >= 3) return 'warning'
    return 'success'
  }

  return (
    <Card
      sx={{
        border: '1px solid',
        borderColor: disabled ? 'action.disabled' : 'primary.main',
        background: disabled
          ? 'rgba(100,100,100,0.1)'
          : 'linear-gradient(135deg, rgba(255,165,0,0.05) 0%, rgba(0,0,0,0) 100%)',
        cursor: onUse && !disabled ? 'pointer' : 'default',
        opacity: disabled ? 0.5 : 1,
        '&:hover': onUse && !disabled ? { borderColor: 'warning.main', transform: 'translateY(-2px)' } : {},
        transition: 'all 0.2s',
      }}
      onClick={() => onUse && !disabled && daemon.daemon_id && onUse(daemon.daemon_id)}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem" color="warning.main">
              {daemon.name || 'Quickhack'}
            </Typography>
            <Chip label={`T${daemon.tier || 1}`} size="small" color={getTierColor(daemon.tier)} sx={{ height: 18, fontSize: '0.65rem' }} />
          </Box>
          <Box display="flex" gap={0.5} flexWrap="wrap">
            <Chip label={daemon.type || 'combat'} size="small" variant="outlined" sx={{ height: 16, fontSize: '0.6rem' }} />
            {daemon.ram_cost && <Chip label={`RAM: ${daemon.ram_cost}`} size="small" color="primary" sx={{ height: 16, fontSize: '0.6rem' }} />}
            {daemon.heat_generation && <Chip label={`Heat: ${daemon.heat_generation}%`} size="small" color="error" sx={{ height: 16, fontSize: '0.6rem' }} />}
          </Box>
          {daemon.cooldown && (
            <Box>
              <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
                Кулдаун: {daemon.cooldown}с
              </Typography>
            </Box>
          )}
          {daemon.requirements && (
            <Box>
              <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
                Требования: {daemon.requirements.class || ''} {daemon.requirements.intelligence ? `INT ${daemon.requirements.intelligence}` : ''} {daemon.requirements.tech ? `TECH ${daemon.requirements.tech}` : ''}
              </Typography>
            </Box>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

