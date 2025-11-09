import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { AlertEvent } from '../types'

const levelColor: Record<AlertEvent['level'], string> = {
  info: '#00f7ff',
  warning: '#fef86c',
  critical: '#ff2a6d',
}

export interface AlertFeedCardProps {
  alerts: AlertEvent[]
}

export const AlertFeedCard: React.FC<AlertFeedCardProps> = ({ alerts }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <NotificationsActiveIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Realtime Alerts
        </Typography>
      </Box>
      {alerts.slice(0, 4).map((alert) => (
        <Box key={alert.id} display="flex" flexDirection="column" gap={0.1}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={levelColor[alert.level]}>
            {alert.level.toUpperCase()} • {alert.raisedAt}
          </Typography>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            {alert.message}
          </Typography>
        </Box>
      ))}
      {alerts.length === 0 && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          SLA alerts отсутствуют
        </Typography>
      )}
    </Stack>
  </CompactCard>
)

export default AlertFeedCard


