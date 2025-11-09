import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'

export const WorldStatePage: React.FC = () => {
  const navigate = useNavigate()

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">Состояние мира</Typography>
      <Typography variant="caption" fontSize="0.7rem">Player Impact</Typography>
      <Divider />
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Уровни влияния</Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['Individual', 'Group', 'Faction', 'Regional', 'Global'].map((l, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">{i+1}. {l}</Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">World State</Typography>
      <Divider />
      <Alert severity="warning" sx={{ fontSize: '0.75rem' }}>
        API world-state.yaml недостаточно детализирован. Требуется дополнение схем состояний и категорий влияния.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        5 уровней влияния: Individual → Group → Faction → Regional → Global. Категории: Territory, Faction Power, Economy, Technology, Social, Quest, Environmental.
      </Typography>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default WorldStatePage

