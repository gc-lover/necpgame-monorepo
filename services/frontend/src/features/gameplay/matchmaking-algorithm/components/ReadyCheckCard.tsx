import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import AccessTimeFilledIcon from '@mui/icons-material/AccessTimeFilled'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ReadyCheckState } from '../types'

export interface ReadyCheckCardProps {
  state: ReadyCheckState
}

export const ReadyCheckCard: React.FC<ReadyCheckCardProps> = ({ state }) => {
  const totalParticipants = state.accepted + state.declined + state.pending
  const acceptedPercent = totalParticipants === 0 ? 0 : (state.accepted / totalParticipants) * 100

  return (
    <CompactCard color="yellow" glowIntensity="weak" compact>
      <Stack spacing={0.5}>
        <Box display="flex" alignItems="center" gap={0.6}>
          <AccessTimeFilledIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Ready Check • {state.matchId}
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Expires in {state.expiresInSeconds}s · Pending {state.pending}
        </Typography>
        <ProgressBar value={acceptedPercent} compact color="green" label="Accepted" customText={`${state.accepted}/${totalParticipants}`} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Declined: {state.declined}
        </Typography>
      </Stack>
    </CompactCard>
  )
}

export default ReadyCheckCard


