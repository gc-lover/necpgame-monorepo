import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import PublicIcon from '@mui/icons-material/Public'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { RegionalQuest } from '../types'

export interface RegionalQuestCardProps {
  quest: RegionalQuest
}

export const RegionalQuestCard: React.FC<RegionalQuestCardProps> = ({ quest }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <PublicIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {quest.name}
        </Typography>
        <Chip label={quest.region} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }} />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {quest.summary}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Faction: {quest.faction}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Min level: {quest.minLevel} · Repeatable: {quest.repeatable ? 'Yes' : 'No'}
      </Typography>
    </Stack>
  </CompactCard>
)

export default RegionalQuestCard


