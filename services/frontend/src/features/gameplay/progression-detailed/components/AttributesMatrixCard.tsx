import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import GridViewIcon from '@mui/icons-material/GridView'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface AttributeRow {
  attribute: string
  base: number
  growth: number
}

export interface ClassMatrixEntry {
  classId: string
  className: string
  focus: string
  attributes: AttributeRow[]
}

export interface AttributesMatrixCardProps {
  entry: ClassMatrixEntry
}

export const AttributesMatrixCard: React.FC<AttributesMatrixCardProps> = ({ entry }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <GridViewIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {entry.className}
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {entry.focus}
        </Typography>
      </Box>
      <Stack spacing={0.2}>
        {entry.attributes.map((row) => (
          <Box key={row.attribute} display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
              {row.attribute}
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Base {row.base} · Growth +{row.growth}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default AttributesMatrixCard


