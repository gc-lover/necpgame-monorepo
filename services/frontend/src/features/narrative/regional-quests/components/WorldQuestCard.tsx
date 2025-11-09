import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import HubIcon from '@mui/icons-material/Hub'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { WorldQuest } from '../types'

export interface WorldQuestCardProps {
  quest: WorldQuest
}

export const WorldQuestCard: React.FC<WorldQuestCardProps> = ({ quest }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <HubIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {quest.name}
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Faction: {quest.faction}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {quest.description}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Region impact: {quest.regionImpact}
      </Typography>
    </Stack>
  </CompactCard>
)

export default WorldQuestCard


