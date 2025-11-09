import { Alert, Stack, Typography } from '@mui/material'
import type { WelcomeScreenResponse } from '@/api/generated/game/models'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

interface StartActionsCardProps {
  buttons: WelcomeScreenResponse['buttons']
  onAction: (skipTutorial: boolean) => void
  isLoading: boolean
  error?: string | null
}

export function StartActionsCard({ buttons, onAction, isLoading, error }: StartActionsCardProps) {
  return (
    <CompactCard
      color="green"
      glowIntensity="strong"
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
        gap: 2,
      }}
    >
      <Typography variant="h6" sx={{ fontSize: '0.95rem', textTransform: 'uppercase' }}>
        Готовы начать?
      </Typography>

      <Typography variant="body2" color="text.secondary">
        Выберите, хотите ли пройти вводный туториал, или сразу погрузиться в Night City.
      </Typography>

      <Stack spacing={1.5} sx={{ flex: 1, justifyContent: 'center' }}>
        {buttons.map((button) => {
          const skip = button.id === 'skip-tutorial'
          return (
            <CyberpunkButton
              key={button.id}
              onClick={() => onAction(skip)}
              disabled={isLoading}
              fullWidth
              variant={skip ? 'outlined' : 'primary'}
              size="large"
            >
              {button.label ?? (skip ? 'Пропустить туториал' : 'Начать игру')}
            </CyberpunkButton>
          )
        })}
      </Stack>

      {error && (
        <Alert severity="error" variant="outlined">
          {error}
        </Alert>
      )}

      <Typography variant="caption" color="text.secondary">
        После старта можно вернуться в пункт отправления, используя пункт «Продолжить игру» в главном меню.
      </Typography>
    </CompactCard>
  )
}






