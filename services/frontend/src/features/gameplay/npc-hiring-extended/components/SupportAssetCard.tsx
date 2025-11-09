import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import LocalShippingIcon from '@mui/icons-material/LocalShipping'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface SupportAssetSummary {
  assetId: string
  name: string
  type: 'DRONE' | 'VEHICLE' | 'ARMOR' | 'TECH'
  status: 'READY' | 'IN_REPAIR' | 'DEPLOYED'
  upkeepCost: number
  capacity: number
  utilization: number
  bonuses: string[]
}

const typeChipColor: Record<SupportAssetSummary['type'], 'cyan' | 'yellow' | 'pink' | 'purple'> = {
  DRONE: 'cyan',
  VEHICLE: 'yellow',
  ARMOR: 'pink',
  TECH: 'purple',
}

export interface SupportAssetCardProps {
  asset: SupportAssetSummary
}

export const SupportAssetCard: React.FC<SupportAssetCardProps> = ({ asset }) => (
  <CompactCard color={typeChipColor[asset.type]} glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <LocalShippingIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {asset.name}
          </Typography>
        </Box>
        <Chip
          label={asset.status}
          size="small"
          color={asset.status === 'READY' ? 'success' : asset.status === 'DEPLOYED' ? 'info' : 'warning'}
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Upkeep: {asset.upkeepCost}¥ · Capacity: {asset.capacity}
      </Typography>
      <ProgressBar
        value={asset.utilization}
        label="Utilization"
        color="yellow"
        compact
        customText={`${asset.utilization}%`}
      />
      <Stack spacing={0.2}>
        {asset.bonuses.map((bonus) => (
          <Typography key={bonus} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {bonus}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default SupportAssetCard


