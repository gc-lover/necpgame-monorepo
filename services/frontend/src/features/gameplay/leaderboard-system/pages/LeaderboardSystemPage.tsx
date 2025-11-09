import { Stack, Typography, Divider, Alert } from '@mui/material'
import { GameLayout, MenuPanel, MenuItem, StatsPanel, StatCard } from '@/shared/ui/layout/GameLayout'
import { LeaderboardDashboardPanel } from '../components/LeaderboardDashboardPanel'

export function LeaderboardSystemPage() {
  const leftPanel = (
    <MenuPanel title="Рейтинги" dense>
      <MenuItem title="Global" description="Топ по категориям: уровень, богатство, PvP и т.д." />
      <MenuItem title="Friends" description="Рейтинги среди друзей — социальное сравнение." />
      <MenuItem title="Guild" description="Топ гильдий по уровню, территориям и рейдам." />
      <MenuItem title="Seasonal" description="Сезонные таблицы с автоматическим сбросом и наградами." />
      <Alert severity="info">
        UI использует React Query и готов к live-обновлениям через WebSocket. Достаточно инвалидации кэша при событии.
      </Alert>
    </MenuPanel>
  )

  const rightPanel = (
    <StatsPanel title="Метрики рейтингов">
      <StatCard title="Топ игроков" value="100" description="По умолчанию выдаём top 100 записей." trend="neutral" />
      <StatCard title="Обновление" value="Realtime" description="Redis Sorted Sets, latency < 100ms." trend="positive" />
      <StatCard title="Антибот" value="Queue & review" description="Админ инструмент корректировки очков." trend="warning" />
      <StatCard title="Сезоны" value="4/год" description="Каждый сезон — уникальные награды и правила." trend="critical" />
    </StatsPanel>
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      <Stack spacing={4} divider={<Divider flexItem />}>
        <Stack spacing={2}>
          <Typography variant="h4">Leaderboard System</Typography>
          <Typography variant="body2" color="text.secondary">
            Унифицированная панель управления глобальными, социальными и сезонными рейтингами.
          </Typography>
        </Stack>

        <LeaderboardDashboardPanel />
      </Stack>
    </GameLayout>
  )
}


