import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import StoryIcon from '@mui/icons-material/AutoStories'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { OriginStory } from '../types'

export interface OriginStoryCardProps {
  origin: OriginStory
}

export const OriginStoryCard: React.FC<OriginStoryCardProps> = ({ origin }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <StoryIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {origin.name}
        </Typography>
        <Chip label={origin.recommendedClass} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {origin.description}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Start in: {origin.startingLocation}
      </Typography>
    </Stack>
  </CompactCard>
)

export default OriginStoryCard



