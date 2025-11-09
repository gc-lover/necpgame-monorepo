import { useEffect, useState } from 'react'
import { Alert, Box, Stack, TextField, Typography } from '@mui/material'
import {
  useLogout,
  useRefreshToken,
} from '@/api/generated/auth/authentication/authentication/authentication'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { CompactCard } from '@/shared/ui/cards/CompactCard'

type TokenManagementCardProps = {
  accessToken?: string
  refreshToken?: string
  onTokensUpdate?: (payload: { accessToken?: string; refreshToken?: string }) => void
  onLogout?: () => void
}

export function TokenManagementCard({
  accessToken,
  refreshToken,
  onTokensUpdate,
  onLogout,
}: TokenManagementCardProps) {
  const [localAccessToken, setLocalAccessToken] = useState(accessToken ?? '')
  const [localRefreshToken, setLocalRefreshToken] = useState(refreshToken ?? '')
  const [feedback, setFeedback] = useState<{ type: 'success' | 'error'; message: string } | null>(null)

  const refreshMutation = useRefreshToken()
  const logoutMutation = useLogout()

  useEffect(() => {
    setLocalAccessToken(accessToken ?? localStorage.getItem('auth_token') ?? '')
    setLocalRefreshToken(refreshToken ?? localStorage.getItem('refresh_token') ?? '')
  }, [accessToken, refreshToken])

  const handleRefresh = () => {
    setFeedback(null)

    if (!localRefreshToken) {
      setFeedback({ type: 'error', message: 'Нет refresh токена для обновления' })
      return
    }

    refreshMutation.mutate(
      { data: { refresh_token: localRefreshToken } },
      {
        onSuccess: (data) => {
          const nextAccessToken = data.access_token ?? ''
          if (nextAccessToken) {
            localStorage.setItem('auth_token', nextAccessToken)
          }
          setLocalAccessToken(nextAccessToken)
          onTokensUpdate?.({ accessToken: nextAccessToken, refreshToken: localRefreshToken })
          setFeedback({ type: 'success', message: 'Access token обновлён' })
        },
        onError: () => {
          setFeedback({ type: 'error', message: 'Не удалось обновить токен. Возможно, refresh токен устарел.' })
        },
      },
    )
  }

  const handleLogout = () => {
    setFeedback(null)

    if (!localRefreshToken) {
      setFeedback({ type: 'error', message: 'Нет refresh токена для выхода' })
      return
    }

    logoutMutation.mutate(
      { data: { refresh_token: localRefreshToken } },
      {
        onSuccess: () => {
          localStorage.removeItem('auth_token')
          localStorage.removeItem('refresh_token')
          setLocalAccessToken('')
          setLocalRefreshToken('')
          onLogout?.()
          setFeedback({ type: 'success', message: 'Сессия завершена' })
        },
        onError: () => {
          setFeedback({ type: 'error', message: 'Не удалось завершить сессию. Попробуйте снова.' })
        },
      },
    )
  }

  const handleClearTokens = () => {
    localStorage.removeItem('auth_token')
    localStorage.removeItem('refresh_token')
    setLocalAccessToken('')
    setLocalRefreshToken('')
    onLogout?.()
    setFeedback({ type: 'success', message: 'Локальные токены очищены' })
  }

  return (
    <CompactCard color="purple" glowIntensity="medium" sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      <Stack spacing={2} sx={{ flex: 1 }}>
        <Box>
          <Typography variant="h6" fontSize="0.9rem" textTransform="uppercase">
            Управление токенами
          </Typography>
          <Typography variant="body2" color="text.secondary" fontSize="0.75rem">
            Обновляйте access token, завершайте сессии и контролируйте refresh token.
          </Typography>
        </Box>

        {feedback && (
          <Alert severity={feedback.type} variant="outlined" sx={{ fontSize: '0.75rem' }}>
            {feedback.message}
          </Alert>
        )}

        <Stack spacing={1}>
          <Typography variant="caption" color="text.secondary">
            Access token
          </Typography>
          <TextField
            value={localAccessToken}
            size="small"
            multiline
            minRows={3}
            placeholder="Нет access token"
            InputProps={{ readOnly: true }}
          />
        </Stack>

        <Stack spacing={1}>
          <Typography variant="caption" color="text.secondary">
            Refresh token
          </Typography>
          <TextField
            value={localRefreshToken}
            size="small"
            multiline
            minRows={2}
            placeholder="Нет refresh token"
            InputProps={{ readOnly: true }}
          />
        </Stack>

        <CyberpunkButton
          variant="primary"
          size="medium"
          disabled={refreshMutation.isPending}
          onClick={handleRefresh}
        >
          {refreshMutation.isPending ? 'Обновление...' : 'Обновить access token'}
        </CyberpunkButton>
        <CyberpunkButton
          variant="secondary"
          size="medium"
          disabled={logoutMutation.isPending}
          onClick={handleLogout}
        >
          {logoutMutation.isPending ? 'Выход...' : 'Выйти и инвалидировать'}
        </CyberpunkButton>
        <CyberpunkButton variant="ghost" size="small" onClick={handleClearTokens}>
          Очистить локальные токены
        </CyberpunkButton>
      </Stack>
    </CompactCard>
  )
}



