import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import BoltIcon from '@mui/icons-material/Bolt'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface CombatAbilitySummary {
  name: string
  category: 'ACTIVE' | 'PASSIVE'
  description: string
  cooldown: string
  synergy: string
}

export interface CombatAbilityCardProps {
  ability: CombatAbilitySummary
}

export const CombatAbilityCard: React.FC<CombatAbilityCardProps> = ({ ability }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <BoltIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {ability.name}
        </Typography>
        <Chip
          label={ability.category}
          size="small"
          color={ability.category === 'ACTIVE' ? 'info' : 'default'}
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, ml: 'auto' }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Cooldown: {ability.cooldown}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {ability.description}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Synergy: {ability.synergy}
      </Typography>
    </Stack>
  </CompactCard>
)

export default CombatAbilityCard


