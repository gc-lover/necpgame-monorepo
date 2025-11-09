import { Stack, Typography, Divider, Alert } from '@mui/material'
import { GameLayout, MenuPanel, MenuItem, StatsPanel, StatCard } from '@/shared/ui/layout/GameLayout'
import { GuildManagementPanel } from '../components/GuildManagementPanel'

export function GuildSystemPage() {
  const leftPanel = (
    <MenuPanel title="Гильдейские механики" dense>
      <MenuItem title="Создание" description="Основатель формирует гильдию, задаёт название и тег." />
      <MenuItem title="Участники" description="Приглашения, вступление и выход работают через REST." />
      <MenuItem title="Ранги" description="API возвращает ранги и вклад участника, UI отображает их списком." />
      <Alert severity="warning">
        Гильдейский банк и войны пока в режиме заглушек. UI покажет статус, когда появятся полноценные данные.
      </Alert>
    </MenuPanel>
  )

  const rightPanel = (
    <StatsPanel title="Контроль гильдии">
      <StatCard title="Уровень" value="1-25" description="Растёт от вкладов участников." trend="positive" />
      <StatCard title="Слоты" value="100 базово" description="С увеличением уровня расширяется." trend="neutral" />
      <StatCard title="Банк" value="Заглушка" description="Поднятие функционала в следующих задачах." trend="warning" />
      <StatCard title="Войны" value="В разработке" description="Фронтенд готов отображать статус." trend="critical" />
    </StatsPanel>
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      <Stack spacing={4} divider={<Divider flexItem />}>
        <Stack spacing={2}>
          <Typography variant="h4">Guild System</Typography>
          <Typography variant="body2" color="text.secondary">
            Управляй гильдиями: создавай, приглашай, отслеживай ранги и вклад. Интерфейс построен на хуках Orval и покрывает критичные сценарии.
          </Typography>
        </Stack>

        <GuildManagementPanel />
      </Stack>
    </GameLayout>
  )
}


