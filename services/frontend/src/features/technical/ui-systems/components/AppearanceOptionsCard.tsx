import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import FaceRetouchingNaturalIcon from '@mui/icons-material/FaceRetouchingNatural'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface AppearanceCategorySummary {
  category: string
  options: number
  presets: number
}

export interface AppearanceOptionsSummary {
  totalCategories: number
  dnaLocking: boolean
  categories: AppearanceCategorySummary[]
}

export interface AppearanceOptionsCardProps {
  options: AppearanceOptionsSummary
}

export const AppearanceOptionsCard: React.FC<AppearanceOptionsCardProps> = ({ options }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <FaceRetouchingNaturalIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Appearance Options
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Categories: {options.totalCategories}
      </Typography>
      {options.dnaLocking && (
        <Chip label="DNA locking" size="small" color="secondary" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
      )}
      <Stack spacing={0.2}>
        {options.categories.slice(0, 4).map((category) => (
          <Typography key={category.category} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {category.category}: {category.options} options · {category.presets} presets
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default AppearanceOptionsCard


