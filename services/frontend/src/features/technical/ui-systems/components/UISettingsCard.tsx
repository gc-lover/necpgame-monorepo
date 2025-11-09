import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import TuneIcon from '@mui/icons-material/Tune'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface UISettingEntry {
  key: string
  label: string
  value: string | number | boolean
  defaultValue?: string | number | boolean
}

export interface UISettingsSummary {
  presetName: string
  brightness: number
  saturation: number
  accessibility: string[]
  settings: UISettingEntry[]
}

export interface UISettingsCardProps {
  settings: UISettingsSummary
}

export const UISettingsCard: React.FC<UISettingsCardProps> = ({ settings }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TuneIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          UI Settings
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Preset: {settings.presetName}
      </Typography>
      <ProgressBar value={settings.brightness} compact color="green" label="Brightness" />
      <ProgressBar value={settings.saturation} compact color="green" label="Saturation" />
      <Box display="flex" gap={0.3} flexWrap="wrap">
        {settings.accessibility.map((tag) => (
          <Chip key={tag} label={tag} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        ))}
      </Box>
      <Stack spacing={0.1}>
        {settings.settings.slice(0, 4).map((entry) => (
          <Typography key={entry.key} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {entry.label}: {String(entry.value)}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default UISettingsCard


