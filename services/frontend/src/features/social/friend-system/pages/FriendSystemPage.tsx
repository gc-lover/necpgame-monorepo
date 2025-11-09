import { Stack, Typography, Divider, Alert } from '@mui/material'
import { GameLayout, MenuPanel, MenuItem, StatsPanel, StatCard } from '@/shared/ui/layout/GameLayout'
import { FriendManagementPanel } from '../components/FriendManagementPanel'

export function FriendSystemPage() {
  const leftPanel = (
    <MenuPanel title="Система друзей" dense>
      <MenuItem title="Friend List" description="Получение списка друзей, онлайн-статуса и фильтрация по player_id." />
      <MenuItem title="Запросы" description="Отправка запросов, принятие, удаление и блокировка." />
      <MenuItem title="Игнор-лист" description="BlockPlayer API обновляет список игнорируемых игроков." />
      <Alert severity="info">Все операции работают через React Query хуки, backend возвращает JSON заглушки.</Alert>
    </MenuPanel>
  )

  const rightPanel = (
    <StatsPanel title="Метрики">
      <StatCard title="Статус" value="online/offline" description="Каждый элемент списка друзей содержит статус." trend="positive" />
      <StatCard title="Pending" value="friend_requests" description="Обрабатывай по requestId." trend="warning" />
      <StatCard title="Социальные ограничения" value="throttle" description="Backend ограничивает частоту запросов." trend="critical" />
      <StatCard title="Игнор" value="blocklist" description="Блокировка хранится отдельно от дружеских связей." trend="neutral" />
    </StatsPanel>
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      <Stack spacing={4} divider={<Divider flexItem />}>
        <Stack spacing={2}>
          <Typography variant="h4">Friend System</Typography>
          <Typography variant="body2" color="text.secondary">
            Интерфейс для управления дружескими связями, запросами и блокировками. Поддерживает все основные операции API.
          </Typography>
        </Stack>

        <FriendManagementPanel />
      </Stack>
    </GameLayout>
  )
}


