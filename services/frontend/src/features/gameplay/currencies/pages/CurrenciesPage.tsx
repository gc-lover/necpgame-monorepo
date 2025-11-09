import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'

export const CurrenciesPage: React.FC = () => {
  const navigate = useNavigate()

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">Валюты</Typography>
      <Typography variant="caption" fontSize="0.7rem">12 валют</Typography>
      <Divider />
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Типы</Typography>
      <Divider />
      <Typography variant="body2" fontSize="0.75rem" color="text.secondary">
        Eurodollar, региональные, фракционные, крипто, premium
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">Валюты</Typography>
      <Divider />
      <Alert severity="warning" sx={{ fontSize: '0.75rem' }}>
        API currencies.yaml недостаточно детализирован. Требуется дополнение схем и endpoints.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        После детализации: Eurodollar, региональные валюты, фракционные токены, криптовалюты, premium валюта, обмен курсов.
      </Typography>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default CurrenciesPage

