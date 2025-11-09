import { Stack, Typography, Divider, Alert } from '@mui/material'
import { GameLayout, MenuPanel, MenuItem, StatsPanel, StatCard } from '@/shared/ui/layout/GameLayout'
import { PartyManagementPanel } from '../components/PartyManagementPanel'

export function PartySystemPage() {
  const leftPanel = (
    <MenuPanel title="Группы (Party)" dense>
      <MenuItem title="Создание и инвайты" description="Лидер создаёт группу, приглашает игроков и настраивает режим лута." />
      <MenuItem title="Join/Leave" description="Игроки могут присоединиться и выйти в любой момент, UI отдаёт character_id." />
      <MenuItem title="Мониторинг" description="Отслеживание состава и ролей происходит через PartyDetails endpoint." />
      <Alert severity="info">
        Напоминание: backend сейчас отдаёт заглушки для shared quest progress. UI отражает текущие контрактные поля.
      </Alert>
    </MenuPanel>
  )

  const rightPanel = (
    <StatsPanel title="Сводка группы">
      <StatCard title="Размер" value="2-5" description="Гибко задаётся лидером" trend="neutral" />
      <StatCard title="Лут" value="personal/need_greed/master" description="Поддержка режимов мастер-лутера и need/greed" trend="warning" />
      <StatCard title="Инвайты" value="Leader only" description="Только лидер может приглашать" trend="critical" />
      <StatCard title="VC/Chat" value="Soon™" description="Интеграция с party chat и voice channel планируется отдельно" trend="positive" />
    </StatsPanel>
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      <Stack spacing={4} divider={<Divider flexItem />}>
        <Stack spacing={2}>
          <Typography variant="h4">Party System</Typography>
          <Typography variant="body2" color="text.secondary">
            Инструменты управления небольшими группами в кооперативном контенте. UI покрывает создание группы, приглашения, присоединение и состав.
          </Typography>
        </Stack>

        <PartyManagementPanel />
      </Stack>
    </GameLayout>
  )
}


