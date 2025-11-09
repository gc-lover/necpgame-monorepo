import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'

export const FactionWarsPage: React.FC = () => {
  const navigate = useNavigate()

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="error">Faction Wars</Typography>
      <Typography variant="caption" fontSize="0.7rem">EVE Online / WoW</Typography>
      <Divider />
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Механики</Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['Территориальный контроль', 'Массовые сражения', 'Осады', 'Альянсы'].map((m, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">• {m}</Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">Войны фракций</Typography>
      <Divider />
      <Alert severity="warning" sx={{ fontSize: '0.75rem' }}>
        API faction-wars.yaml недостаточно детализирован. Требуется дополнение схем войн, территорий, сражений.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        После детализации: корпорации, банды, государства, территориальный контроль, массовые сражения, осады, альянсы, награды (территория, ресурсы, репутация).
      </Typography>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default FactionWarsPage

