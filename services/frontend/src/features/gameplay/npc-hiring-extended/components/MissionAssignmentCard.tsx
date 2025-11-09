import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import TargetIcon from '@mui/icons-material/MyLocation'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface MissionMember {
  npcName: string
  role: string
  effectiveness: number
}

export interface MissionAssignmentSummary {
  missionId: string
  title: string
  location: string
  dangerLevel: 'LOW' | 'MEDIUM' | 'HIGH' | 'EXTREME'
  payout: string
  successChance: number
  members: MissionMember[]
}

const dangerColor: Record<MissionAssignmentSummary['dangerLevel'], string> = {
  LOW: '#05ffa1',
  MEDIUM: '#00f7ff',
  HIGH: '#fef86c',
  EXTREME: '#ff2a6d',
}

export interface MissionAssignmentCardProps {
  mission: MissionAssignmentSummary
}

export const MissionAssignmentCard: React.FC<MissionAssignmentCardProps> = ({ mission }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <TargetIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {mission.title}
          </Typography>
        </Box>
        <Chip
          label={mission.dangerLevel}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${dangerColor[mission.dangerLevel]}`,
            color: dangerColor[mission.dangerLevel],
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Location: {mission.location} · Payout: {mission.payout}
      </Typography>
      <ProgressBar value={mission.successChance} label="Success chance" color="green" compact />
      <Stack spacing={0.2}>
        {mission.members.map((member) => (
          <Typography key={member.npcName} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {member.npcName} — {member.role} ({member.effectiveness}% effectiveness)
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default MissionAssignmentCard


