import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import WidgetsIcon from '@mui/icons-material/Widgets'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface UIFeatureSummary {
  id: string
  name: string
  description: string
  unlocked: boolean
  module: string
}

export interface UIFeaturesCardProps {
  features: UIFeatureSummary[]
}

export const UIFeaturesCard: React.FC<UIFeaturesCardProps> = ({ features }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <WidgetsIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          UI Features
        </Typography>
      </Box>
      <Stack spacing={0.2}>
        {features.slice(0, 4).map((feature) => (
          <Box key={feature.id} display="flex" justifyContent="space-between" alignItems="center">
            <Box display="flex" flexDirection="column">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
                {feature.name}
              </Typography>
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                {feature.description}
              </Typography>
            </Box>
            <Chip
              label={feature.unlocked ? 'enabled' : 'locked'}
              size="small"
              color={feature.unlocked ? 'info' : 'default'}
              sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
            />
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default UIFeaturesCard


