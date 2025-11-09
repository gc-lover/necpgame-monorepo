import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { ShootingStatsCard } from '../components/ShootingStatsCard'

export const ShootingPage: React.FC = () => {
  const navigate = useNavigate()

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">Shooting</Typography>
      <Typography variant="caption" fontSize="0.7rem">Боевая система стрельбы</Typography>
      <Divider />
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Инфо</Typography>
      <Divider />
      <Typography variant="body2" fontSize="0.75rem" color="text.secondary">
        Система стрельбы с учетом TTK, частей тела, модификаторов, пробития укрытий
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">Combat Shooting</Typography>
      <Divider />
      <ShootingStatsCard stats={{ damage: 50, accuracy: 85, fire_rate: 600 }} />
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default ShootingPage

