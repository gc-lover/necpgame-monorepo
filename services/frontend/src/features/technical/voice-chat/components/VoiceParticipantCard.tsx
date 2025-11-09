import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import VolumeUpIcon from '@mui/icons-material/VolumeUp'
import VolumeOffIcon from '@mui/icons-material/VolumeOff'
import HearingDisabledIcon from '@mui/icons-material/HearingDisabled'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { VoiceParticipant } from '../types'

const roleColor: Record<VoiceParticipant['role'], string> = {
  leader: '#ff9f43',
  moderator: '#00f7ff',
  member: '#05ffa1',
}

export interface VoiceParticipantCardProps {
  participant: VoiceParticipant
}

export const VoiceParticipantCard: React.FC<VoiceParticipantCardProps> = ({ participant }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        {participant.muted ? (
          <VolumeOffIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        ) : (
          <VolumeUpIcon sx={{ fontSize: '1rem', color: participant.speaking ? 'success.main' : 'secondary.main' }} />
        )}
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {participant.displayName}
        </Typography>
        <Chip
          label={participant.role}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${roleColor[participant.role]}`, color: roleColor[participant.role], ml: 'auto' }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Latency: {participant.latencyMs} ms • Speaking: {participant.speaking ? 'YES' : 'NO'}
      </Typography>
      {participant.deafened && (
        <Box display="flex" alignItems="center" gap={0.3}>
          <HearingDisabledIcon sx={{ fontSize: '0.9rem', color: 'warning.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="warning.main">
            Deafened
          </Typography>
        </Box>
      )}
    </Stack>
  </CompactCard>
)

export default VoiceParticipantCard


