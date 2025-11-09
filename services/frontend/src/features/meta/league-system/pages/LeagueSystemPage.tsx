import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'

export const LeagueSystemPage: React.FC = () => {
  const navigate = useNavigate()

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">Лиги</Typography>
      <Typography variant="caption" fontSize="0.7rem">Сезонная система</Typography>
      <Divider />
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Фазы</Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['Start', 'Rise', 'Crisis', 'Endgame', 'Finale'].map((p, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">{i+1}. {p}</Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">League System</Typography>
      <Divider />
      <Alert severity="warning" sx={{ fontSize: '0.75rem' }}>
        API league-system.yaml недостаточно детализирован. Требуется дополнение схем сезонов, рейтингов, наград.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        Цикл: 73 года игровых (2020-2093), 3-6 месяцев реальных. 5 фаз, глобальный сброс, мета-прогресс.
      </Typography>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default LeagueSystemPage

