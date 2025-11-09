import { Stack, Typography, Divider, Alert } from '@mui/material'
import { GameLayout, MenuPanel, MenuItem, StatsPanel, StatCard } from '@/shared/ui/layout/GameLayout'
import { MailInboxPanel, MailOutboxPanel, MailComposePanel } from '../components'

export function MailSystemPage() {
  const leftPanel = (
    <MenuPanel title="Внутриигровая почта" dense>
      <MenuItem title="Inbox" description="Просматривай входящие письма, забирай вложения, отслеживай COD." />
      <MenuItem title="Outbox" description="Контролируй исходящие рассылки, отзывать письма, создавай флаги." />
      <MenuItem title="Composer" description="Отправляй личные, клановые и системные письма." />
      <Alert severity="info">
        API поддерживает COD, шаблоны и массовые рассылки. Для MVP UI реализованы базовые сценарии.
      </Alert>
    </MenuPanel>
  )

  const rightPanel = (
    <StatsPanel title="Мониторинг">
      <StatCard title="Вложения" value="AttachmentClaim" description="Забор вложений синхронно обновляет инвентарь и журнал." trend="neutral" />
      <StatCard title="COD" value="PaymentRequired" description="COD операции требуют подтверждения экономикой." trend="warning" />
      <StatCard title="Рассылы" value="GM/System" description="Системные рассылки требуют токен сервисов и проходят модерацию." trend="critical" />
      <StatCard title="Лимиты" value="Throttle" description="Антиспам блокирует >10 писем/минуту и проверяет ключи." trend="positive" />
    </StatsPanel>
  )

  return (
    <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
      <Stack spacing={4} divider={<Divider flexItem />}>
        <Stack spacing={2}>
          <Typography variant="h4">Почтовая система</Typography>
          <Typography variant="body2" color="text.secondary">
            Управляй входящими и исходящими письмами, обрабатывай COD операции и настройки фильтров. Все операции выполняются через React Query хуки, сгенерированные Orval.
          </Typography>
        </Stack>

        <MailComposePanel />
        <MailInboxPanel />
        <MailOutboxPanel />
      </Stack>
    </GameLayout>
  )
}


