import React from 'react'
import { Typography, Stack, Box, Chip, LinearProgress } from '@mui/material'
import HeadsetMicIcon from '@mui/icons-material/HeadsetMic'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { VoiceChannelSummary } from '../types'

const typeColor: Record<VoiceChannelSummary['channelType'], string> = {
  party: '#05ffa1',
  guild: '#00f7ff',
  raid: '#ff9f43',
  proximity: '#fef86c',
  custom: '#d817ff',
}

export interface VoiceChannelCardProps {
  channel: VoiceChannelSummary
}

export const VoiceChannelCard: React.FC<VoiceChannelCardProps> = ({ channel }) => {
  const usage = Math.min(100, (channel.participants / channel.maxParticipants) * 100)
  return (
    <CompactCard color="cyan" glowIntensity="weak" compact>
      <Stack spacing={0.5}>
        <Box display="flex" alignItems="center" gap={0.6}>
          <HeadsetMicIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {channel.channelName}
          </Typography>
          <Chip
            label={channel.channelType.toUpperCase()}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs, border: `1px solid ${typeColor[channel.channelType]}`, color: typeColor[channel.channelType], ml: 'auto' }}
          />
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Owner: {channel.owner} • Active: {channel.isActive ? 'YES' : 'NO'}
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Participants: {channel.participants}/{channel.maxParticipants}
        </Typography>
        <LinearProgress variant="determinate" value={usage} sx={{ height: 4, borderRadius: 2 }} />
      </Stack>
    </CompactCard>
  )
}

export default VoiceChannelCard


