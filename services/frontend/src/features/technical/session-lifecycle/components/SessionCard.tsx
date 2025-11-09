import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import PersonIcon from '@mui/icons-material/Person'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { SessionInfo } from '../types'

const statusColor: Record<SessionInfo['status'], string> = {
  ACTIVE: '#05ffa1',
  AFK: '#fef86c',
  DISCONNECTED: '#ff9f43',
  TERMINATED: '#ff2a6d',
}

export interface SessionCardProps {
  session: SessionInfo
}

export const SessionCard: React.FC<SessionCardProps> = ({ session }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <PersonIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Session {session.sessionId}
        </Typography>
        <Chip
          label={session.status}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${statusColor[session.status]}`, color: statusColor[session.status], ml: 'auto' }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Player: {session.playerId} • Character: {session.characterId}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Created: {session.createdAt} • Last heartbeat: {session.lastHeartbeatAt}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Expires: {session.expiresAt}
      </Typography>
    </Stack>
  </CompactCard>
)

export default SessionCard


