import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import LanguageIcon from '@mui/icons-material/Language'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ServerInfoSummary {
  serverId: string
  name: string
  region: string
  population: 'LOW' | 'MEDIUM' | 'HIGH' | 'FULL'
  ping: number
  status: 'ONLINE' | 'MAINTENANCE' | 'QUEUE'
}

const statusColor: Record<ServerInfoSummary['status'], string> = {
  ONLINE: '#05ffa1',
  MAINTENANCE: '#fef86c',
  QUEUE: '#ff2a6d',
}

export interface ServerListCardProps {
  servers: ServerInfoSummary[]
}

export const ServerListCard: React.FC<ServerListCardProps> = ({ servers }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <LanguageIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Server Selection
        </Typography>
      </Box>
      <Stack spacing={0.3}>
        {servers.slice(0, 4).map((server) => (
          <Box key={server.serverId} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
                {server.name} · {server.region}
              </Typography>
              <Chip
                label={server.status}
                size="small"
                sx={{
                  height: 16,
                  fontSize: cyberpunkTokens.fonts.xs,
                  border: `1px solid ${statusColor[server.status]}`,
                  color: statusColor[server.status],
                }}
              />
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Population: {server.population} · Ping: {server.ping}ms
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ServerListCard


