import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import SettingsIcon from '@mui/icons-material/Settings'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ServiceConfigInfo } from '../types'

export interface ServiceConfigCardProps {
  config: ServiceConfigInfo
}

export const ServiceConfigCard: React.FC<ServiceConfigCardProps> = ({ config }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <SettingsIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {config.serviceName}
        </Typography>
        <Chip label={config.environment} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Version: {config.version}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Keys: {Object.keys(config.configuration).slice(0, 3).join(', ') || '—'}
      </Typography>
    </Stack>
  </CompactCard>
)

export default ServiceConfigCard


