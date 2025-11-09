import { Stack, Typography, Divider, Alert } from '@mui/material'
import { GameLayout, MenuPanel, MenuItem, StatsPanel, StatCard } from '@/shared/ui/layout/GameLayout'
import { NotificationCenterPanel } from '../components/NotificationCenterPanel'

export function NotificationSystemPage() {
  const leftPanel = (
    <MenuPanel title="Уведомления" dense>
      <MenuItem title="Фильтры" description="Поиск по player_id, типу, непрочитанным и лимиту." />
      <MenuItem title="Маркировка" description="Пометка одного или всех уведомлений как прочитанных." />
      <MenuItem title="Отправка" description="Создание тестовых уведомлений и emailable событий." />
      <Alert severity="info">
        Реалтайм пуш будет подключаться через WebSocket. UI готов: достаточно подписаться на канал и обновлять QueryClient.
      </Alert>
    </MenuPanel>
  )

  const rightPanel = (
    <StatsPanel title="Каналы связи">
      <StatCard title="In-game" value="Toast / Modal" description="Обновление через REST + WebSocket." trend="positive" />
      <StatCard title="Email" value="Optional" description="Только критичные события." trend="neutral" />
      <StatCard title="Push" value="Готово" description="Webhook и FCM в roadmap." trend="warning" />
      <StatCard title="QoS" value="Retry + DLQ" description="Backend гарантирует доставку." trend="critical" />
    </StatsPanel>
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      <Stack spacing={4} divider={<Divider flexItem />}>
        <Stack spacing={2}>
          <Typography variant="h4">Notification System</Typography>
          <Typography variant="body2" color="text.secondary">
            Контроль уведомлений игрока: просмотр, фильтры, массовая пометка и отправка тестовых событий.
          </Typography>
        </Stack>

        <NotificationCenterPanel />
      </Stack>
    </GameLayout>
  )
}


