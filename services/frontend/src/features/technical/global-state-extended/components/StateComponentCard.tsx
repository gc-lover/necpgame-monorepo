import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import StorageIcon from '@mui/icons-material/Storage'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface StateComponentSummary {
  component: 'WORLD' | 'FACTIONS' | 'ECONOMY' | 'PLAYER' | 'QUESTS' | 'COMBAT'
  version: number
  health: number
  pendingMutations: number
  drift: number
  lastUpdated: string
}

const componentColor: Record<StateComponentSummary['component'], 'cyan' | 'green' | 'yellow' | 'pink' | 'purple' | 'cyan'> = {
  WORLD: 'cyan',
  FACTIONS: 'green',
  ECONOMY: 'yellow',
  PLAYER: 'pink',
  QUESTS: 'purple',
  COMBAT: 'cyan',
}

export interface StateComponentCardProps {
  state: StateComponentSummary
}

export const StateComponentCard: React.FC<StateComponentCardProps> = ({ state }) => (
  <CompactCard color={componentColor[state.component]} glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <StorageIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {state.component} state
          </Typography>
        </Box>
        <Chip
          label={`v${state.version}`}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <ProgressBar value={state.health} label="Health" color="green" compact />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Pending mutations: {state.pendingMutations}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={state.drift > 5 ? 'warning.main' : 'text.secondary'}>
        Drift: {state.drift}%
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Updated: {state.lastUpdated}
      </Typography>
    </Stack>
  </CompactCard>
)

export default StateComponentCard


