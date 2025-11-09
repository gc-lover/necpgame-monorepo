import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import MilitaryTechIcon from '@mui/icons-material/MilitaryTech'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ClassBonusEntry {
  bonus: string
  value: string
  description?: string
}

export interface ClassBonusesCardProps {
  className: string
  focus: string
  bonuses: ClassBonusEntry[]
}

export const ClassBonusesCard: React.FC<ClassBonusesCardProps> = ({ className, focus, bonuses }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <MilitaryTechIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {className}
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {focus}
        </Typography>
      </Box>
      <Stack spacing={0.25}>
        {bonuses.map((bonus) => (
          <Box key={`${bonus.bonus}-${bonus.value}`} display="flex" flexDirection="column" gap={0.05}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
              {bonus.bonus}: {bonus.value}
            </Typography>
            {bonus.description && (
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                {bonus.description}
              </Typography>
            )}
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ClassBonusesCard


