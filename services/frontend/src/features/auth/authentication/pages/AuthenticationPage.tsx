import { useEffect, useState } from 'react'
import {
  Alert,
  Box,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Typography,
} from '@mui/material'
import LockOpenIcon from '@mui/icons-material/LockOpen'
import VpnKeyIcon from '@mui/icons-material/VpnKey'
import TokenIcon from '@mui/icons-material/Token'
import SecurityIcon from '@mui/icons-material/Security'
import PublicIcon from '@mui/icons-material/Public'
import InfoOutlinedIcon from '@mui/icons-material/InfoOutlined'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import {
  LoginRegisterCard,
  PasswordRecoveryCard,
  TokenManagementCard,
  AccountRolesCard,
  OAuthProvidersCard,
  SessionInfoCard,
} from '../components'

type Section = 'overview' | 'auth' | 'password' | 'tokens' | 'roles' | 'oauth'

type SessionState = {
  accessToken?: string
  refreshToken?: string
  roles?: string[]
}

export function AuthenticationPage() {
  const [section, setSection] = useState<Section>('overview')
  const [session, setSession] = useState<SessionState>({
    accessToken: localStorage.getItem('auth_token') ?? undefined,
    refreshToken: localStorage.getItem('refresh_token') ?? undefined,
    roles: [],
  })

  useEffect(() => {
    setSession((prev) => ({
      ...prev,
      accessToken: localStorage.getItem('auth_token') ?? prev.accessToken,
      refreshToken: localStorage.getItem('refresh_token') ?? prev.refreshToken,
    }))
  }, [])

  const handleAuthSuccess = (payload: { accessToken?: string; refreshToken?: string; roles?: string[] }) => {
    setSession((prev) => ({
      ...prev,
      accessToken: payload.accessToken ?? prev.accessToken,
      refreshToken: payload.refreshToken ?? prev.refreshToken,
      roles: payload.roles ?? prev.roles,
    }))
  }

  const handleTokensUpdate = (payload: { accessToken?: string; refreshToken?: string }) => {
    setSession((prev) => ({
      ...prev,
      accessToken: payload.accessToken ?? prev.accessToken,
      refreshToken: payload.refreshToken ?? prev.refreshToken,
    }))
  }

  const handleLogout = () => {
    setSession({ accessToken: undefined, refreshToken: undefined, roles: [] })
  }

  const leftPanel = (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, height: '100%' }}>
      <Box>
        <Typography variant="h6" sx={{ color: 'primary.main', fontSize: '0.9rem', fontWeight: 700, textTransform: 'uppercase' }}>
          Аутентификация
        </Typography>
        <Typography variant="caption" color="text.secondary" fontSize="0.7rem">
          Управление аккаунтами, токенами и ролями. Критическая часть MVP.
        </Typography>
      </Box>

      <List dense>
        <ListItem disablePadding>
          <ListItemButton
            selected={section === 'overview'}
            onClick={() => setSection('overview')}
            sx={{ borderRadius: 1, mb: 0.5 }}
          >
            <ListItemIcon sx={{ minWidth: 36 }}>
              <InfoOutlinedIcon fontSize="small" />
            </ListItemIcon>
            <ListItemText primary="Обзор" primaryTypographyProps={{ fontSize: '0.8rem' }} />
          </ListItemButton>
        </ListItem>

        <ListItem disablePadding>
          <ListItemButton
            selected={section === 'auth'}
            onClick={() => setSection('auth')}
            sx={{ borderRadius: 1, mb: 0.5 }}
          >
            <ListItemIcon sx={{ minWidth: 36 }}>
              <LockOpenIcon fontSize="small" />
            </ListItemIcon>
            <ListItemText primary="Вход и регистрация" primaryTypographyProps={{ fontSize: '0.8rem' }} />
          </ListItemButton>
        </ListItem>

        <ListItem disablePadding>
          <ListItemButton
            selected={section === 'password'}
            onClick={() => setSection('password')}
            sx={{ borderRadius: 1, mb: 0.5 }}
          >
            <ListItemIcon sx={{ minWidth: 36 }}>
              <VpnKeyIcon fontSize="small" />
            </ListItemIcon>
            <ListItemText primary="Восстановление пароля" primaryTypographyProps={{ fontSize: '0.8rem' }} />
          </ListItemButton>
        </ListItem>

        <ListItem disablePadding>
          <ListItemButton
            selected={section === 'tokens'}
            onClick={() => setSection('tokens')}
            sx={{ borderRadius: 1, mb: 0.5 }}
          >
            <ListItemIcon sx={{ minWidth: 36 }}>
              <TokenIcon fontSize="small" />
            </ListItemIcon>
            <ListItemText primary="Токены и сессии" primaryTypographyProps={{ fontSize: '0.8rem' }} />
          </ListItemButton>
        </ListItem>

        <ListItem disablePadding>
          <ListItemButton
            selected={section === 'roles'}
            onClick={() => setSection('roles')}
            sx={{ borderRadius: 1, mb: 0.5 }}
          >
            <ListItemIcon sx={{ minWidth: 36 }}>
              <SecurityIcon fontSize="small" />
            </ListItemIcon>
            <ListItemText primary="Роли и права" primaryTypographyProps={{ fontSize: '0.8rem' }} />
          </ListItemButton>
        </ListItem>

        <ListItem disablePadding>
          <ListItemButton
            selected={section === 'oauth'}
            onClick={() => setSection('oauth')}
            sx={{ borderRadius: 1, mb: 0.5 }}
          >
            <ListItemIcon sx={{ minWidth: 36 }}>
              <PublicIcon fontSize="small" />
            </ListItemIcon>
            <ListItemText primary="OAuth провайдеры" primaryTypographyProps={{ fontSize: '0.8rem' }} />
          </ListItemButton>
        </ListItem>
      </List>
    </Box>
  )

  const renderContent = () => {
    switch (section) {
      case 'auth':
        return <LoginRegisterCard onAuthSuccess={handleAuthSuccess} />
      case 'password':
        return <PasswordRecoveryCard />
      case 'tokens':
        return (
          <TokenManagementCard
            accessToken={session.accessToken}
            refreshToken={session.refreshToken}
            onTokensUpdate={handleTokensUpdate}
            onLogout={handleLogout}
          />
        )
      case 'roles':
        return <AccountRolesCard enabled={Boolean(session.accessToken)} />
      case 'oauth':
        return <OAuthProvidersCard />
      case 'overview':
      default:
        return (
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
            <Typography variant="h5" fontSize="1.1rem" fontWeight="bold" color="primary">
              Центр аутентификации
            </Typography>
            <Alert severity="info" sx={{ fontSize: '0.8rem' }}>
              Система аутентификации — критическая часть MVP. Здесь тестируются сценарии регистрации, входа,
              восстановления пароля, управления токенами и выдачи ролей. Используйте токены для доступа к защищённым
              API и проверьте обработку ошибок.
            </Alert>
            <Typography variant="body2" fontSize="0.85rem">
              • Регистрация создаёт аккаунт и возвращает базовую информацию.
            </Typography>
            <Typography variant="body2" fontSize="0.85rem">
              • Login выдаёт access и refresh токены. Access используется для всех защищённых API, refresh позволяет
              обновить сессию.
            </Typography>
            <Typography variant="body2" fontSize="0.85rem">
              • Logout инвалидирует refresh токен и очищает локальное состояние.
            </Typography>
            <Typography variant="body2" fontSize="0.85rem">
              • Password reset покрывает запрос письма и применение нового пароля.
            </Typography>
            <Typography variant="body2" fontSize="0.85rem">
              • OAuth демонстрирует внешний логин (Steam/Google/Discord). Для локального теста требуется настроенный
              backend.
            </Typography>
          </Box>
        )
    }
  }

  const rightPanel = (
    <StatsPanel>
      <SessionInfoCard accessToken={session.accessToken} refreshToken={session.refreshToken} roles={session.roles} />
      <Alert severity="warning" sx={{ fontSize: '0.75rem' }}>
        Никогда не публикуйте токены в публичных каналах. Для демо используйте тестовые аккаунты или локальный сервер.
      </Alert>
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        После обновления access token просмотрите вкладку &laquo;Роли&raquo;, чтобы убедиться, что права приложения
        соответствуют ожиданиям.
      </Alert>
    </StatsPanel>
  )

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      <Header />
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2, minHeight: 0, overflowY: 'auto' }}>
          {renderContent()}
        </Box>
      </GameLayout>
    </Box>
  )
}



