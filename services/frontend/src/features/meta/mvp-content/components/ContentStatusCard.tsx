import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import HighlightOffIcon from '@mui/icons-material/HighlightOff'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface SystemStatusSummary {
  quest_engine: boolean
  combat: boolean
  progression: boolean
  social: boolean
  economy: boolean
}

export interface ContentStatusCardProps {
  ready: boolean
  totalQuests: number
  totalLocations: number
  totalNPCs: number
  systemsReady: SystemStatusSummary
}

export const ContentStatusCard: React.FC<ContentStatusCardProps> = ({ ready, totalQuests, totalLocations, totalNPCs, systemsReady }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        {ready ? (
          <CheckCircleIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        ) : (
          <HighlightOffIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        )}
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          MVP Content Status
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Quests: {totalQuests} · Locations: {totalLocations} · NPCs: {totalNPCs}
      </Typography>
      <Stack spacing={0.2}>
        {Object.entries(systemsReady).map(([system, status]) => (
          <Typography key={system} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color={status ? 'success.main' : 'error.main'}>
            {system.replace('_', ' ')}: {status ? 'ready' : 'pending'}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ContentStatusCard


