import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import SmartToyIcon from '@mui/icons-material/SmartToy'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface EnemyAISummary {
  name: string
  tier: 'BASIC' | 'ADVANCED' | 'ELITE'
  aggression: number
  tactics: string[]
  weaknesses: string[]
}

const tierColor: Record<EnemyAISummary['tier'], 'cyan' | 'yellow' | 'pink'> = {
  BASIC: 'cyan',
  ADVANCED: 'yellow',
  ELITE: 'pink',
}

export interface EnemyAICardProps {
  enemy: EnemyAISummary
}

export const EnemyAICard: React.FC<EnemyAICardProps> = ({ enemy }) => (
  <CompactCard color={tierColor[enemy.tier]} glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <SmartToyIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {enemy.name}
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {enemy.tier}
        </Typography>
      </Box>
      <ProgressBar value={enemy.aggression} compact color={tierColor[enemy.tier]} label="Aggression" customText={`${enemy.aggression}%`} />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Tactics: {enemy.tactics.slice(0, 2).join(', ')}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Weaknesses: {enemy.weaknesses.slice(0, 2).join(', ')}
      </Typography>
    </Stack>
  </CompactCard>
)

export default EnemyAICard

