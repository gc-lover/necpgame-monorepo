import { useState } from 'react'
import {
  Alert,
  Box,
  Checkbox,
  FormControlLabel,
  Stack,
  Tab,
  Tabs,
  TextField,
  Typography,
} from '@mui/material'
import { useLogin, useRegister } from '@/api/generated/auth/auth/auth'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

type AuthMode = 'login' | 'register'

interface AuthenticationCardProps {
  onAuthenticated?: () => void
}

export function AuthenticationCard({ onAuthenticated }: AuthenticationCardProps) {
  const [mode, setMode] = useState<AuthMode>('login')
  const [loginInput, setLoginInput] = useState({ login: '', password: '' })
  const [registerInput, setRegisterInput] = useState({
    email: '',
    username: '',
    password: '',
    confirm: '',
    termsAccepted: false,
  })
  const [feedback, setFeedback] = useState<{ type: 'success' | 'error'; message: string } | null>(null)

  const loginMutation = useLogin()
  const registerMutation = useRegister()

  const handleModeChange = (_: React.SyntheticEvent, value: string) => {
    if (value === 'login' || value === 'register') {
      setMode(value)
      setFeedback(null)
    }
  }

  const handleLoginSubmit = (event: React.FormEvent) => {
    event.preventDefault()
    setFeedback(null)
    if (!loginInput.login.trim() || !loginInput.password.trim()) {
      setFeedback({ type: 'error', message: 'Заполните логин и пароль' })
      return
    }
    loginMutation.mutate(
      { data: { login: loginInput.login.trim(), password: loginInput.password } },
      {
        onSuccess: (data) => {
          localStorage.setItem('auth_token', data.token)
          setFeedback({ type: 'success', message: 'Вход выполнен. Токен сохранён.' })
          onAuthenticated?.()
        },
        onError: (error) => {
          const apiMessage = error.response?.data && 'message' in error.response.data
            ? String((error.response.data as Record<string, unknown>).message)
            : null
          setFeedback({
            type: 'error',
            message: apiMessage || 'Не удалось выполнить вход. Проверьте данные.',
          })
        },
      }
    )
  }

  const handleRegisterSubmit = (event: React.FormEvent) => {
    event.preventDefault()
    setFeedback(null)
    if (!registerInput.email.trim() || !registerInput.username.trim()) {
      setFeedback({ type: 'error', message: 'Заполните email и имя пользователя' })
      return
    }
    if (registerInput.password.length < 8) {
      setFeedback({ type: 'error', message: 'Пароль должен содержать не менее 8 символов' })
      return
    }
    if (registerInput.password !== registerInput.confirm) {
      setFeedback({ type: 'error', message: 'Пароли не совпадают' })
      return
    }
    if (!registerInput.termsAccepted) {
      setFeedback({ type: 'error', message: 'Необходимо принять условия использования' })
      return
    }
    registerMutation.mutate(
      {
        data: {
          email: registerInput.email.trim(),
          username: registerInput.username.trim(),
          password: registerInput.password,
          password_confirm: registerInput.confirm,
          terms_accepted: true,
        },
      },
      {
        onSuccess: (data) => {
          setFeedback({
            type: 'success',
            message: `Аккаунт создан. ID: ${data.account_id}`,
          })
          setMode('login')
          setRegisterInput({
            email: '',
            username: '',
            password: '',
            confirm: '',
            termsAccepted: false,
          })
        },
        onError: (error) => {
          const apiMessage = error.response?.data && 'message' in error.response.data
            ? String((error.response.data as Record<string, unknown>).message)
            : null
          setFeedback({
            type: 'error',
            message: apiMessage || 'Регистрация не удалась. Попробуйте снова.',
          })
        },
      }
    )
  }

  return (
    <CompactCard
      color="cyan"
      glowIntensity="strong"
      sx={{
        height: '100%',
        display: 'flex',
        flexDirection: 'column',
      }}
    >
      <Stack spacing={2} sx={{ flex: 1, minHeight: 0 }}>
        <Box>
          <Typography variant="h6" fontSize="0.9rem" textTransform="uppercase">
            Аутентификация
          </Typography>
          <Typography variant="body2" color="text.secondary" fontSize="0.75rem">
            Войдите или создайте аккаунт, чтобы управлять персонажами
          </Typography>
        </Box>
        <Tabs
          value={mode}
          onChange={handleModeChange}
          aria-label="auth mode"
          variant="fullWidth"
          sx={{
            minHeight: 40,
            '& .MuiTab-root': {
              minHeight: 40,
              fontSize: '0.7rem',
              fontWeight: 600,
            },
          }}
        >
          <Tab label="Вход" value="login" disableRipple />
          <Tab label="Регистрация" value="register" disableRipple />
        </Tabs>
        {feedback && (
          <Alert severity={feedback.type} variant="outlined">
            {feedback.message}
          </Alert>
        )}
        {mode === 'login' ? (
          <Box component="form" onSubmit={handleLoginSubmit} sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 1.5 }}>
            <TextField
              label="Email или логин"
              value={loginInput.login}
              onChange={(event) => setLoginInput((prev) => ({ ...prev, login: event.target.value }))}
              size="small"
              fullWidth
            />
            <TextField
              label="Пароль"
              type="password"
              value={loginInput.password}
              onChange={(event) => setLoginInput((prev) => ({ ...prev, password: event.target.value }))}
              size="small"
              fullWidth
            />
            <CyberpunkButton
              type="submit"
              variant="primary"
              size="medium"
              disabled={loginMutation.isPending}
            >
              {loginMutation.isPending ? 'Вход...' : 'Войти'}
            </CyberpunkButton>
          </Box>
        ) : (
          <Box component="form" onSubmit={handleRegisterSubmit} sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 1.5 }}>
            <TextField
              label="Email"
              type="email"
              value={registerInput.email}
              onChange={(event) => setRegisterInput((prev) => ({ ...prev, email: event.target.value }))}
              size="small"
              fullWidth
            />
            <TextField
              label="Имя пользователя"
              value={registerInput.username}
              onChange={(event) => setRegisterInput((prev) => ({ ...prev, username: event.target.value }))}
              size="small"
              fullWidth
            />
            <Stack direction="row" spacing={1}>
              <TextField
                label="Пароль"
                type="password"
                value={registerInput.password}
                onChange={(event) => setRegisterInput((prev) => ({ ...prev, password: event.target.value }))}
                size="small"
                fullWidth
              />
              <TextField
                label="Подтверждение"
                type="password"
                value={registerInput.confirm}
                onChange={(event) => setRegisterInput((prev) => ({ ...prev, confirm: event.target.value }))}
                size="small"
                fullWidth
              />
            </Stack>
            <FormControlLabel
              control={
                <Checkbox
                  checked={registerInput.termsAccepted}
                  onChange={(event) =>
                    setRegisterInput((prev) => ({ ...prev, termsAccepted: event.target.checked }))
                  }
                  size="small"
                />
              }
              label={
                <Typography variant="caption">
                  Принимаю условия использования и политику конфиденциальности
                </Typography>
              }
            />
            <CyberpunkButton
              type="submit"
              variant="secondary"
              size="medium"
              disabled={registerMutation.isPending}
            >
              {registerMutation.isPending ? 'Создание...' : 'Зарегистрироваться'}
            </CyberpunkButton>
          </Box>
        )}
      </Stack>
    </CompactCard>
  )
}






