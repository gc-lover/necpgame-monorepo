/**
 * Компонент приветственного экрана
 */
import { Box, Typography, Stack, Paper, Divider } from '@mui/material'
import { GameStartButton } from './GameStartButton'
import type { WelcomeScreenResponse } from '@/api/generated/game/models'

interface WelcomeScreenProps {
  data: WelcomeScreenResponse
  onStartGame: (skipTutorial: boolean) => void
  loading?: boolean
}

export function WelcomeScreen({ data, onStartGame, loading = false }: WelcomeScreenProps) {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '80vh',
        p: 3,
      }}
    >
      <Paper
        elevation={4}
        sx={{
          maxWidth: 600,
          width: '100%',
          p: 4,
          textAlign: 'center',
          backgroundColor: 'background.paper',
          border: '2px solid',
          borderColor: 'primary.main',
        }}
      >
        <Typography
          variant="h3"
          sx={{
            mb: 2,
            color: 'primary.main',
            fontWeight: 'bold',
            textTransform: 'uppercase',
          }}
        >
          {data.message}
        </Typography>

        <Typography variant="h6" sx={{ mb: 3, color: 'text.secondary', fontStyle: 'italic' }}>
          {data.subtitle}
        </Typography>

        <Divider sx={{ my: 3 }} />

        {/* Информация о персонаже */}
        <Box sx={{ mb: 3, textAlign: 'left' }}>
          <Typography variant="subtitle1" sx={{ fontWeight: 'bold', mb: 1 }}>
            Ваш персонаж:
          </Typography>
          <Stack spacing={1}>
            <Typography variant="body1">
              <strong>Имя:</strong> {data.character.name}
            </Typography>
            <Typography variant="body1">
              <strong>Класс:</strong> {data.character.class}
            </Typography>
            <Typography variant="body1">
              <strong>Уровень:</strong> {data.character.level}
            </Typography>
          </Stack>
        </Box>

        {/* Стартовая локация */}
        <Box sx={{ mb: 3, textAlign: 'left' }}>
          <Typography variant="subtitle1" sx={{ fontWeight: 'bold', mb: 1 }}>
            Стартовая локация:
          </Typography>
          <Typography variant="body1">{data.startingLocation}</Typography>
        </Box>

        <Divider sx={{ my: 3 }} />

        {/* Кнопки действий */}
        <Stack spacing={2} sx={{ mt: 3 }}>
          {data.buttons.map((button) => (
            <GameStartButton
              key={button.id}
              onClick={() => onStartGame(button.id === 'skip-tutorial')}
              loading={loading}
              label={button.label || 'Начать'}
              skipTutorial={button.id === 'skip-tutorial'}
            />
          ))}
        </Stack>
      </Paper>
    </Box>
  )
}

