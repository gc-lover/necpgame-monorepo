import { Stack, Typography, Divider, Alert } from '@mui/material'
import { GameLayout, MenuPanel, MenuItem, StatsPanel, StatCard } from '@/shared/ui/layout/GameLayout'
import { AchievementCatalogPanel } from '../components/AchievementCatalogPanel'
import { PlayerAchievementsPanel } from '../components/PlayerAchievementsPanel'

export function AchievementSystemPage() {
  const leftPanel = (
    <MenuPanel title="Достижения" dense>
      <MenuItem title="Каталог" description="Просматривай все достижения с фильтрами по категориям и редкости." />
      <MenuItem title="Прогресс" description="Отслеживай состояние игрока, обновляй прогресс вручную." />
      <MenuItem title="Титулы" description="Управляй титулами, полученными за достижения." />
      <Alert severity="info">
        Веб-сокеты и автообновление прогресса подключаются через event bus. UI использует React Query для ручного обновления.
      </Alert>
    </MenuPanel>
  )

  const rightPanel = (
    <StatsPanel title="Статистика достижений">
      <StatCard title="Категории" value="8" description="Combat, Social, Economy и др." trend="neutral" />
      <StatCard title="Редкость" value="Common→Legendary" description="Секретные достижения скрыты до разблокировки." trend="warning" />
      <StatCard title="Награды" value="Titles, Badges, Items" description="Используются для метагейма и социальной витрины." trend="positive" />
      <StatCard title="События" value="Event-driven" description="Backend пушит прогресс на основе игровых событий." trend="critical" />
    </StatsPanel>
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      <Stack spacing={4} divider={<Divider flexItem />}>
        <Stack spacing={2}>
          <Typography variant="h4">Achievement System</Typography>
          <Typography variant="body2" color="text.secondary">
            Каталог достижений и управление прогрессом игрока. Покрывает критичные сценарии Tier-2 прогрессии.
          </Typography>
        </Stack>

        <AchievementCatalogPanel />
        <PlayerAchievementsPanel />
      </Stack>
    </GameLayout>
  )
}


