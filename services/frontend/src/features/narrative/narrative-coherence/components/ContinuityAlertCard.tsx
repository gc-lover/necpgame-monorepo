import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import ReportIcon from '@mui/icons-material/Report'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ContinuityAlertSummary {
  alertId: string
  title: string
  severity: 'INFO' | 'WARNING' | 'CRITICAL'
  description: string
  recommendedAction: string
  detectedAt: string
}

const severityColor: Record<ContinuityAlertSummary['severity'], string> = {
  INFO: '#00f7ff',
  WARNING: '#fef86c',
  CRITICAL: '#ff2a6d',
}

export interface ContinuityAlertCardProps {
  alert: ContinuityAlertSummary
}

export const ContinuityAlertCard: React.FC<ContinuityAlertCardProps> = ({ alert }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <ReportIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {alert.title}
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} sx={{ color: severityColor[alert.severity] }}>
          {alert.severity}
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {alert.description}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Action: {alert.recommendedAction}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Detected: {alert.detectedAt}
      </Typography>
    </Stack>
  </CompactCard>
)

export default ContinuityAlertCard


