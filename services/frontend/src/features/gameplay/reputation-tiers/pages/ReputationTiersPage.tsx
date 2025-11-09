import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { ReputationTierCard } from '../components/ReputationTierCard'

export const ReputationTiersPage: React.FC = () => {
  const navigate = useNavigate()

  // Mock data - заменится на API
  const mockFactions = [
    { faction: 'NCPD', tier: 'friendly', points: 1200 },
    { faction: 'Arasaka', tier: 'neutral', points: 0 },
    { faction: 'Militech', tier: 'unfriendly', points: -500 },
  ]

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">Репутация</Typography>
      <Typography variant="caption" fontSize="0.7rem">8 тиров</Typography>
      <Divider />
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Тиры</Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['Hated', 'Hostile', 'Unfriendly', 'Neutral', 'Friendly', 'Trusted', 'Honored', 'Legendary'].map((t, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">{i+1}. {t}</Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">Репутация с фракциями</Typography>
      <Divider />
      <Box sx={{ overflowY: 'auto', flex: 1 }}>
        <Stack spacing={1.5}>
          {mockFactions.map((f, i) => <ReputationTierCard key={i} faction={f.faction} tier={f.tier} points={f.points} />)}
        </Stack>
      </Box>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default ReputationTiersPage

