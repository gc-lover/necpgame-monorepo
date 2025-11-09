import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import MemoryIcon from '@mui/icons-material/Memory'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface TechnologyLoreSummary {
  name: string
  eraIntroduced: number
  riskLevel: number
  description: string
  keySystems: string[]
}

export interface TechnologyLoreCardProps {
  technology: TechnologyLoreSummary
}

export const TechnologyLoreCard: React.FC<TechnologyLoreCardProps> = ({ technology }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <MemoryIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {technology.name}
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Introduced: {technology.eraIntroduced}
      </Typography>
      <ProgressBar value={technology.riskLevel} compact color="purple" label="Risk" customText={`${technology.riskLevel}%`} />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {technology.description}
      </Typography>
      <Stack spacing={0.1}>
        {technology.keySystems.slice(0, 3).map((system) => (
          <Typography key={system} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {system}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default TechnologyLoreCard


