import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import MusicNoteIcon from '@mui/icons-material/MusicNote'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface CultureLoreSummary {
  theme: string
  influences: string[]
  iconicMedia: string[]
  slangTerms: string[]
}

export interface CultureLoreCardProps {
  culture: CultureLoreSummary
}

export const CultureLoreCard: React.FC<CultureLoreCardProps> = ({ culture }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <MusicNoteIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {culture.theme}
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Influences: {culture.influences.join(', ')}
      </Typography>
      <Stack spacing={0.1}>
        {culture.iconicMedia.slice(0, 2).map((media) => (
          <Typography key={media} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • Media: {media}
          </Typography>
        ))}
      </Stack>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        {culture.slangTerms.slice(0, 4).map((slang) => (
          <Typography key={slang} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            #{slang}
          </Typography>
        ))}
      </Box>
    </Stack>
  </CompactCard>
)

export default CultureLoreCard


