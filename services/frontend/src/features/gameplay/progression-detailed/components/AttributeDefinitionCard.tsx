import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import TimelineIcon from '@mui/icons-material/Timeline'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface AttributeDefinitionSummary {
  code: string
  name: string
  description: string
  growthType: 'LINEAR' | 'EXPONENTIAL' | 'SOFT_CAP'
  softCap?: number
  hardCap?: number
  synergySkills: string[]
}

export interface AttributeDefinitionCardProps {
  attribute: AttributeDefinitionSummary
}

const growthColor: Record<AttributeDefinitionSummary['growthType'], string> = {
  LINEAR: '#00f7ff',
  EXPONENTIAL: '#fef86c',
  SOFT_CAP: '#05ffa1',
}

export const AttributeDefinitionCard: React.FC<AttributeDefinitionCardProps> = ({ attribute }) => (
  <CompactCard color="cyan" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <TimelineIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {attribute.name} ({attribute.code})
          </Typography>
        </Box>
        <Chip
          label={attribute.growthType}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${growthColor[attribute.growthType]}`,
            color: growthColor[attribute.growthType],
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {attribute.description}
      </Typography>
      {(attribute.softCap ?? attribute.hardCap) && (
        <ProgressBar
          value={attribute.softCap ?? attribute.hardCap ?? 0}
          label="Soft Cap"
          color="purple"
          compact
          customText={`Soft ${attribute.softCap ?? '—'} · Hard ${attribute.hardCap ?? '—'}`}
        />
      )}
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary" fontWeight={600}>
        Synergies
      </Typography>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        {attribute.synergySkills.slice(0, 5).map((skill) => (
          <Chip key={skill} label={skill} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        ))}
      </Box>
    </Stack>
  </CompactCard>
)

export default AttributeDefinitionCard


