import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { CombatParticipantCard } from '../components/CombatParticipantCard'

export const CombatSystemPage: React.FC = () => {
  const navigate = useNavigate()

  // Mock data для демонстрации
  const mockParticipants = [
    { id: '1', name: 'Игрок', type: 'player', health: 80, maxHealth: 100, armor: 20, isAlive: true },
    { id: '2', name: 'Враг', type: 'enemy', health: 45, maxHealth: 60, armor: 10, isAlive: true },
  ]

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">Combat System</Typography>
      <Typography variant="caption" fontSize="0.7rem">Текстовая боевая система</Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Действия</Typography>
      <Button variant="contained" size="small" sx={{ fontSize: '0.7rem' }}>Атака</Button>
      <Button variant="outlined" size="small" sx={{ fontSize: '0.7rem' }}>Защита</Button>
      <Button variant="outlined" size="small" sx={{ fontSize: '0.7rem' }}>Предмет</Button>
      <Button variant="outlined" color="warning" size="small" sx={{ fontSize: '0.7rem' }}>Сбежать</Button>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Лог боя</Typography>
      <Divider />
      <Box sx={{ overflowY: 'auto', maxHeight: '400px' }}>
        <Stack spacing={0.5}>
          <Typography variant="caption" fontSize="0.7rem">→ Раунд 1</Typography>
          <Typography variant="caption" fontSize="0.7rem">• Игрок атаковал (урон: 20)</Typography>
          <Typography variant="caption" fontSize="0.7rem">• Враг защищается</Typography>
        </Stack>
      </Box>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">Бой</Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Участники</Typography>
      <Stack spacing={1.5}>
        {mockParticipants.map((p) => <CombatParticipantCard key={p.id} participant={p} />)}
      </Stack>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default CombatSystemPage

