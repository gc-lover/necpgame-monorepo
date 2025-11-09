import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import TerminalIcon from '@mui/icons-material/Terminal'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface TextActionSummary {
  action: string
  description: string
  command: string
}

export interface TextVersionStateSummary {
  character: {
    name: string
    level: number
    location: string
    hp: number
    hpMax: number
  }
  availableActions: TextActionSummary[]
  currentQuest?: {
    questName: string
    objectives: string[]
  } | null
  inventorySummary: {
    itemsCount: number
    weight: number
  }
  nearbyNPCs: {
    name: string
    canInteract: boolean
  }[]
}

export interface TextVersionStateCardProps {
  state: TextVersionStateSummary
}

export const TextVersionStateCard: React.FC<TextVersionStateCardProps> = ({ state }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <TerminalIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Text Version State
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {state.character.name} · Lvl {state.character.level} · {state.character.location}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        HP: {state.character.hp}/{state.character.hpMax} · Inventory: {state.inventorySummary.itemsCount} items ({state.inventorySummary.weight}kg)
      </Typography>
      {state.currentQuest && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Quest: {state.currentQuest.questName} ({state.currentQuest.objectives.join(', ')})
        </Typography>
      )}
      <Stack spacing={0.2}>
        {state.availableActions.slice(0, 3).map((action) => (
          <Typography key={action.command} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            {action.command}: {action.description}
          </Typography>
        ))}
      </Stack>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        NPCs: {state.nearbyNPCs.map((npc) => `${npc.name}${npc.canInteract ? '' : ' (busy)'}`).join(', ') || '—'}
      </Typography>
    </Stack>
  </CompactCard>
)

export default TextVersionStateCard


