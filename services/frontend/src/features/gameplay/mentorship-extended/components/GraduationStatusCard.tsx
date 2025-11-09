import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import EmojiEventsIcon from '@mui/icons-material/EmojiEvents'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface GraduationRequirement {
  label: string
  completed: boolean
}

export interface GraduationStatusCardProps {
  stage: 'IN_PROGRESS' | 'READY' | 'GRADUATED'
  progress: number
  mentorApproval: number
  reputationImpact: string
  requirements: GraduationRequirement[]
}

const stageLabel: Record<GraduationStatusCardProps['stage'], string> = {
  IN_PROGRESS: 'В процессе',
  READY: 'Готов к выпуску',
  GRADUATED: 'Выпускник',
}

export const GraduationStatusCard: React.FC<GraduationStatusCardProps> = ({ stage, progress, mentorApproval, reputationImpact, requirements }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <EmojiEventsIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Graduation Status — {stageLabel[stage]}
        </Typography>
      </Box>
      <ProgressBar value={progress} label="Progress" color="green" compact />
      <ProgressBar value={mentorApproval} label="Mentor Approval" color="cyan" compact />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Reputation impact: {reputationImpact}
      </Typography>
      <Stack spacing={0.2}>
        {requirements.map((req) => (
          <Typography
            key={req.label}
            variant="caption"
            fontSize={cyberpunkTokens.fonts.xs}
            color={req.completed ? 'success.main' : 'text.secondary'}
          >
            {req.completed ? '✓' : '•'} {req.label}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default GraduationStatusCard


