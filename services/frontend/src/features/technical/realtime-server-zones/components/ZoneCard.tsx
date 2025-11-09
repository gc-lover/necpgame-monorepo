import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import MapIcon from '@mui/icons-material/Map'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ZoneSummary } from '../types'

const statusColor: Record<ZoneSummary['status'], string> = {
  ONLINE: '#05ffa1',
  MAINTENANCE: '#fef86c',
  MIGRATING: '#ff9f43',
  OFFLINE: '#ff2a6d',
}

export interface ZoneCardProps {
  zone: ZoneSummary
}

export const ZoneCard: React.FC<ZoneCardProps> = ({ zone }) => {
  const totalEntities = zone.playerCount + zone.npcCount

  return (
    <CompactCard color="purple" glowIntensity="weak" compact>
      <Stack spacing={0.5}>
        <Box display="flex" alignItems="center" gap={0.6}>
          <MapIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {zone.zoneName}
          </Typography>
          <Chip
            label={zone.status}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${statusColor[zone.status]}`, color: statusColor[zone.status], ml: 'auto' }}
          />
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Assigned: {zone.assignedServerId} • PvP: {zone.isPvpEnabled ? 'ON' : 'OFF'}
        </Typography>
        <ProgressBar
          value={Math.min(100, (zone.playerCount / Math.max(1, totalEntities)) * 100)}
          compact
          color="cyan"
          label="Players"
          customText={`${zone.playerCount}`}
        />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          NPCs: {zone.npcCount}
        </Typography>
      </Stack>
    </CompactCard>
  )
}

export default ZoneCard


