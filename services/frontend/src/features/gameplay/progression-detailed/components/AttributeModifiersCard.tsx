import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import BoltIcon from '@mui/icons-material/Bolt'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface AttributeModifier {
  attribute: string
  total: number
  base: number
  equipment: number
  buffs: number
}

export interface AttributeModifiersCardProps {
  modifiers: AttributeModifier[]
}

export const AttributeModifiersCard: React.FC<AttributeModifiersCardProps> = ({ modifiers }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <BoltIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Attribute Modifiers
        </Typography>
      </Box>
      <Stack spacing={0.3}>
        {modifiers.map((modifier) => (
          <Box key={modifier.attribute} display="flex" flexDirection="column" gap={0.15}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
              {modifier.attribute} · {modifier.total}
            </Typography>
            <ProgressBar
              value={Math.min(100, modifier.total)}
              compact
              color="green"
              customText={`Base ${modifier.base} · Equip ${modifier.equipment} · Buff ${modifier.buffs}`}
            />
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default AttributeModifiersCard


