import React from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, Grid, Chip } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import GameLayout from '@/features/game/components/GameLayout'
import { StaminaMeter } from '../components/StaminaMeter'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useCheckStamina,
  usePerformJump,
  usePerformClimb,
  usePerformSlide,
  usePerformGrapple,
  usePerformAerialAttack,
} from '@/api/generated/freerun/freerun/freerun'

export const FreerunPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()

  const { data: staminaData } = useCheckStamina({ character_id: selectedCharacterId || '' }, { query: { enabled: !!selectedCharacterId, refetchInterval: 1000 } })

  const jumpMutation = usePerformJump()
  const climbMutation = usePerformClimb()
  const slideMutation = usePerformSlide()
  const grappleMutation = usePerformGrapple()
  const aerialAttackMutation = usePerformAerialAttack()

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="success">
        Freerun
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Mirror's Edge / AC
      </Typography>
      <Divider />
      {staminaData && (
        <StaminaMeter
          current={staminaData.current_stamina || 0}
          max={staminaData.max_stamina || 100}
          regenRate={staminaData.stamina_regen_rate || 5}
          availableActions={staminaData.available_actions}
        />
      )}
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Элементы
      </Typography>
      <Stack spacing={0.5}>
        {['Прыжки между крышами', 'Лазание', 'Скольжение', 'Зацепление крюками', 'Гравиботы', 'Импланты для ног'].map((e, i) => (
          <Typography key={i} variant="caption" fontSize="0.7rem">
            • {e}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Типы прыжков
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['Normal', 'Double', 'Roof-to-Roof', 'Ledge Grab'].map((t, i) => (
          <Chip key={i} label={t} size="small" sx={{ fontSize: '0.7rem' }} />
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Grapple
      </Typography>
      <Stack spacing={0.5}>
        {['Hook', 'Gravibot', 'Mantis Blades'].map((t, i) => (
          <Chip key={i} label={t} size="small" variant="outlined" sx={{ fontSize: '0.7rem' }} />
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        Система паркура (Freerun)
      </Typography>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        Паркур: автоматический + ручной контроль. Ограничения: выносливость (STA).
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        <strong>Элементы:</strong> прыжки между крышами (normal, double, roof-to-roof, ledge grab), лазание (wall, ledge, pipe, ladder), скольжение (уклонение), зацепление (hook, gravibot, mantis blades).
      </Typography>
      <Typography variant="body2" fontSize="0.75rem">
        <strong>Интеграция с боем:</strong> атаки с воздуха (dive attack, aerial shoot, mantis strike), стрельба в движении, мобильные способности, комбо.
      </Typography>
      <Typography variant="body2" fontSize="0.75rem" fontWeight="bold">
        Вдохновение: Assassin's Creed, Mirror's Edge, Dying Light
      </Typography>
      <Grid container spacing={1} mt={1}>
        <Grid item xs={6}>
          <Button fullWidth variant="contained" size="small" sx={{ fontSize: '0.7rem' }} disabled={!staminaData?.available_actions?.jump}>
            Jump
          </Button>
        </Grid>
        <Grid item xs={6}>
          <Button fullWidth variant="contained" size="small" sx={{ fontSize: '0.7rem' }} disabled={!staminaData?.available_actions?.climb}>
            Climb
          </Button>
        </Grid>
        <Grid item xs={6}>
          <Button fullWidth variant="outlined" size="small" sx={{ fontSize: '0.7rem' }} disabled={!staminaData?.available_actions?.slide}>
            Slide
          </Button>
        </Grid>
        <Grid item xs={6}>
          <Button fullWidth variant="outlined" size="small" sx={{ fontSize: '0.7rem' }} disabled={!staminaData?.available_actions?.grapple}>
            Grapple
          </Button>
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default FreerunPage

