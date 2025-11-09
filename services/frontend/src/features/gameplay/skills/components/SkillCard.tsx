import React from 'react'
import { Card, CardContent, Typography, Stack, LinearProgress, Box } from '@mui/material'

export const SkillCard: React.FC<{ skill: any }> = ({ skill }) => (
  <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
    <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">{skill.name || 'Навык'}</Typography>
          <Typography variant="caption" fontSize="0.7rem">Ур. {skill.level || 1}</Typography>
        </Box>
        <Typography variant="caption" fontSize="0.7rem">Прогресс: {skill.progress || 0}%</Typography>
        <LinearProgress variant="determinate" value={skill.progress || 0} sx={{ height: 4 }} />
      </Stack>
    </CardContent>
  </Card>
)

