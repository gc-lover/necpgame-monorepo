import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box } from '@mui/material'
import SchoolIcon from '@mui/icons-material/School'
import StarIcon from '@mui/icons-material/Star'

interface MentorProps {
  mentor: {
    mentor_id?: string
    name?: string
    level?: number
    specialization?: string
    mentorship_level?: number
    active_mentees?: number
    max_mentees?: number
    rating?: number
  }
}

export const MentorCard: React.FC<MentorProps> = ({ mentor }) => {
  return (
    <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box display="flex" alignItems="center" gap={0.5}>
              <SchoolIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
              <Typography variant="caption" fontSize="0.75rem" fontWeight="bold">
                {mentor.name}
              </Typography>
            </Box>
            <Chip label={`LVL ${mentor.level || 1}`} size="small" color="primary" sx={{ height: 16, fontSize: '0.6rem' }} />
          </Box>
          {mentor.specialization && (
            <Chip label={mentor.specialization} size="small" color="info" sx={{ height: 14, fontSize: '0.55rem' }} />
          )}
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
              Учеников: {mentor.active_mentees}/{mentor.max_mentees}
            </Typography>
            <Box display="flex" alignItems="center" gap={0.3}>
              <StarIcon sx={{ fontSize: '0.8rem', color: 'warning.main' }} />
              <Typography variant="caption" fontSize="0.65rem" color="warning.main">
                {mentor.rating?.toFixed(1)}
              </Typography>
            </Box>
          </Box>
        </Stack>
      </CardContent>
    </Card>
  )
}

