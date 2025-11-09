/**
 * Компонент отображения состояния персонажа
 * 
 * Показывает:
 * - Здоровье (Health)
 * - Энергию (Energy)  
 * - Человечность (Humanity)
 * - Деньги (Money/Eddies)
 * - Уровень (Level)
 * - Опыт (Experience)
 */
import { Box, Typography, LinearProgress, Stack, Paper } from '@mui/material'
import FavoriteIcon from '@mui/icons-material/Favorite'
import BoltIcon from '@mui/icons-material/Bolt'
import PsychologyIcon from '@mui/icons-material/Psychology'
import AttachMoneyIcon from '@mui/icons-material/AttachMoney'
import type { GameCharacterState } from '@/api/generated/game/models'

interface CharacterStateProps {
  state: GameCharacterState
}

export function CharacterState({ state }: CharacterStateProps) {
  return (
    <Paper
      elevation={2}
      sx={{
        p: 1.5,
        backgroundColor: 'background.paper',
        border: '1px solid',
        borderColor: 'divider',
      }}
    >
      <Typography 
        variant="subtitle2" 
        gutterBottom 
        sx={{ 
          color: 'primary.main',
          fontSize: '0.75rem',
          textTransform: 'uppercase',
          letterSpacing: '0.05em',
          mb: 1,
        }}
      >
        Статус
      </Typography>

      <Stack spacing={1.5}>
        {/* Здоровье */}
        <Box>
          <Stack direction="row" spacing={0.5} alignItems="center" sx={{ mb: 0.3 }}>
            <FavoriteIcon sx={{ fontSize: '0.875rem' }} color="error" />
            <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.7rem' }}>
              HP: {state.health}/100
            </Typography>
          </Stack>
          <LinearProgress
            variant="determinate"
            value={state.health || 0}
            sx={{ height: 4, borderRadius: 1 }}
            color={state.health > 50 ? 'success' : 'error'}
          />
        </Box>

        {/* Энергия */}
        <Box>
          <Stack direction="row" spacing={0.5} alignItems="center" sx={{ mb: 0.3 }}>
            <BoltIcon sx={{ fontSize: '0.875rem' }} color="warning" />
            <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.7rem' }}>
              EN: {state.energy}/100
            </Typography>
          </Stack>
          <LinearProgress
            variant="determinate"
            value={state.energy || 0}
            sx={{ height: 4, borderRadius: 1 }}
            color={state.energy > 50 ? 'primary' : 'warning'}
          />
        </Box>

        {/* Человечность */}
        <Box>
          <Stack direction="row" spacing={0.5} alignItems="center" sx={{ mb: 0.3 }}>
            <PsychologyIcon sx={{ fontSize: '0.875rem' }} color="info" />
            <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.7rem' }}>
              HM: {state.humanity}/100
            </Typography>
          </Stack>
          <LinearProgress
            variant="determinate"
            value={state.humanity || 0}
            sx={{ height: 4, borderRadius: 1 }}
            color={state.humanity > 50 ? 'info' : 'warning'}
          />
        </Box>

        {/* Деньги и уровень */}
        <Stack direction="row" justifyContent="space-between" alignItems="center">
          <Stack direction="row" spacing={0.3} alignItems="center">
            <AttachMoneyIcon sx={{ fontSize: '0.875rem' }} color="success" />
            <Typography variant="caption" fontWeight="bold" sx={{ fontSize: '0.7rem' }}>
              {state.money}
            </Typography>
          </Stack>
          <Typography variant="caption" sx={{ color: 'text.secondary', fontSize: '0.7rem' }}>
            Ур: <strong>{state.level}</strong>
            {state.experience !== undefined && ` | XP: ${state.experience}`}
          </Typography>
        </Stack>
      </Stack>
    </Paper>
  )
}

