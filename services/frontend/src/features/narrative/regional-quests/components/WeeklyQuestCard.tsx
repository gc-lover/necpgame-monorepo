import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import DateRangeIcon from '@mui/icons-material/DateRange'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { WeeklyQuest } from '../types'

export interface WeeklyQuestCardProps {
  quest: WeeklyQuest
}

export const WeeklyQuestCard: React.FC<WeeklyQuestCardProps> = ({ quest }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <DateRangeIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {quest.name}
        </Typography>
        <Chip label={quest.region} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Power: {quest.recommendedPower}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {quest.description}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Reward: {quest.reward}
      </Typography>
    </Stack>
  </CompactCard>
)

export default WeeklyQuestCard


