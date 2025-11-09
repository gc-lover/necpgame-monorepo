import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import MovieFilterIcon from '@mui/icons-material/MovieFilter'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { MainStoryQuest } from '../types'

export interface MainStoryQuestCardProps {
  quest: MainStoryQuest
}

export const MainStoryQuestCard: React.FC<MainStoryQuestCardProps> = ({ quest }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <MovieFilterIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {quest.name}
        </Typography>
        <Chip label={`Chapter ${quest.chapter}`} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Period: {quest.period}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {quest.description}
      </Typography>
      <Stack spacing={0.1}>
        {quest.objectives.slice(0, 3).map((objective) => (
          <Typography key={objective} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {objective}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default MainStoryQuestCard



