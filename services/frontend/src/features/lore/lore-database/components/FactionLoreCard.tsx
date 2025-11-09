import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import GroupsIcon from '@mui/icons-material/Groups'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface FactionLoreSummary {
  name: string
  type: 'GANG' | 'CORPORATION' | 'GOVERNMENT' | 'UNIQUE'
  region: string
  influence: number
  foundedYear: number
  keywords: string[]
}

const typeColor: Record<FactionLoreSummary['type'], string> = {
  GANG: '#ff2a6d',
  CORPORATION: '#00f7ff',
  GOVERNMENT: '#fef86c',
  UNIQUE: '#d817ff',
}

export interface FactionLoreCardProps {
  faction: FactionLoreSummary
}

export const FactionLoreCard: React.FC<FactionLoreCardProps> = ({ faction }) => (
  <CompactCard color="pink" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <GroupsIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {faction.name}
        </Typography>
        <Chip
          label={faction.type}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${typeColor[faction.type]}`,
            color: typeColor[faction.type],
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Region: {faction.region} · Founded: {faction.foundedYear}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Influence Index: {Math.round(faction.influence * 100)}
      </Typography>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        {faction.keywords.slice(0, 4).map((kw) => (
          <Chip key={kw} label={kw} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        ))}
      </Box>
    </Stack>
  </CompactCard>
)

export default FactionLoreCard


