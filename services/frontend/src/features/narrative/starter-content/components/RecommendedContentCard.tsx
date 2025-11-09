import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import StarIcon from '@mui/icons-material/Star'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { StarterQuest } from '../types'

export interface RecommendedContentCardProps {
  originQuest: StarterQuest
  classQuests: StarterQuest[]
  tutorialQuests: StarterQuest[]
}

export const RecommendedContentCard: React.FC<RecommendedContentCardProps> = ({ originQuest, classQuests, tutorialQuests }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <StarIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Recommended Start
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Origin: {originQuest.name}
      </Typography>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        {classQuests.slice(0, 2).map((quest) => (
          <Chip key={quest.questId} label={quest.name} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        ))}
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Tutorials: {tutorialQuests.slice(0, 2).map((quest) => quest.name).join(', ')}
      </Typography>
    </Stack>
  </CompactCard>
)

export default RecommendedContentCard



