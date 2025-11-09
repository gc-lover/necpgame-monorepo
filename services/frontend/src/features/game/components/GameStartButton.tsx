/**
 * Компонент кнопки начала игры
 */
import { Button, CircularProgress } from '@mui/material'
import PlayArrowIcon from '@mui/icons-material/PlayArrow'

interface GameStartButtonProps {
  onClick: () => void
  loading?: boolean
  disabled?: boolean
  label?: string
  skipTutorial?: boolean
}

export function GameStartButton({
  onClick,
  loading = false,
  disabled = false,
  label = 'Начать игру',
  skipTutorial = false,
}: GameStartButtonProps) {
  return (
    <Button
      variant={skipTutorial ? 'outlined' : 'contained'}
      size="large"
      startIcon={loading ? <CircularProgress size={20} /> : <PlayArrowIcon />}
      onClick={onClick}
      disabled={disabled || loading}
      sx={{
        minWidth: 200,
        textTransform: 'none',
        fontSize: '1.1rem',
        py: 1.5,
      }}
    >
      {loading ? 'Загрузка...' : label}
    </Button>
  )
}

