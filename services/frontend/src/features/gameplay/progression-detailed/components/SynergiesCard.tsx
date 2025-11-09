import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import ScienceIcon from '@mui/icons-material/Science'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface SynergyEntry {
  name: string
  description: string
  bonus: string
}

export interface SynergiesCardProps {
  synergies: SynergyEntry[]
}

export const SynergiesCard: React.FC<SynergiesCardProps> = ({ synergies }) => (
  <CompactCard color="pink" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <ScienceIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Active Synergies
        </Typography>
      </Box>
      <Stack spacing={0.3}>
        {synergies.slice(0, 4).map((synergy) => (
          <Box key={synergy.name} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
                {synergy.name}
              </Typography>
              <Chip
                label={synergy.bonus}
                size="small"
                sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
              />
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              {synergy.description}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default SynergiesCard


