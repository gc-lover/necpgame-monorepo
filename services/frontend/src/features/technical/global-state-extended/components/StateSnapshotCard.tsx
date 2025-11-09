import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import CameraIcon from '@mui/icons-material/CameraAlt'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface StateSnapshotSummary {
  snapshotId: string
  createdAt: string
  createdBy: string
  sizeMb: number
  tags: string[]
  rollbackAvailable: boolean
}

export interface StateSnapshotCardProps {
  snapshot: StateSnapshotSummary
}

export const StateSnapshotCard: React.FC<StateSnapshotCardProps> = ({ snapshot }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <CameraIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Snapshot {snapshot.snapshotId.slice(0, 8)}
          </Typography>
        </Box>
        {snapshot.rollbackAvailable && (
          <Chip
            label="Rollback"
            size="small"
            color="info"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        )}
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Created {snapshot.createdAt} by {snapshot.createdBy}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Size: {snapshot.sizeMb} MB
      </Typography>
      <Box display="flex" gap={0.3} flexWrap="wrap">
        {snapshot.tags.slice(0, 3).map((tag) => (
          <Chip key={tag} label={tag} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} />
        ))}
      </Box>
    </Stack>
  </CompactCard>
)

export default StateSnapshotCard


