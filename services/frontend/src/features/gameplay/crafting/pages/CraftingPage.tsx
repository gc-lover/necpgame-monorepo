import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'

export const CraftingPage: React.FC = () => {
  const navigate = useNavigate()

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>Назад</Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">Крафт</Typography>
      <Typography variant="caption" fontSize="0.7rem">Система крафта</Typography>
      <Divider />
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">Инфо</Typography>
      <Divider />
      <Typography variant="body2" fontSize="0.75rem" color="text.secondary">
        API требует детализации в .BRAIN и API-SWAGGER
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">Система крафта</Typography>
      <Divider />
      <Alert severity="warning" sx={{ fontSize: '0.75rem' }}>
        API crafting.yaml недостаточно детализирован. Требуется дополнение схем, endpoints и моделей данных.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        После детализации API будут добавлены: рецепты, компоненты, процесс крафта, качество предметов.
      </Typography>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default CraftingPage

