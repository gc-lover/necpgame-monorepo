import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import EventRepeatIcon from '@mui/icons-material/EventRepeat'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { DailyQuest } from '../types'

export interface DailyQuestCardProps {
  quest: DailyQuest
}

const difficultyColor: Record<DailyQuest['difficulty'], string> = {
  EASY: '#05ffa1',
  NORMAL: '#fef86c',
  HARD: '#ff2a6d',
}

export const DailyQuestCard: React.FC<DailyQuestCardProps> = ({ quest }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <EventRepeatIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {quest.name}
        </Typography>
        <Chip
          label={quest.difficulty}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${difficultyColor[quest.difficulty]}`, color: difficultyColor[quest.difficulty], ml: 'auto' }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {quest.objective}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Reward: {quest.reward}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Reset: {quest.resetsAt}
      </Typography>
    </Stack>
  </CompactCard>
)

export default DailyQuestCard


