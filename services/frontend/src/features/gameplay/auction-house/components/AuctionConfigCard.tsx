import React, { useMemo } from 'react'
import Grid2 from '@mui/material/Unstable_Grid2'
import {
  Card,
  CardContent,
  Chip,
  Divider,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Typography,
} from '@mui/material'
import type {
  AuctionConfig,
  PlayerMarketComparison,
} from '@/api/generated/economy/auction-house/models'

interface AuctionConfigCardProps {
  config?: AuctionConfig
  comparisonOverride?: PlayerMarketComparison[]
}

const formatValue = (value: unknown): string => {
  if (value === null || value === undefined) {
    return '—'
  }
  if (Array.isArray(value)) {
    return value.length ? value.join(', ') : '—'
  }
  if (typeof value === 'number') {
    return Number.isFinite(value) ? value.toLocaleString('ru-RU') : '—'
  }
  return String(value)
}

const BooleanChip: React.FC<{ value?: boolean; label?: string }> = ({ value, label }) => {
  if (value === undefined || value === null) {
    return <Chip label={label ?? '—'} size="small" variant="outlined" color="default" />
  }
  return (
    <Chip
      label={label ?? (value ? 'Включено' : 'Отключено')}
      size="small"
      color={value ? 'success' : 'default'}
      variant={value ? 'filled' : 'outlined'}
    />
  )
}

const Section: React.FC<{ title: string; items: Array<{ label: string; value: React.ReactNode }> }> = ({ title, items }) => {
  return (
    <Stack spacing={1.5}>
      <Typography variant="subtitle1" fontWeight={600} fontSize="0.95rem">
        {title}
      </Typography>
      <Stack spacing={1}>
        {items.map((item) => (
          <Stack key={item.label} spacing={0.25}>
            <Typography variant="caption" color="text.secondary" fontSize="0.7rem" component="span">
              {item.label}
            </Typography>
            <Typography variant="body2" fontSize="0.85rem" component="div">
              {item.value}
            </Typography>
          </Stack>
        ))}
      </Stack>
    </Stack>
  )
}

export const AuctionConfigCard: React.FC<AuctionConfigCardProps> = ({ config, comparisonOverride }) => {
  const comparison = useMemo(() => comparisonOverride ?? config?.comparison ?? [], [comparisonOverride, config])

  return (
    <Card variant="outlined">
      <CardContent sx={{ p: 3 }}>
        <Stack spacing={3}>
          <Stack spacing={0.5}>
            <Typography variant="h6" fontWeight={700} fontSize="1.1rem">
              Конфигурация аукционного дома
            </Typography>
            <Typography variant="body2" color="text.secondary" fontSize="0.85rem">
              Живые правила для экономики, ставок, комиссий и расписаний. Данные приходят из `economy-service`
            </Typography>
          </Stack>
          <Grid2 container spacing={3}>
            <Grid2 xs={12} md={6}>
              <Section
                title="Создание лотов"
                items={[
                  { label: 'Минимальная стартовая ставка', value: formatValue(config?.creation?.minStartingBid) },
                  { label: 'Максимальная длительность (часов)', value: formatValue(config?.creation?.maxDurationHours) },
                  { label: 'Допустимые длительности', value: formatValue(config?.creation?.allowedDurations) },
                  {
                    label: 'Минимальный коэффициент buyout',
                    value:
                      config?.creation?.buyoutMinRatio !== undefined
                        ? `${(config.creation.buyoutMinRatio * 100).toFixed(1)}%`
                        : '—',
                  },
                  { label: 'Лимит активных лотов на игрока', value: formatValue(config?.creation?.listingLimit) },
                  {
                    label: 'Автобид',
                    value: <BooleanChip value={config?.creation?.autoBidEnabled} />,
                  },
                ]}
              />
            </Grid2>
            <Grid2 xs={12} md={6}>
              <Section
                title="Ставки"
                items={[
                  { label: 'Минимальное увеличение', value: config?.bidding?.minIncrementPercent ? `${config.bidding.minIncrementPercent}%` : '—' },
                  { label: 'Порог авто-продления (мин)', value: formatValue(config?.bidding?.autoExtendThresholdMinutes) },
                  { label: 'Продление при ставке (мин)', value: formatValue(config?.bidding?.autoExtendMinutes) },
                  { label: 'Макс. активных ставок', value: formatValue(config?.bidding?.maxActiveBids) },
                  { label: 'Валюта торгов', value: config?.bidding?.currency ?? '€$' },
                ]}
              />
            </Grid2>
            <Grid2 xs={12} md={6}>
              <Section
                title="Buyout"
                items={[
                  { label: 'Статус', value: <BooleanChip value={config?.buyout?.enabled} /> },
                  { label: 'Мин. цена', value: formatValue(config?.buyout?.minPrice) },
                  { label: 'Макс. цена', value: formatValue(config?.buyout?.maxPrice) },
                  { label: 'Кулдаун (сек)', value: formatValue(config?.buyout?.cooldownSeconds) },
                  { label: 'Подтверждение', value: <BooleanChip value={config?.buyout?.requiresConfirmation} /> },
                ]}
              />
            </Grid2>
            <Grid2 xs={12} md={6}>
              <Section
                title="Комиссии"
                items={[
                  { label: 'Листинг (фикс.)', value: config?.commission?.listingFee ? `${config.commission.listingFee} €$` : '—' },
                  {
                    label: 'Комиссия с продажи',
                    value:
                      config?.commission?.saleCommissionPercent !== undefined
                        ? `${config.commission.saleCommissionPercent}%`
                        : '—',
                  },
                  {
                    label: 'Комиссия buyout',
                    value:
                      config?.commission?.buyoutCommissionPercent !== undefined
                        ? `${config.commission.buyoutCommissionPercent}%`
                        : '—',
                  },
                  { label: 'Политика возвратов', value: config?.commission?.refundPolicy ?? 'Следуем настройкам сервиса' },
                ]}
              />
            </Grid2>
            <Grid2 xs={12}>
              <Section
                title="Планировщик"
                items={[
                  { label: 'Частота (мин)', value: formatValue(config?.scheduler?.frequencyMinutes) },
                  { label: 'Размер батча', value: formatValue(config?.scheduler?.batchSize) },
                  { label: 'Грейс-период (мин)', value: formatValue(config?.scheduler?.gracePeriodMinutes) },
                  { label: 'Стратегия блокировок', value: config?.scheduler?.lockingStrategy ?? '—' },
                ]}
              />
            </Grid2>
          </Grid2>
          {comparison.length > 0 && (
            <Stack spacing={1.5}>
              <Divider />
              <Typography variant="subtitle1" fontWeight={600} fontSize="0.95rem">
                Сравнение с Player Market
              </Typography>
              <Table size="small" sx={{ '& th': { fontSize: '0.75rem' }, '& td': { fontSize: '0.75rem' } }}>
                <TableHead>
                  <TableRow>
                    <TableCell>Метрика</TableCell>
                    <TableCell>Auction House</TableCell>
                    <TableCell>Player Market</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {comparison.map((row) => (
                    <TableRow key={`${row.feature}-${row.auctionHouse}-${row.playerMarket}`}>
                      <TableCell>{row.feature ?? '—'}</TableCell>
                      <TableCell>{row.auctionHouse ?? '—'}</TableCell>
                      <TableCell>{row.playerMarket ?? '—'}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </Stack>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

export default AuctionConfigCard
