import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import FavoriteIcon from '@mui/icons-material/Favorite'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface RomanceRelationshipSummary {
  relationshipId: string
  npcName: string
  stage: 'MEETING' | 'FRIENDSHIP' | 'FLIRTING' | 'DATING' | 'INTIMACY' | 'CONFLICT' | 'RECONCILIATION' | 'COMMITMENT' | 'BREAKUP'
  affectionLevel: number
  trustLevel: number
  jealousyLevel: number
  eventsCompleted: number
  startedAt: string
}

const stageColor: Partial<Record<RomanceRelationshipSummary['stage'], string>> = {
  MEETING: '#00f7ff',
  FRIENDSHIP: '#05ffa1',
  FLIRTING: '#fef86c',
  DATING: '#ff2a6d',
  INTIMACY: '#d817ff',
  CONFLICT: '#ff8f2a',
  RECONCILIATION: '#05ffa1',
  COMMITMENT: '#37FFB4',
  BREAKUP: '#ff2a6d',
}

export interface RomanceRelationshipCardProps {
  relationship: RomanceRelationshipSummary
}

export const RomanceRelationshipCard: React.FC<RomanceRelationshipCardProps> = ({ relationship }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <FavoriteIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {relationship.npcName}
          </Typography>
        </Box>
        <Chip
          label={relationship.stage}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${stageColor[relationship.stage] ?? '#ff2a6d'}`,
            color: stageColor[relationship.stage] ?? '#ff2a6d',
          }}
        />
      </Box>
      <ProgressBar value={relationship.affectionLevel} label="Affection" color="pink" compact />
      <ProgressBar value={relationship.trustLevel} label="Trust" color="cyan" compact />
      <ProgressBar value={relationship.jealousyLevel} label="Jealousy" color="yellow" compact />
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Events: {relationship.eventsCompleted}
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Since: {new Date(relationship.startedAt).toLocaleDateString()}
        </Typography>
      </Box>
    </Stack>
  </CompactCard>
)

export default RomanceRelationshipCard


