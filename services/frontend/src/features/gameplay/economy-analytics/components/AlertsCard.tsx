import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface AlertItem {
  alertId?: string
  itemName?: string
  alertType?: string
  targetPrice?: number
  notificationMethod?: string
}

export interface AlertsCardProps {
  alerts?: AlertItem[]
}

export const AlertsCard: React.FC<AlertsCardProps> = ({ alerts = [] }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <NotificationsActiveIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Active Alerts
          </Typography>
        </Box>
        <Chip
          label={alerts.length}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      {alerts.length === 0 ? (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Активных оповещений нет
        </Typography>
      ) : (
        <Stack spacing={0.3}>
          {alerts.slice(0, 4).map((alert) => (
            <Box key={alert.alertId ?? alert.itemName} display="flex" flexDirection="column" gap={0.1}>
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                {alert.itemName ?? 'Item'} • {alert.alertType ?? 'price'}
              </Typography>
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                Target: {alert.targetPrice ?? 0}¥ • {alert.notificationMethod ?? 'in_game'}
              </Typography>
            </Box>
          ))}
        </Stack>
      )}
    </Stack>
  </CompactCard>
)

export default AlertsCard

