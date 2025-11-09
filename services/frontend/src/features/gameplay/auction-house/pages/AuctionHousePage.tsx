import React, { useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Alert,
  AlertColor,
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  Divider,
  FormControlLabel,
  Grid,
  MenuItem,
  Stack,
  Switch,
  TextField,
  Typography,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import RefreshIcon from '@mui/icons-material/Refresh'
import SyncAltIcon from '@mui/icons-material/SyncAlt'
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive'
import CalculateOutlinedIcon from '@mui/icons-material/CalculateOutlined'
import GameLayout from '@/features/game/components/GameLayout'
import { AuctionConfigCard } from '../components/AuctionConfigCard'
import {
  useGetAuctionConfig,
  useGetAuctionNotificationSamples,
  useCompareAuctionHouseAndPlayerMarket,
} from '@/api/generated/economy/auction-house/auction-config/auction-config'
import { useValidateAuctionCreation } from '@/api/generated/economy/auction-house/auction-rules/auction-rules'
import {
  usePlaceAuctionBid,
  useBuyoutAuction,
  useCancelAuction,
  useExtendAuction,
  useProcessExpiredAuctions,
} from '@/api/generated/economy/auction-house/auction-lifecycle/auction-lifecycle'
import type {
  AuctionValidationResult,
  AuctionCreateRequest,
  AuctionCreateRequestDurationHours,
  BidResult,
  BuyoutResult,
  CancelResult,
  ExtendResult,
  ProcessExpiredResult,
  CompareAuctionHouseAndPlayerMarket200,
  NotificationPayload,
} from '@/api/generated/economy/auction-house/models'
import { AuctionCreateRequestDurationHours as DurationEnum, ExtendRequestMode, ExtendRequestTriggerSource } from '@/api/generated/economy/auction-house/models'

interface OperationLogItem {
  id: string
  timestamp: string
  severity: AlertColor
  title: string
  details?: string
}

interface ValidationFormState {
  sellerId: string
  itemId: string
  quantity: number
  startingBid: number
  buyoutPrice?: number
  durationHours: AuctionCreateRequestDurationHours
  autoBidEnabled: boolean
  autoBidMax: number
  autoBidIncrement: number
}

interface BidFormState {
  auctionId: string
  bidderId: string
  bidAmount: number
  allowAutoExtend: boolean
}

interface BuyoutFormState {
  auctionId: string
  buyerId: string
  confirm: boolean
}

interface CancelFormState {
  auctionId: string
  reason: string
  confirmFee: boolean
}

interface ExtendFormState {
  auctionId: string
  mode: (typeof ExtendRequestMode)[keyof typeof ExtendRequestMode]
  requestedMinutes: number
  triggerSource: (typeof ExtendRequestTriggerSource)[keyof typeof ExtendRequestTriggerSource]
}

interface ProcessFormState {
  shardIds: string
  windowMinutes: number
  limit: number
}

const nowIso = () => new Date().toISOString()

const toAuctionRequest = (form: ValidationFormState): AuctionCreateRequest => ({
  sellerId: form.sellerId,
  itemId: form.itemId,
  quantity: form.quantity,
  startingBid: form.startingBid,
  buyoutPrice: form.buyoutPrice,
  durationHours: form.durationHours,
  autoBid: form.autoBidEnabled
    ? {
        enabled: true,
        maxAutoBid: form.autoBidMax,
        incrementPercent: form.autoBidIncrement,
      }
    : undefined,
})

const formatErrorMessage = (error: unknown): string => {
  if (error && typeof error === 'object') {
    if ('message' in error && typeof (error as { message: unknown }).message === 'string') {
      return (error as { message: string }).message
    }
  }
  return 'Не удалось выполнить операцию'
}

export const AuctionHousePage: React.FC = () => {
  const navigate = useNavigate()

  const [validationForm, setValidationForm] = useState<ValidationFormState>({
    sellerId: 'player-512',
    itemId: 'item-9901',
    quantity: 1,
    startingBid: 500,
    buyoutPrice: 900,
    durationHours: DurationEnum.NUMBER_24,
    autoBidEnabled: true,
    autoBidMax: 2500,
    autoBidIncrement: 10,
  })
  const [bidForm, setBidForm] = useState<BidFormState>({
    auctionId: 'auction-0001',
    bidderId: 'player-777',
    bidAmount: 620,
    allowAutoExtend: true,
  })
  const [buyoutForm, setBuyoutForm] = useState<BuyoutFormState>({
    auctionId: 'auction-0001',
    buyerId: 'player-888',
    confirm: true,
  })
  const [cancelForm, setCancelForm] = useState<CancelFormState>({
    auctionId: 'auction-0001',
    reason: 'Изменение стратегии продаж',
    confirmFee: true,
  })
  const [extendForm, setExtendForm] = useState<ExtendFormState>({
    auctionId: 'auction-0001',
    mode: ExtendRequestMode.manual,
    requestedMinutes: 30,
    triggerSource: ExtendRequestTriggerSource.admin,
  })
  const [processForm, setProcessForm] = useState<ProcessFormState>({
    shardIds: 'economy-shard-1,economy-shard-2',
    windowMinutes: 15,
    limit: 200,
  })

  const [operationLog, setOperationLog] = useState<OperationLogItem[]>([])
  const [validationResult, setValidationResult] = useState<AuctionValidationResult | null>(null)
  const [bidResult, setBidResult] = useState<BidResult | null>(null)
  const [buyoutResult, setBuyoutResult] = useState<BuyoutResult | null>(null)
  const [cancelResult, setCancelResult] = useState<CancelResult | null>(null)
  const [extendResult, setExtendResult] = useState<ExtendResult | null>(null)
  const [processResult, setProcessResult] = useState<ProcessExpiredResult | null>(null)
  const [compareResult, setCompareResult] = useState<CompareAuctionHouseAndPlayerMarket200 | null>(null)

  const configQuery = useGetAuctionConfig({
    query: {
      retry: false,
    },
  })
  const notificationsQuery = useGetAuctionNotificationSamples({
    query: {
      retry: false,
    },
  })

  const validateMutation = useValidateAuctionCreation()
  const bidMutation = usePlaceAuctionBid()
  const buyoutMutation = useBuyoutAuction()
  const cancelMutation = useCancelAuction()
  const extendMutation = useExtendAuction()
  const processMutation = useProcessExpiredAuctions()
  const compareMutation = useCompareAuctionHouseAndPlayerMarket()

  const pushLog = (entry: Omit<OperationLogItem, 'id' | 'timestamp'>) => {
    const record: OperationLogItem = {
      id: crypto.randomUUID(),
      timestamp: nowIso(),
      severity: entry.severity,
      title: entry.title,
      details: entry.details,
    }
    setOperationLog((prev) => [record, ...prev].slice(0, 8))
  }

  const handleValidate = () => {
    validateMutation.mutate(
      { data: toAuctionRequest(validationForm) },
      {
        onSuccess: (data) => {
          setValidationResult(data)
          pushLog({ severity: 'success', title: 'Валидация лота пройдена', details: `Сообщений: ${data.messages?.length ?? 0}` })
        },
        onError: (error) => {
          setValidationResult(null)
          pushLog({ severity: 'error', title: 'Валидация лота не прошла', details: formatErrorMessage(error) })
        },
      }
    )
  }

  const handleBid = () => {
    bidMutation.mutate(
      {
        auctionId: bidForm.auctionId,
        data: {
          bidderId: bidForm.bidderId,
          bidAmount: bidForm.bidAmount,
          allowAutoExtend: bidForm.allowAutoExtend,
          currency: '€$',
        },
      },
      {
        onSuccess: (data) => {
          setBidResult(data)
          pushLog({ severity: 'success', title: 'Ставка отправлена', details: `Новая ставка: ${data.currentBid ?? bidForm.bidAmount}` })
        },
        onError: (error) => {
          setBidResult(null)
          pushLog({ severity: 'error', title: 'Ставка отклонена', details: formatErrorMessage(error) })
        },
      }
    )
  }

  const handleBuyout = () => {
    buyoutMutation.mutate(
      {
        auctionId: buyoutForm.auctionId,
        data: {
          buyerId: buyoutForm.buyerId,
          confirm: buyoutForm.confirm,
        },
      },
      {
        onSuccess: (data) => {
          setBuyoutResult(data)
          pushLog({ severity: 'success', title: 'Buyout успешно выполнен', details: `Сумма: ${data.finalPrice ?? 'уточняется'}` })
        },
        onError: (error) => {
          setBuyoutResult(null)
          pushLog({ severity: 'error', title: 'Buyout не выполнен', details: formatErrorMessage(error) })
        },
      }
    )
  }

  const handleCancel = () => {
    cancelMutation.mutate(
      {
        auctionId: cancelForm.auctionId,
        data: {
          reason: cancelForm.reason,
          confirmFee: cancelForm.confirmFee,
        },
      },
      {
        onSuccess: (data) => {
          setCancelResult(data)
          pushLog({ severity: 'info', title: 'Аукцион отменён', details: data.status ?? 'Статус уточняется' })
        },
        onError: (error) => {
          setCancelResult(null)
          pushLog({ severity: 'error', title: 'Отмена отклонена', details: formatErrorMessage(error) })
        },
      }
    )
  }

  const handleExtend = () => {
    extendMutation.mutate(
      {
        auctionId: extendForm.auctionId,
        data: {
          mode: extendForm.mode,
          requestedMinutes: extendForm.requestedMinutes,
          triggerSource: extendForm.triggerSource,
        },
      },
      {
        onSuccess: (data) => {
          setExtendResult(data)
          pushLog({ severity: 'success', title: 'Таймер продлён', details: `До: ${data.expiresAt ?? 'неизвестно'}` })
        },
        onError: (error) => {
          setExtendResult(null)
          pushLog({ severity: 'error', title: 'Продление не выполнено', details: formatErrorMessage(error) })
        },
      }
    )
  }

  const handleProcess = () => {
    const shardIds = processForm.shardIds
      .split(',')
      .map((item) => item.trim())
      .filter(Boolean)

    processMutation.mutate(
      {
        data: {
          shardIds: shardIds.length ? shardIds : undefined,
          windowMinutes: processForm.windowMinutes,
          limit: processForm.limit,
        },
      },
      {
        onSuccess: (data) => {
          setProcessResult(data)
          pushLog({ severity: 'info', title: 'Пакет завершения выполнен', details: `Обработано: ${data.processedCount ?? 0}` })
        },
        onError: (error) => {
          setProcessResult(null)
          pushLog({ severity: 'error', title: 'Завершение не выполнено', details: formatErrorMessage(error) })
        },
      }
    )
  }

  const handleCompare = () => {
    compareMutation.mutate(
      {
        data: {
          locale: 'ru-RU',
          includeExamples: true,
        },
      },
      {
        onSuccess: (data) => {
          setCompareResult(data)
          pushLog({ severity: 'success', title: 'Сравнение с Player Market выполнено', details: `Метрик: ${data.comparison?.length ?? 0}` })
        },
        onError: (error) => {
          setCompareResult(null)
          pushLog({ severity: 'error', title: 'Сравнение не выполнено', details: formatErrorMessage(error) })
        },
      }
    )
  }

  const notifications = useMemo<NotificationPayload[]>(() => notificationsQuery.data?.notifications ?? [], [notificationsQuery.data])

  const leftPanel = (
    <Stack spacing={2}>
      <Button
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/game')}
        fullWidth
        variant="outlined"
        size="small"
      >
        Назад в игру
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight={700} color="warning.main">
        Auction House Mechanics
      </Typography>
      <Typography variant="caption" fontSize="0.72rem" color="text.secondary">
        Управление ставками, buyout, расписанием и уведомлениями в рамках economy-service
      </Typography>
      <Divider />
      <Stack spacing={1.5}>
        <Button
          startIcon={<RefreshIcon />}
          variant="contained"
          size="small"
          onClick={() => configQuery.refetch()}
          disabled={configQuery.isFetching}
        >
          Обновить конфиг
        </Button>
        <Button
          startIcon={<SyncAltIcon />}
          variant="outlined"
          size="small"
          onClick={handleCompare}
          disabled={compareMutation.isPending}
        >
          Сравнить с Player Market
        </Button>
        <Button
          startIcon={<NotificationsActiveIcon />}
          variant="outlined"
          size="small"
          onClick={() => notificationsQuery.refetch()}
          disabled={notificationsQuery.isFetching}
        >
          Обновить уведомления
        </Button>
      </Stack>
      <Divider />
      <Stack spacing={1}>
        <Typography variant="subtitle2" fontSize="0.8rem" fontWeight={600}>
          Статусы запросов
        </Typography>
        <Chip
          label={configQuery.isFetching ? 'Конфиг: обновляется' : 'Конфиг: актуален'}
          color={configQuery.isFetching ? 'warning' : 'success'}
          size="small"
        />
        <Chip
          label={notificationsQuery.isFetching ? 'Уведомления: обновление' : `Уведомления: ${notifications.length}`}
          color={notificationsQuery.isFetching ? 'warning' : 'info'}
          size="small"
        />
        <Chip
          label={`Лог операций: ${operationLog.length}`}
          color="default"
          size="small"
        />
      </Stack>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.85rem" fontWeight={600}>
        Журнал операций
      </Typography>
      <Divider />
      {operationLog.length === 0 ? (
        <Typography variant="caption" color="text.secondary">
          Операции ещё не выполнялись. Используйте формы в центре, чтобы прогнать сценарии auction lifecycle.
        </Typography>
      ) : (
        <Stack spacing={1.5} maxHeight={420} sx={{ overflowY: 'auto', pr: 0.5 }}>
          {operationLog.map((entry) => (
            <Alert key={entry.id} severity={entry.severity} variant="outlined" sx={{ fontSize: '0.75rem' }}>
              <Stack spacing={0.5}>
                <Typography variant="subtitle2" fontSize="0.78rem" fontWeight={600}>
                  {entry.title}
                </Typography>
                {entry.details && (
                  <Typography variant="caption" color="text.secondary">
                    {entry.details}
                  </Typography>
                )}
                <Typography variant="caption" color="text.disabled">
                  {new Date(entry.timestamp).toLocaleTimeString('ru-RU')}
                </Typography>
              </Stack>
            </Alert>
          ))}
        </Stack>
      )}
    </Stack>
  )

  const centerContent = (
    <Stack spacing={3}>
      <Stack spacing={0.5}>
        <Typography variant="h5" fontSize="1.2rem" fontWeight={700}>
          Механики аукционного дома
        </Typography>
        <Typography variant="body2" color="text.secondary" fontSize="0.85rem">
          От проверки параметров до buyout и автоматического завершения. Используйте формы ниже, чтобы прогнать цепочку агентов.
        </Typography>
      </Stack>

      {configQuery.isError && (
        <Alert severity="error" variant="outlined">
          Не удалось загрузить конфигурацию: {formatErrorMessage(configQuery.error)}
        </Alert>
      )}

      <AuctionConfigCard
        config={configQuery.data}
        comparisonOverride={compareResult?.comparison}
      />

      <Card variant="outlined">
        <CardContent sx={{ p: 3 }}>
          <Stack spacing={2.5}>
            <Stack direction="row" alignItems="center" spacing={1}>
              <CalculateOutlinedIcon fontSize="small" color="primary" />
              <Typography variant="subtitle1" fontWeight={700} fontSize="0.95rem">
                Уведомления и события аукциона
              </Typography>
            </Stack>
            {notifications.length === 0 ? (
              <Typography variant="caption" color="text.secondary">
                Пока нет примеров уведомлений. Обновите данные из economy-service.
              </Typography>
            ) : (
              <Stack spacing={1.5}>
                {notifications.map((notification, index) => (
                  <Card key={`${notification.event}-${notification.templateId ?? index}`} variant="outlined" sx={{ backgroundColor: 'background.paper' }}>
                    <CardContent sx={{ p: 2 }}>
                      <Stack spacing={0.75}>
                        <Stack direction="row" spacing={1} alignItems="center">
                          <Chip label={notification.event ?? 'event'} size="small" color="secondary" />
                          <Typography variant="caption" color="text.secondary">
                            Каналы: {notification.channels?.join(', ') ?? '—'}
                          </Typography>
                        </Stack>
                        <Typography variant="body2" fontSize="0.8rem">
                          {notification.message ?? 'Тело уведомления отсутствует'}
                        </Typography>
                      </Stack>
                    </CardContent>
                  </Card>
                ))}
              </Stack>
            )}
          </Stack>
        </CardContent>
      </Card>

      <Card variant="outlined">
        <CardContent sx={{ p: 3 }}>
          <Stack spacing={3}>
            <Stack spacing={0.5}>
              <Typography variant="subtitle1" fontWeight={700} fontSize="0.95rem">
                Формы управления аукционом
              </Typography>
              <Typography variant="caption" color="text.secondary">
                Эти действия соответствуют сценариям Frontend Agent из `ФРОНТТАСК.MD`: валидация лота, ставки, buyout, отмена и обработка истёкших лотов.
              </Typography>
            </Stack>

            <Stack spacing={2.5}>
              <Stack spacing={1.5}>
                <Typography variant="subtitle2" fontWeight={600} fontSize="0.9rem">
                  1. Проверка параметров создания лота
                </Typography>
                <Grid container spacing={2}>
                  <Grid item xs={12} md={6}>
                    <TextField
                      label="Seller ID"
                      value={validationForm.sellerId}
                      size="small"
                      onChange={(e) => setValidationForm((prev) => ({ ...prev, sellerId: e.target.value }))}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <TextField
                      label="Item ID"
                      value={validationForm.itemId}
                      size="small"
                      onChange={(e) => setValidationForm((prev) => ({ ...prev, itemId: e.target.value }))}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={3}>
                    <TextField
                      label="Количество"
                      type="number"
                      value={validationForm.quantity}
                      size="small"
                      onChange={(e) => setValidationForm((prev) => ({ ...prev, quantity: Number(e.target.value) || 0 }))}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={3}>
                    <TextField
                      label="Стартовая ставка"
                      type="number"
                      value={validationForm.startingBid}
                      size="small"
                      onChange={(e) => setValidationForm((prev) => ({ ...prev, startingBid: Number(e.target.value) || 0 }))}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={3}>
                    <TextField
                      label="Buyout"
                      type="number"
                      value={validationForm.buyoutPrice ?? ''}
                      size="small"
                      onChange={(e) =>
                        setValidationForm((prev) => ({
                          ...prev,
                          buyoutPrice: e.target.value === '' ? undefined : Number(e.target.value),
                        }))
                      }
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={3}>
                    <TextField
                      label="Длительность (ч)"
                      select
                      value={validationForm.durationHours}
                      size="small"
                      onChange={(e) =>
                        setValidationForm((prev) => ({
                          ...prev,
                          durationHours: Number(e.target.value) as AuctionCreateRequestDurationHours,
                        }))
                      }
                      fullWidth
                    >
                      {Object.values(DurationEnum).map((value) => (
                        <MenuItem key={value} value={value}>
                          {value}
                        </MenuItem>
                      ))}
                    </TextField>
                  </Grid>
                  <Grid item xs={12} md={4}>
                    <FormControlLabel
                      control={
                        <Switch
                          checked={validationForm.autoBidEnabled}
                          onChange={(e) => setValidationForm((prev) => ({ ...prev, autoBidEnabled: e.target.checked }))}
                          size="small"
                        />
                      }
                      label="Включить автобид"
                    />
                  </Grid>
                  <Grid item xs={12} md={4}>
                    <TextField
                      label="Автобид max"
                      type="number"
                      value={validationForm.autoBidMax}
                      size="small"
                      onChange={(e) => setValidationForm((prev) => ({ ...prev, autoBidMax: Number(e.target.value) || 0 }))}
                      fullWidth
                      disabled={!validationForm.autoBidEnabled}
                    />
                  </Grid>
                  <Grid item xs={12} md={4}>
                    <TextField
                      label="Шаг автобида (%)"
                      type="number"
                      value={validationForm.autoBidIncrement}
                      size="small"
                      onChange={(e) => setValidationForm((prev) => ({ ...prev, autoBidIncrement: Number(e.target.value) || 0 }))}
                      fullWidth
                      disabled={!validationForm.autoBidEnabled}
                    />
                  </Grid>
                </Grid>
                <Stack direction="row" spacing={1} alignItems="center">
                  <Button variant="contained" size="small" onClick={handleValidate} disabled={validateMutation.isPending}>
                    Проверить лот
                  </Button>
                  {validateMutation.isPending && <Typography variant="caption" color="text.secondary">Отправка...</Typography>}
                </Stack>
                {validationResult && (
                  <Alert severity={validationResult.valid ? 'success' : 'warning'} variant="outlined">
                    <Stack spacing={0.5}>
                      <Typography variant="subtitle2" fontSize="0.8rem" fontWeight={600}>
                        {validationResult.valid ? 'Лот валиден и может быть создан' : 'Есть замечания по созданию лота'}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        Резервирование: {validationResult.reserved ? 'да' : 'нет'}
                      </Typography>
                      {(validationResult.messages ?? []).slice(0, 3).map((item) => (
                        <Typography key={item.code} variant="caption" color="text.secondary">
                          {item.code}: {item.description}
                        </Typography>
                      ))}
                    </Stack>
                  </Alert>
                )}
              </Stack>

              <Divider />

              <Stack spacing={1.5}>
                <Typography variant="subtitle2" fontWeight={600} fontSize="0.9rem">
                  2. Управление живыми аукционами
                </Typography>
                <Grid container spacing={2}>
                  <Grid item xs={12} md={4}>
                    <TextField
                      label="Auction ID"
                      value={bidForm.auctionId}
                      size="small"
                      onChange={(e) => {
                        setBidForm((prev) => ({ ...prev, auctionId: e.target.value }))
                        setBuyoutForm((prev) => ({ ...prev, auctionId: e.target.value }))
                        setCancelForm((prev) => ({ ...prev, auctionId: e.target.value }))
                        setExtendForm((prev) => ({ ...prev, auctionId: e.target.value }))
                      }}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={4}>
                    <TextField
                      label="Bidder ID"
                      value={bidForm.bidderId}
                      size="small"
                      onChange={(e) => setBidForm((prev) => ({ ...prev, bidderId: e.target.value }))}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={4}>
                    <TextField
                      label="Сумма ставки"
                      type="number"
                      value={bidForm.bidAmount}
                      size="small"
                      onChange={(e) => setBidForm((prev) => ({ ...prev, bidAmount: Number(e.target.value) || 0 }))}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={4}>
                    <FormControlLabel
                      control={
                        <Switch
                          checked={bidForm.allowAutoExtend}
                          onChange={(e) => setBidForm((prev) => ({ ...prev, allowAutoExtend: e.target.checked }))}
                          size="small"
                        />
                      }
                      label="Разрешить авто-продление"
                    />
                  </Grid>
                </Grid>
                <Stack direction="row" spacing={1}>
                  <Button variant="outlined" size="small" onClick={handleBid} disabled={bidMutation.isPending}>
                    Сделать ставку
                  </Button>
                  {bidResult && (
                    <Alert severity="success" variant="outlined" sx={{ fontSize: '0.72rem' }}>
                      Текущая ставка: {bidResult.currentBid ?? '—'} • Лидер: {bidResult.leaderId ?? '—'}
                    </Alert>
                  )}
                </Stack>

                <Grid container spacing={2}>
                  <Grid item xs={12} md={6}>
                    <TextField
                      label="Buyer ID"
                      value={buyoutForm.buyerId}
                      size="small"
                      onChange={(e) => setBuyoutForm((prev) => ({ ...prev, buyerId: e.target.value }))}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <FormControlLabel
                      control={
                        <Switch
                          checked={buyoutForm.confirm}
                          onChange={(e) => setBuyoutForm((prev) => ({ ...prev, confirm: e.target.checked }))}
                          size="small"
                        />
                      }
                      label="Подтвердить buyout"
                    />
                  </Grid>
                </Grid>
                <Stack direction="row" spacing={1}>
                  <Button variant="outlined" size="small" color="success" onClick={handleBuyout} disabled={buyoutMutation.isPending}>
                    Выполнить buyout
                  </Button>
                  {buyoutResult && (
                    <Alert severity="success" variant="outlined" sx={{ fontSize: '0.72rem' }}>
                      Покупатель: {buyoutResult.buyerId ?? buyoutForm.buyerId} • Цена: {buyoutResult.finalPrice ?? '—'}
                    </Alert>
                  )}
                </Stack>

                <Grid container spacing={2}>
                  <Grid item xs={12} md={8}>
                    <TextField
                      label="Причина отмены"
                      value={cancelForm.reason}
                      size="small"
                      onChange={(e) => setCancelForm((prev) => ({ ...prev, reason: e.target.value }))}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={4}>
                    <FormControlLabel
                      control={
                        <Switch
                          checked={cancelForm.confirmFee}
                          onChange={(e) => setCancelForm((prev) => ({ ...prev, confirmFee: e.target.checked }))}
                          size="small"
                        />
                      }
                      label="Подтвердить штраф"
                    />
                  </Grid>
                </Grid>
                <Stack direction="row" spacing={1}>
                  <Button variant="outlined" size="small" color="warning" onClick={handleCancel} disabled={cancelMutation.isPending}>
                    Отменить аукцион
                  </Button>
                  {cancelResult && (
                    <Alert severity="info" variant="outlined" sx={{ fontSize: '0.72rem' }}>
                      Статус: {cancelResult.status ?? 'возврат'} • Комиссия: {cancelResult.penalty ?? 0}
                    </Alert>
                  )}
                </Stack>

                <Grid container spacing={2}>
                  <Grid item xs={12} md={4}>
                    <TextField
                      label="Минуты продления"
                      type="number"
                      value={extendForm.requestedMinutes}
                      size="small"
                      onChange={(e) => setExtendForm((prev) => ({ ...prev, requestedMinutes: Number(e.target.value) || 0 }))}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={4}>
                    <TextField
                      label="Режим"
                      select
                      value={extendForm.mode}
                      size="small"
                      onChange={(e) => setExtendForm((prev) => ({ ...prev, mode: e.target.value as ExtendFormState['mode'] }))}
                      fullWidth
                    >
                      {Object.values(ExtendRequestMode).map((mode) => (
                        <MenuItem key={mode} value={mode}>
                          {mode}
                        </MenuItem>
                      ))}
                    </TextField>
                  </Grid>
                  <Grid item xs={12} md={4}>
                    <TextField
                      label="Источник"
                      select
                      value={extendForm.triggerSource}
                      size="small"
                      onChange={(e) =>
                        setExtendForm((prev) => ({ ...prev, triggerSource: e.target.value as ExtendFormState['triggerSource'] }))
                      }
                      fullWidth
                    >
                      {Object.values(ExtendRequestTriggerSource).map((source) => (
                        <MenuItem key={source} value={source}>
                          {source}
                        </MenuItem>
                      ))}
                    </TextField>
                  </Grid>
                </Grid>
                <Stack direction="row" spacing={1}>
                  <Button variant="outlined" size="small" onClick={handleExtend} disabled={extendMutation.isPending}>
                    Продлить таймер
                  </Button>
                  {extendResult && (
                    <Alert severity="success" variant="outlined" sx={{ fontSize: '0.72rem' }}>
                      Новая дата: {extendResult.expiresAt ?? '—'} • Причина: {extendResult.reason ?? 'manual'}
                    </Alert>
                  )}
                </Stack>
              </Stack>

              <Divider />

              <Stack spacing={1.5}>
                <Typography variant="subtitle2" fontWeight={600} fontSize="0.9rem">
                  3. Планировщик: завершение истёкших аукционов
                </Typography>
                <Grid container spacing={2}>
                  <Grid item xs={12} md={6}>
                    <TextField
                      label="Shard IDs"
                      helperText="Через запятую"
                      value={processForm.shardIds}
                      size="small"
                      onChange={(e) => setProcessForm((prev) => ({ ...prev, shardIds: e.target.value }))}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={3}>
                    <TextField
                      label="Окно (мин)"
                      type="number"
                      value={processForm.windowMinutes}
                      size="small"
                      onChange={(e) => setProcessForm((prev) => ({ ...prev, windowMinutes: Number(e.target.value) || 0 }))}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} md={3}>
                    <TextField
                      label="Лимит"
                      type="number"
                      value={processForm.limit}
                      size="small"
                      onChange={(e) => setProcessForm((prev) => ({ ...prev, limit: Number(e.target.value) || 0 }))}
                      fullWidth
                    />
                  </Grid>
                </Grid>
                <Stack direction="row" spacing={1}>
                  <Button variant="contained" size="small" color="secondary" onClick={handleProcess} disabled={processMutation.isPending}>
                    Обработать истёкшие лоты
                  </Button>
                  {processResult && (
                    <Alert severity="info" variant="outlined" sx={{ fontSize: '0.72rem' }}>
                      Завершено: {processResult.processedCount ?? 0} • Перезапущено: {processResult.rescheduled ?? 0}
                    </Alert>
                  )}
                </Stack>
              </Stack>
            </Stack>
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default AuctionHousePage

