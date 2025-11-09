import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import HandshakeIcon from '@mui/icons-material/Handshake'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface MentorshipRelationshipSummary {
  relationshipId: string
  mentorName: string
  type: 'COMBAT' | 'TECH' | 'NETRUNNING' | 'SOCIAL' | 'ECONOMY' | 'MEDICAL'
  stage: 'REQUESTED' | 'ACTIVE' | 'ADVANCED' | 'GRADUATION' | 'ALUMNI'
  bondStrength: number
  trust: number
  lessonsCompleted: number
  totalLessons: number
  startedAt: string
}

const stageColor: Record<MentorshipRelationshipSummary['stage'], string> = {
  REQUESTED: '#00f7ff',
  ACTIVE: '#05ffa1',
  ADVANCED: '#fef86c',
  GRADUATION: '#d817ff',
  ALUMNI: '#37FFB4',
}

export interface MentorshipRelationshipCardProps {
  relationship: MentorshipRelationshipSummary
}

export const MentorshipRelationshipCard: React.FC<MentorshipRelationshipCardProps> = ({ relationship }) => (
  <CompactCard color="cyan" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <HandshakeIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {relationship.mentorName}
          </Typography>
        </Box>
        <Chip
          label={relationship.stage}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${stageColor[relationship.stage]}`,
            color: stageColor[relationship.stage],
          }}
        />
      </Box>

      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Type: {relationship.type} · Started: {new Date(relationship.startedAt).toLocaleDateString()}
      </Typography>

      <ProgressBar value={relationship.bondStrength} label="Bond" color="purple" compact />
      <ProgressBar value={relationship.trust} label="Trust" color="green" compact />
      <ProgressBar
        value={(relationship.lessonsCompleted / Math.max(relationship.totalLessons, 1)) * 100}
        label="Lessons"
        color="yellow"
        compact
        customText={`${relationship.lessonsCompleted}/${relationship.totalLessons}`}
      />
    </Stack>
  </CompactCard>
)

export default MentorshipRelationshipCard


