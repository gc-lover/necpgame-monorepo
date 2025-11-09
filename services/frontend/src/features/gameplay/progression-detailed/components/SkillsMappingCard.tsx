import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface SkillsMappingCardProps {
  mappingType: 'TO_ITEMS' | 'TO_IMPLANTS' | 'TO_CLASSES'
  entries: {
    source: string
    targets: string[]
  }[]
}

const typeLabel: Record<SkillsMappingCardProps['mappingType'], string> = {
  TO_ITEMS: 'Skills → Items',
  TO_IMPLANTS: 'Skills → Implants',
  TO_CLASSES: 'Skills → Classes',
}

export const SkillsMappingCard: React.FC<SkillsMappingCardProps> = ({ mappingType, entries }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <AutoAwesomeIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {typeLabel[mappingType]}
        </Typography>
      </Box>
      <Stack spacing={0.3}>
        {entries.slice(0, 4).map((entry) => (
          <Box key={entry.source} display="flex" flexDirection="column" gap={0.1}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
              {entry.source}
            </Typography>
            <Box display="flex" gap={0.3} flexWrap="wrap">
              {entry.targets.slice(0, 4).map((target) => (
                <Chip key={target} label={target} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
              ))}
            </Box>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default SkillsMappingCard


