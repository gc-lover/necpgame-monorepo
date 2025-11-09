import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import BackpackIcon from '@mui/icons-material/Backpack'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface StarterItemSummary {
  itemId: string
  quantity: number
}

export interface StarterNPCSummary {
  npcId: string
  name: string
  location: string
  role: string
}

export interface InitialDataCardProps {
  starterItems: StarterItemSummary[]
  starterQuests: string[]
  starterLocations: string[]
  npcs: StarterNPCSummary[]
}

export const InitialDataCard: React.FC<InitialDataCardProps> = ({ starterItems, starterQuests, starterLocations, npcs }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <BackpackIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Initial Game Data
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Starter items: {starterItems.map((item) => `${item.itemId.slice(0, 6)}×${item.quantity}`).join(', ')}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Starter quests: {starterQuests.join(', ') || '—'}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Locations: {starterLocations.join(', ') || '—'}
      </Typography>
      <Stack spacing={0.2}>
        {npcs.slice(0, 3).map((npc) => (
          <Typography key={npc.npcId} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            {npc.name} · {npc.location} ({npc.role})
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default InitialDataCard


