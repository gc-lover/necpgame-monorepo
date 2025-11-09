import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import SchoolIcon from '@mui/icons-material/School'
import GameLayout from '@/features/game/components/GameLayout'
import { MentorCard } from '../components/MentorCard'
import { useGameState } from '@/features/game/hooks/useGameState'
import { useGetAvailableMentors } from '@/api/generated/mentorship/mentorship/mentorship'

export const MentorshipPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()

  const { data: mentorsData } = useGetAvailableMentors({})

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="info">
        Mentorship
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Наставничество
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Роли
      </Typography>
      <Stack spacing={0.3}>
        {['Mentor (опытный)', 'Mentee (новичок)'].map((r, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {r}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Бонусы
      </Typography>
      <Stack spacing={0.3}>
        {['XP boost', 'Skill bonuses', 'Rewards'].map((b, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {b}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Прогрессия
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['Уровни наставничества', 'Достижения', 'Репутация'].map((p, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {p}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Обязательства
      </Typography>
      <Stack spacing={0.3}>
        {['Времени', 'Помощи', 'Обучения'].map((o, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {o}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <SchoolIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
          Система наставничества
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        Mentor ↔ Mentee! XP boost для новичков, skill bonuses, rewards для обоих. Уровни наставничества, достижения, репутация.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        Доступных менторов: {mentorsData?.mentors?.length || 0}
      </Typography>
      <Grid container spacing={1}>
        {mentorsData?.mentors?.map((mentor, index) => (
          <Grid item xs={12} sm={6} md={4} key={mentor.mentor_id || index}>
            <MentorCard mentor={mentor} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default MentorshipPage

