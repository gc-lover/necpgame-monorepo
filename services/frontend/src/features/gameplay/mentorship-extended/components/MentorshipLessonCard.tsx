import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import MenuBookIcon from '@mui/icons-material/MenuBook'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface MentorshipLessonSummary {
  lessonId: string
  title: string
  stage: string
  difficulty: 'EASY' | 'MEDIUM' | 'HARD' | 'LEGENDARY'
  requirements: string[]
  reward: string
  recommendedScore: number
}

const difficultyColor: Record<MentorshipLessonSummary['difficulty'], string> = {
  EASY: '#05ffa1',
  MEDIUM: '#00f7ff',
  HARD: '#fef86c',
  LEGENDARY: '#ff2a6d',
}

export interface MentorshipLessonCardProps {
  lesson: MentorshipLessonSummary
}

export const MentorshipLessonCard: React.FC<MentorshipLessonCardProps> = ({ lesson }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <MenuBookIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {lesson.title}
          </Typography>
        </Box>
        <Chip
          label={lesson.difficulty}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${difficultyColor[lesson.difficulty]}`,
            color: difficultyColor[lesson.difficulty],
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Stage: {lesson.stage} · Recommended score {lesson.recommendedScore}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Reward: {lesson.reward}
      </Typography>
      <Stack spacing={0.2}>
        {lesson.requirements.slice(0, 3).map((req) => (
          <Typography key={req} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {req}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default MentorshipLessonCard


