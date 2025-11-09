import React from 'react'
import { Typography, Stack, Box, Chip, LinearProgress } from '@mui/material'
import DnsIcon from '@mui/icons-material/Dns'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { RealtimeInstance } from '../types'

const statusColor: Record<RealtimeInstance['status'], string> = {
  ONLINE: '#05ffa1',
  MAINTENANCE: '#fef86c',
  DRAINING: '#ff9f43',
  OFFLINE: '#ff2a6d',
}

export interface RealtimeInstanceCardProps {
  instance: RealtimeInstance
}

export const RealtimeInstanceCard: React.FC<RealtimeInstanceCardProps> = ({ instance }) => {
  const loadPercent = Math.min(100, (instance.activePlayers / instance.maxPlayers) * 100)

  return (
    <CompactCard color="cyan" glowIntensity="weak" compact>
      <Stack spacing={0.5}>
        <Box display="flex" alignItems="center" gap={0.6}>
          <DnsIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {instance.instanceId}
          </Typography>
          <Chip
            label={instance.status}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${statusColor[instance.status]}`, color: statusColor[instance.status], ml: 'auto' }}
          />
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Region: {instance.region} • TickRate: {instance.tickRate}
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Players: {instance.activePlayers}/{instance.maxPlayers} • Zones: {instance.maxZones}
        </Typography>
        <LinearProgress variant="determinate" value={loadPercent} sx={{ height: 4, borderRadius: 2 }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Zone types: {instance.supportedZoneTypes.join(', ')}
        </Typography>
      </Stack>
    </CompactCard>
  )
}

export default RealtimeInstanceCard


