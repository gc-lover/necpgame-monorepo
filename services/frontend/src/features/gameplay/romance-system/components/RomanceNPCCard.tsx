import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface RomanceNPCSummary {
  npcId: string
  name: string
  region: string
  orientation: 'HETERO' | 'HOMO' | 'BI' | 'PAN'
  romanceDifficulty: 'EASY' | 'MEDIUM' | 'HARD' | 'VERY_HARD'
  compatibilityScore: number
  personalityTraits: string[]
  interests: string[]
  currentStatus?: 'STRANGER' | 'ACQUAINTANCE' | 'FRIEND' | 'DATING' | 'COMMITTED' | null
}

const difficultyColor: Record<RomanceNPCSummary['romanceDifficulty'], string> = {
  EASY: '#05ffa1',
  MEDIUM: '#00f7ff',
  HARD: '#fef86c',
  VERY_HARD: '#ff2a6d',
}

export interface RomanceNPCCardProps {
  npc: RomanceNPCSummary
}

export const RomanceNPCCard: React.FC<RomanceNPCCardProps> = ({ npc }) => (
  <CompactCard color="pink" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <FavoriteBorderIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {npc.name}
          </Typography>
        </Box>
        <Chip
          label={`${npc.compatibilityScore}%`}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            bgcolor: 'rgba(255, 42, 109, 0.18)',
            color: '#ff2a6d',
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {npc.region} · {npc.orientation}
      </Typography>
      <ProgressBar value={npc.compatibilityScore} label="Compatibility" color="pink" compact />
      <Box display="flex" gap={0.4} flexWrap="wrap">
        <Chip
          label={`Difficulty: ${npc.romanceDifficulty}`}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${difficultyColor[npc.romanceDifficulty]}`,
            color: difficultyColor[npc.romanceDifficulty],
          }}
        />
        {npc.currentStatus && (
          <Chip
            label={npc.currentStatus}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        )}
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary" fontWeight={600}>
        Traits
      </Typography>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        {npc.personalityTraits.slice(0, 4).map((trait) => (
          <Chip key={trait} label={trait} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        ))}
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary" fontWeight={600}>
        Interests
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {npc.interests.slice(0, 3).join(', ')}
        {npc.interests.length > 3 ? '…' : ''}
      </Typography>
    </Stack>
  </CompactCard>
)

export default RomanceNPCCard


