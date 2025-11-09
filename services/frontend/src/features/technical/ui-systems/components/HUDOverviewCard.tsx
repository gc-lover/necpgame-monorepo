import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import RadarIcon from '@mui/icons-material/Radar'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface HUDWidgetSummary {
  widget: string
  enabled: boolean
  position: string
  priority: number
}

export interface HUDOverviewSummary {
  latencyMs: number
  widgetCount: number
  widgets: HUDWidgetSummary[]
}

export interface HUDOverviewCardProps {
  hud: HUDOverviewSummary
}

export const HUDOverviewCard: React.FC<HUDOverviewCardProps> = ({ hud }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <RadarIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          HUD Overview
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Widgets: {hud.widgetCount} · Latency: {hud.latencyMs}ms
      </Typography>
      <ProgressBar value={Math.min(100, hud.widgetCount * 10)} compact color="cyan" label="Load" />
      <Stack spacing={0.2}>
        {hud.widgets.slice(0, 4).map((widget) => (
          <Typography key={widget.widget} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {widget.widget} ({widget.position}) · priority {widget.priority} · {widget.enabled ? 'enabled' : 'disabled'}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default HUDOverviewCard


