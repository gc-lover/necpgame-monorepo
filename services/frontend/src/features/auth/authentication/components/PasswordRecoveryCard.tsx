import { useState } from 'react'
import { Alert, Box, Stack, Tab, Tabs, TextField, Typography } from '@mui/material'
import {
  useForgotPassword,
  useResetPassword,
} from '@/api/generated/auth/authentication/password/password'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

type PasswordMode = 'forgot' | 'reset'

export function PasswordRecoveryCard() {
  const [mode, setMode] = useState<PasswordMode>('forgot')
  const [forgotEmail, setForgotEmail] = useState('')
  const [resetInput, setResetInput] = useState({ token: '', password: '', confirm: '' })
  const [feedback, setFeedback] = useState<{ type: 'success' | 'error'; message: string } | null>(null)

  const forgotPasswordMutation = useForgotPassword()
  const resetPasswordMutation = useResetPassword()

  const handleModeChange = (_: React.SyntheticEvent, next: string) => {
    if (next === 'forgot' || next === 'reset') {
      setMode(next)
      setFeedback(null)
    }
  }

  const handleForgotSubmit = (event: React.FormEvent) => {
    event.preventDefault()
    setFeedback(null)

    if (!forgotEmail.trim()) {
      setFeedback({ type: 'error', message: 'Укажите email, привязанный к аккаунту' })
      return
    }

    forgotPasswordMutation.mutate(
      { data: { email: forgotEmail.trim() } },
      {
        onSuccess: (data) => {
          setFeedback({
            type: 'success',
            message: data.message ?? 'Ссылка для сброса пароля отправлена на указанную почту.',
          })
        },
        onError: () => {
          setFeedback({
            type: 'error',
            message: 'Не удалось отправить письмо. Попробуйте ещё раз или проверьте email.',
          })
        },
      },
    )
  }

  const handleResetSubmit = (event: React.FormEvent) => {
    event.preventDefault()
    setFeedback(null)

    if (!resetInput.token.trim()) {
      setFeedback({ type: 'error', message: 'Укажите токен из письма' })
      return
    }
    if (resetInput.password.length < 8) {
      setFeedback({ type: 'error', message: 'Пароль должен содержать не менее 8 символов' })
      return
    }
    if (resetInput.password !== resetInput.confirm) {
      setFeedback({ type: 'error', message: 'Пароли не совпадают' })
      return
    }

    resetPasswordMutation.mutate(
      {
        data: {
          token: resetInput.token.trim(),
          new_password: resetInput.password,
          confirm_password: resetInput.confirm,
        },
      },
      {
        onSuccess: (data) => {
          setFeedback({
            type: 'success',
            message: data.message ?? 'Пароль успешно обновлён. Теперь войдите с новым паролем.',
          })
          setMode('forgot')
          setResetInput({ token: '', password: '', confirm: '' })
        },
        onError: () => {
          setFeedback({
            type: 'error',
            message: 'Не удалось обновить пароль. Проверьте токен и попробуйте снова.',
          })
        },
      },
    )
  }

  return (
    <CompactCard color="magenta" glowIntensity="strong" sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      <Stack spacing={2} sx={{ flex: 1 }}>
        <Box>
          <Typography variant="h6" fontSize="0.9rem" textTransform="uppercase">
            Восстановление доступа
          </Typography>
          <Typography variant="body2" color="text.secondary" fontSize="0.75rem">
            Запросите письмо для сброса пароля или примените новый пароль с токеном из письма.
          </Typography>
        </Box>

        <Tabs
          value={mode}
          onChange={handleModeChange}
          variant="fullWidth"
          sx={{
            minHeight: 40,
            '& .MuiTab-root': {
              minHeight: 40,
              fontSize: '0.72rem',
              fontWeight: 600,
            },
          }}
        >
          <Tab label="Запросить письмо" value="forgot" disableRipple />
          <Tab label="Сбросить пароль" value="reset" disableRipple />
        </Tabs>

        {feedback && (
          <Alert severity={feedback.type} variant="outlined" sx={{ fontSize: '0.75rem' }}>
            {feedback.message}
          </Alert>
        )}

        {mode === 'forgot' ? (
          <Box component="form" onSubmit={handleForgotSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 1.5 }}>
            <TextField
              label="Email"
              size="small"
              value={forgotEmail}
              onChange={(event) => setForgotEmail(event.target.value)}
              helperText="Введите email, указанный при регистрации"
            />
            <CyberpunkButton type="submit" variant="primary" size="medium" disabled={forgotPasswordMutation.isPending}>
              {forgotPasswordMutation.isPending ? 'Отправка...' : 'Отправить письмо'}
            </CyberpunkButton>
          </Box>
        ) : (
          <Box component="form" onSubmit={handleResetSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 1.5 }}>
            <TextField
              label="Токен из письма"
              size="small"
              value={resetInput.token}
              onChange={(event) => setResetInput((prev) => ({ ...prev, token: event.target.value }))}
            />
            <TextField
              label="Новый пароль"
              size="small"
              type="password"
              value={resetInput.password}
              onChange={(event) => setResetInput((prev) => ({ ...prev, password: event.target.value }))}
            />
            <TextField
              label="Подтверждение пароль"
              size="small"
              type="password"
              value={resetInput.confirm}
              onChange={(event) => setResetInput((prev) => ({ ...prev, confirm: event.target.value }))}
            />
            <CyberpunkButton type="submit" variant="primary" size="medium" disabled={resetPasswordMutation.isPending}>
              {resetPasswordMutation.isPending ? 'Применение...' : 'Сбросить пароль'}
            </CyberpunkButton>
          </Box>
        )}
      </Stack>
    </CompactCard>
  )
}



