import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff'
import GameLayout from '@/features/game/components/GameLayout'
import { StealthMeter } from '../components/StealthMeter'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useGetStealthStatus,
  useEnterStealth,
  usePerformTakedown,
  useHideBody,
  useCreateDistraction,
  useActivateOpticalCamo,
} from '@/api/generated/stealth/stealth/stealth'

export const StealthPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()

  const { data: stealthStatus } = useGetStealthStatus({ character_id: selectedCharacterId || '' }, { query: { enabled: !!selectedCharacterId, refetchInterval: 1000 } })

  const enterStealthMutation = useEnterStealth()
  const takedownMutation = usePerformTakedown()
  const hideBodyMutation = useHideBody()
  const distractionMutation = useCreateDistraction()
  const opticalCamoMutation = useActivateOpticalCamo()

  const handleEnterStealth = () => {
    if (!selectedCharacterId) return
    enterStealthMutation.mutate({ data: { character_id: selectedCharacterId, use_implants: false } })
  }

  const handleActivateCamo = () => {
    if (!selectedCharacterId) return
    opticalCamoMutation.mutate({ data: { character_id: selectedCharacterId, duration: 30 } })
  }

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="info">
        Stealth
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Deus Ex / Dishonored
      </Typography>
      <Divider />
      <Button startIcon={<VisibilityOffIcon />} onClick={handleEnterStealth} fullWidth variant="contained" size="small" sx={{ fontSize: '0.75rem' }} disabled={enterStealthMutation.isPending || stealthStatus?.stealth_level === 'hidden'}>
        Crouch (Скрытность)
      </Button>
      <Button onClick={handleActivateCamo} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }} disabled={opticalCamoMutation.isPending}>
        Optical Camo
      </Button>
      <Divider />
      {stealthStatus && <StealthMeter status={stealthStatus} />}
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Действия
      </Typography>
      <Divider />
      <Stack spacing={1}>
        <Button fullWidth variant="outlined" size="small" sx={{ fontSize: '0.7rem' }}>
          Takedown (Нейтрализация)
        </Button>
        <Button fullWidth variant="outlined" size="small" sx={{ fontSize: '0.7rem' }}>
          Hide Body (Спрятать тело)
        </Button>
        <Button fullWidth variant="outlined" size="small" sx={{ fontSize: '0.7rem' }}>
          Distraction (Отвлечение)
        </Button>
      </Stack>
      <Divider />
      <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
        Требует близкая дистанция + COOL проверка
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        Система скрытности
      </Typography>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        4 уровня: Hidden → Suspicious → Detected → Combat
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        Механики: видимость, шум, освещение, тени. Действия: crouch, takedown (lethal/non-lethal), hide body, distraction. Импланты: optical camo, sound dampener, enemy radar.
      </Typography>
      <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
        Вдохновение: Deus Ex, Dishonored, Hitman
      </Typography>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default StealthPage

