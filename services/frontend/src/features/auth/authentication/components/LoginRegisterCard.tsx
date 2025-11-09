import { useState } from 'react'
import {
  Alert,
  Box,
  Stack,
  Tab,
  Tabs,
  TextField,
  Typography,
} from '@mui/material'
import { useLogin, useRegisterAccount } from '@/api/generated/auth/authentication/authentication/authentication'
import type { LoginResponse } from '@/api/generated/auth/authentication/models'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

type AuthMode = 'login' | 'register'

type LoginRegisterCardProps = {
  onAuthSuccess?: (payload: { accessToken?: string; refreshToken?: string; roles?: string[] }) => void
}

export function LoginRegisterCard({ onAuthSuccess }: LoginRegisterCardProps) {
  const [mode, setMode] = useState<AuthMode>('login')
  const [loginInput, setLoginInput] = useState({ login: '', password: '' })
  const [registerInput, setRegisterInput] = useState({
    email: '',
    password: '',
    confirm: '',
    username: '',
    agree: false,
  })
  const [feedback, setFeedback] = useState<{ type: 'success' | 'error'; message: string } | null>(null)

  const loginMutation = useLogin()
  const registerMutation = useRegisterAccount()

  const persistTokens = (data: LoginResponse) => {
    if (data.access_token) {
      localStorage.setItem('auth_token', data.access_token)
    }
    if (data.refresh_token) {
      localStorage.setItem('refresh_token', data.refresh_token)
    }
    onAuthSuccess?.({
      accessToken: data.access_token,
      refreshToken: data.refresh_token,
      roles: data.roles ?? [],
    })
  }

  const handleModeChange = (_: React.SyntheticEvent, next: string) => {
    if (next === 'login' || next === 'register') {
      setMode(next)
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
        onSuccess: (response) => {
          persistTokens(response)
          setFeedback({ type: 'success', message: 'Вход выполнен успешно. Токены сохранены.' })
        },
        onError: (error) => {
          const apiMessage =
            error.response?.data && typeof error.response.data === 'object'
              ? (error.response.data as Record<string, unknown>).message
              : undefined
          setFeedback({
            type: 'error',
            message: apiMessage ? String(apiMessage) : 'Не удалось выполнить вход. Проверьте данные.',
          })
        },
      },
    )
  }

  const handleRegisterSubmit = (event: React.FormEvent) => {
    event.preventDefault()
    setFeedback(null)

    if (!registerInput.email.trim() || !registerInput.username.trim()) {
      setFeedback({ type: 'error', message: 'Укажите email и имя пользователя' })
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
    if (!registerInput.agree) {
      setFeedback({ type: 'error', message: 'Необходимо согласиться с условиями использования' })
      return
    }

    registerMutation.mutate(
      {
        data: {
          email: registerInput.email.trim(),
          password: registerInput.password,
          password_confirm: registerInput.confirm,
          username: registerInput.username.trim(),
          agree_to_terms: registerInput.agree,
        },
      },
      {
        onSuccess: (data) => {
          setFeedback({
            type: 'success',
            message: `Аккаунт создан. ID: ${data.account_id}. Теперь войдите.`,
          })
          setMode('login')
          setRegisterInput({
            email: '',
            password: '',
            confirm: '',
            username: '',
            agree: false,
          })
        },
        onError: (error) => {
          const apiMessage =
            error.response?.data && typeof error.response.data === 'object'
              ? (error.response.data as Record<string, unknown>).message
              : undefined
          setFeedback({
            type: 'error',
            message: apiMessage ? String(apiMessage) : 'Не удалось создать аккаунт. Попробуйте снова.',
          })
        },
      },
    )
  }

  return (
    <CompactCard
      color="cyan"
      glowIntensity="strong"
      sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}
    >
      <Stack spacing={2} sx={{ flex: 1 }}>
        <Box>
          <Typography variant="h6" fontSize="0.9rem" textTransform="uppercase">
            Управление аккаунтом
          </Typography>
          <Typography variant="body2" color="text.secondary" fontSize="0.75rem">
            Создайте аккаунт или войдите в систему, чтобы продолжить работу.
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
          <Tab label="Вход" value="login" disableRipple />
          <Tab label="Регистрация" value="register" disableRipple />
        </Tabs>

        {feedback && (
          <Alert severity={feedback.type} variant="outlined" sx={{ fontSize: '0.75rem' }}>
            {feedback.message}
          </Alert>
        )}

        {mode === 'login' ? (
          <Box component="form" onSubmit={handleLoginSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 1.5 }}>
            <TextField
              label="Email или логин"
              size="small"
              value={loginInput.login}
              onChange={(event) => setLoginInput((prev) => ({ ...prev, login: event.target.value }))}
            />
            <TextField
              label="Пароль"
              size="small"
              type="password"
              value={loginInput.password}
              onChange={(event) => setLoginInput((prev) => ({ ...prev, password: event.target.value }))}
            />
            <CyberpunkButton type="submit" variant="primary" size="medium" disabled={loginMutation.isPending}>
              {loginMutation.isPending ? 'Вход...' : 'Войти'}
            </CyberpunkButton>
          </Box>
        ) : (
          <Box
            component="form"
            onSubmit={handleRegisterSubmit}
            sx={{ display: 'flex', flexDirection: 'column', gap: 1.5 }}
          >
            <TextField
              label="Email"
              size="small"
              value={registerInput.email}
              onChange={(event) => setRegisterInput((prev) => ({ ...prev, email: event.target.value }))}
            />
            <TextField
              label="Имя пользователя"
              size="small"
              value={registerInput.username}
              onChange={(event) => setRegisterInput((prev) => ({ ...prev, username: event.target.value }))}
            />
            <TextField
              label="Пароль"
              size="small"
              type="password"
              value={registerInput.password}
              onChange={(event) => setRegisterInput((prev) => ({ ...prev, password: event.target.value }))}
              helperText="Минимум 8 символов"
            />
            <TextField
              label="Повтор пароля"
              size="small"
              type="password"
              value={registerInput.confirm}
              onChange={(event) => setRegisterInput((prev) => ({ ...prev, confirm: event.target.value }))}
            />
            <Box display="flex" alignItems="center" gap={1}>
              <input
                id="termsAccepted"
                type="checkbox"
                checked={registerInput.agree}
                onChange={(event) => setRegisterInput((prev) => ({ ...prev, agree: event.target.checked }))}
                style={{ accentColor: '#00f7ff' }}
              />
              <label htmlFor="termsAccepted" style={{ fontSize: '0.7rem', color: '#a0a0a0' }}>
                Я принимаю условия использования и политику конфиденциальности
              </label>
            </Box>
            <CyberpunkButton
              type="submit"
              variant="primary"
              size="medium"
              disabled={registerMutation.isPending}
            >
              {registerMutation.isPending ? 'Регистрация...' : 'Создать аккаунт'}
            </CyberpunkButton>
          </Box>
        )}
      </Stack>
    </CompactCard>
  )
}



