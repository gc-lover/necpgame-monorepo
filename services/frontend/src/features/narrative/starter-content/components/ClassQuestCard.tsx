import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import AssignmentIndIcon from '@mui/icons-material/AssignmentInd'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { StarterQuest } from '../types'

export interface ClassQuestCardProps {
  quest: StarterQuest
}

export const ClassQuestCard: React.FC<ClassQuestCardProps> = ({ quest }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <AssignmentIndIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {quest.name}
        </Typography>
        <Chip label={quest.questType} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {quest.description}
      </Typography>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        {quest.rewards.slice(0, 3).map((reward) => (
          <Chip key={reward} label={reward} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        ))}
      </Box>
    </Stack>
  </CompactCard>
)

export default ClassQuestCard



