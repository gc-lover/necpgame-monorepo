import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import FlashOnIcon from '@mui/icons-material/FlashOn'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface MentorshipAbilitySummary {
  abilityId: string
  name: string
  description: string
  rarity: 'COMMON' | 'RARE' | 'EPIC' | 'LEGENDARY'
  activationCost: string
  cooldown: string
}

const rarityColor: Record<MentorshipAbilitySummary['rarity'], string> = {
  COMMON: '#05ffa1',
  RARE: '#00f7ff',
  EPIC: '#d817ff',
  LEGENDARY: '#ff2a6d',
}

export interface MentorshipAbilityCardProps {
  ability: MentorshipAbilitySummary
}

export const MentorshipAbilityCard: React.FC<MentorshipAbilityCardProps> = ({ ability }) => (
  <CompactCard color="pink" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <FlashOnIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {ability.name}
          </Typography>
        </Box>
        <Chip
          label={ability.rarity}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${rarityColor[ability.rarity]}`,
            color: rarityColor[ability.rarity],
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {ability.description}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Cost: {ability.activationCost} · Cooldown: {ability.cooldown}
      </Typography>
    </Stack>
  </CompactCard>
)

export default MentorshipAbilityCard


