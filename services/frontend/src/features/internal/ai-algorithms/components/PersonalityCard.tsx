import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import PsychologyIcon from '@mui/icons-material/Psychology'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface PersonalityTrait {
  trait: string
  score: number
}

export interface NPCPersonalitySummary {
  npcName: string
  template: string
  faction: string
  region: string
  role: string
  traits: PersonalityTrait[]
  quirks: string[]
}

export interface PersonalityCardProps {
  personality: NPCPersonalitySummary
}

export const PersonalityCard: React.FC<PersonalityCardProps> = ({ personality }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <PsychologyIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          NPC Personality · {personality.npcName}
        </Typography>
      </Box>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        <Chip label={personality.template} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        <Chip label={personality.faction} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        <Chip label={personality.region} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        <Chip label={personality.role} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Stack spacing={0.1}>
        {personality.traits.slice(0, 4).map((trait) => (
          <Typography key={trait.trait} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {trait.trait}: {Math.round(trait.score * 100)}%
          </Typography>
        ))}
      </Stack>
      <Stack spacing={0.2}>
        {personality.quirks.slice(0, 2).map((quirk) => (
          <Typography key={quirk} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Quirk: {quirk}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default PersonalityCard


