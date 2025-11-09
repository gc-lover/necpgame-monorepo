import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import PeopleIcon from '@mui/icons-material/People'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface CharacterSelectEntry {
  characterId: string
  name: string
  className: string
  level: number
  location: string
  lastPlayed: string
}

export interface CharacterSelectSummary {
  maxSlots: number
  characters: CharacterSelectEntry[]
}

export interface CharacterSelectCardProps {
  data: CharacterSelectSummary
}

export const CharacterSelectCard: React.FC<CharacterSelectCardProps> = ({ data }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <PeopleIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Character Select
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Slots {data.characters.length}/{data.maxSlots}
      </Typography>
      <Stack spacing={0.2}>
        {data.characters.slice(0, 4).map((character) => (
          <Box key={character.characterId} display="flex" flexDirection="column" gap={0.1}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
              {character.name} · {character.className} · Lv {character.level}
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              {character.location} · Last played {character.lastPlayed}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default CharacterSelectCard


