/**
 * Страница торговли
 * 
 * SPA архитектура с компактной 3-колоночной сеткой:
 * - Левая панель: Список торговцев
 * - Центр: Ассортимент выбранного торговца
 * - Правая панель: Информация о деньгах игрока
 */
import { useEffect, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import {
  Box,
  CircularProgress,
  Alert,
  Typography,
  Grid,
  Paper,
  Stack,
  Tabs,
  Tab,
  Chip,
} from '@mui/material'
import {
  useGetVendors,
  useGetVendorInventory,
  useBuyItem,
  useSellItem,
} from '@/api/generated/trading/trading/trading'
import { useGetInventory } from '@/api/generated/inventory/inventory/inventory'
import { VendorCard, TradeItemCard, SellItemCard } from '../components'
import { useGameState } from '@/features/game/hooks/useGameState'
import { Header } from '@/shared/components/layout/Header'
import { GameLayout, StatsPanel } from '@/shared/ui/layout'
import type { Vendor } from '@/api/generated/trading/models'

export function TradingPage() {
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const selectedCharacterId = useGameState((state) => state.selectedCharacterId)
  const characterState = useGameState((state) => state.characterState)
  const [selectedVendor, setSelectedVendor] = useState<Vendor | null>(null)
  const [tabValue, setTabValue] = useState(0) // 0 = купить, 1 = продать

  // Загрузка списка торговцев из OpenAPI
  const {
    data: vendorsData,
    isLoading: isLoadingVendors,
    error: vendorsError,
  } = useGetVendors(
    { characterId: selectedCharacterId || '' },
    { query: { enabled: !!selectedCharacterId } }
  )

  // Загрузка ассортимента торговца из OpenAPI
  const {
    data: vendorInventory,
    isLoading: isLoadingInventory,
    refetch: refetchVendorInventory,
  } = useGetVendorInventory(
    selectedVendor?.id ?? '',
    { characterId: selectedCharacterId ?? '' },
    { query: { enabled: Boolean(selectedVendor && selectedCharacterId) } }
  )

  // Загрузка инвентаря игрока для продажи
  const {
    data: playerInventory,
    isLoading: isLoadingPlayerInventory,
    refetch: refetchPlayerInventory,
  } = useGetInventory(
    { characterId: selectedCharacterId || '' },
    { query: { enabled: !!selectedCharacterId && tabValue === 1 } }
  )

  // Mutations
  const { mutate: buyItem, isPending: isBuying } = useBuyItem()
  const { mutate: sellItem, isPending: isSelling } = useSellItem()

  useEffect(() => {
    if (!selectedCharacterId) {
      navigate('/characters')
    }
  }, [selectedCharacterId, navigate])

  useEffect(() => {
    if (!vendorsData?.vendors || vendorsData.vendors.length === 0) {
      return
    }

    const vendorId = searchParams.get('vendorId')
    if (vendorId) {
      const vendor = vendorsData.vendors.find((v) => v.id === vendorId)
      if (vendor) {
        setSelectedVendor(vendor)
        return
      }
    }

    if (!selectedVendor) {
      setSelectedVendor(vendorsData.vendors[0])
    }
  }, [searchParams, vendorsData, selectedVendor])

  const handleBuy = (itemId: string) => {
    if (!selectedCharacterId || !selectedVendor) return
    console.log('Buy item:', itemId)
    buyItem(
      { data: { characterId: selectedCharacterId, vendorId: selectedVendor.id, itemId, quantity: 1 } },
      {
        onSuccess: (result) => {
          console.log('Purchase successful:', result)
          refetchVendorInventory()
          if (tabValue === 1) {
            refetchPlayerInventory()
          }
        },
        onError: (err) => console.error('Purchase error:', err),
      }
    )
  }

  const handleSell = (itemId: string) => {
    if (!selectedCharacterId || !selectedVendor) return
    console.log('Sell item:', itemId)
    sellItem(
      { data: { characterId: selectedCharacterId, vendorId: selectedVendor.id, itemId, quantity: 1 } },
      {
        onSuccess: (result) => {
          console.log('Sale successful:', result)
          refetchVendorInventory()
          refetchPlayerInventory()
        },
        onError: (err) => console.error('Sale error:', err),
      }
    )
  }

  // Левая панель - Список торговцев
  const leftPanel = (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, height: '100%', minHeight: 0 }}>
      <Typography
        variant="h6"
        sx={{
          color: 'primary.main',
          textShadow: '0 0 8px currentColor',
          fontWeight: 'bold',
          fontSize: '0.875rem',
          textTransform: 'uppercase',
          letterSpacing: '0.1em',
        }}
      >
        Торговцы
      </Typography>

      <Stack spacing={1} sx={{ overflowY: 'auto', flex: 1 }}>
        {isLoadingVendors ? (
          <CircularProgress size={30} />
        ) : vendorsData?.vendors && vendorsData.vendors.length > 0 ? (
          vendorsData.vendors.map((vendor) => (
            <VendorCard
              key={vendor.id}
              vendor={vendor}
              onClick={() => setSelectedVendor(vendor)}
            />
          ))
        ) : (
          <Typography variant="caption" sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
            Нет доступных торговцев
          </Typography>
        )}
      </Stack>
    </Box>
  )

  // Правая панель - Деньги игрока
  const rightPanel = (
    <StatsPanel>
      <Paper elevation={2} sx={{ p: 2 }}>
        <Typography
          variant="subtitle2"
          sx={{
            color: 'success.main',
            fontSize: '0.75rem',
            textTransform: 'uppercase',
            mb: 1,
          }}
        >
          Деньги
        </Typography>
        <Stack spacing={1}>
          <Stack direction="row" justifyContent="space-between" alignItems="center">
            <Typography variant="caption" sx={{ fontSize: '0.7rem' }}>Eddies:</Typography>
            <Chip
              label={`${characterState?.money || 0}€$`}
              size="small"
              color="success"
              sx={{ fontSize: '0.75rem', fontWeight: 'bold' }}
            />
          </Stack>
        </Stack>
      </Paper>

      {selectedVendor && (
        <Paper elevation={2} sx={{ p: 2, mt: 2 }}>
          <Typography variant="subtitle2" sx={{ fontSize: '0.75rem', mb: 1 }}>
            Торговец:
          </Typography>
          <Typography variant="body2" sx={{ fontSize: '0.8rem', fontWeight: 'bold' }}>
            {selectedVendor.name}
          </Typography>
          {selectedVendor.specialization && (
            <Chip
              label={selectedVendor.specialization}
              size="small"
              color="secondary"
              sx={{ fontSize: '0.65rem', mt: 1 }}
            />
          )}
        </Paper>
      )}

      {vendorInventory?.nextRefresh && (
        <Paper elevation={2} sx={{ p: 2, mt: 2 }}>
          <Typography variant="subtitle2" sx={{ fontSize: '0.75rem', mb: 1 }}>
            Обновление ассортимента
          </Typography>
          <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
            {new Date(vendorInventory.nextRefresh).toLocaleString()}
          </Typography>
        </Paper>
      )}
    </StatsPanel>
  )

  if (isLoadingVendors) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1 }}>
          <CircularProgress size={60} />
        </Box>
      </Box>
    )
  }

  if (vendorsError) {
    return (
      <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
        <Header />
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flex: 1, p: 3 }}>
          <Alert severity="error" sx={{ maxWidth: 600 }}>
            <Typography variant="h6">Ошибка загрузки торговцев</Typography>
            <Typography variant="body2">
              {(vendorsError as unknown as Error)?.message || 'Не удалось загрузить список торговцев'}
            </Typography>
          </Alert>
        </Box>
      </Box>
    )
  }

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100vh', overflow: 'hidden' }}>
      <Header />
      <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>
        <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 2, minHeight: 0, overflowY: 'auto', p: 2 }}>
          <Typography variant="h5" sx={{ color: 'primary.main', fontWeight: 'bold', fontSize: '1.1rem' }}>
            Торговля
          </Typography>

          {selectedVendor ? (
            <>
              <Tabs value={tabValue} onChange={(_, v) => setTabValue(v)} sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tab label="Купить" sx={{ fontSize: '0.8rem', minHeight: 40 }} />
                <Tab label="Продать" sx={{ fontSize: '0.8rem', minHeight: 40 }} />
              </Tabs>

              {isLoadingInventory ? (
                <CircularProgress size={40} />
              ) : tabValue === 0 ? (
                // Покупка - ассортимент торговца
                <Grid container spacing={1.5}>
                  {vendorInventory?.items && vendorInventory.items.length > 0 ? (
                    vendorInventory.items.map((item) => (
                      <Grid item xs={12} sm={6} md={4} key={item.itemId}>
                        <TradeItemCard
                          item={item}
                          mode="buy"
                          onTrade={() => handleBuy(item.itemId)}
                          disabled={isBuying}
                        />
                      </Grid>
                    ))
                  ) : (
                    <Grid item xs={12}>
                      <Typography variant="caption" sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
                        Нет товаров в ассортименте
                      </Typography>
                    </Grid>
                  )}
                </Grid>
              ) : isLoadingPlayerInventory ? (
                <CircularProgress size={40} />
              ) : (
                // Продажа - инвентарь игрока
                <Grid container spacing={1.5}>
                  {playerInventory?.items && playerInventory.items.length > 0 ? (
                    playerInventory.items
                      .filter(item => !item.questItem) // Не продаем квестовые предметы
                      .map((item) => (
                        <Grid item xs={12} sm={6} md={4} key={item.id}>
                          <SellItemCard
                            vendorId={selectedVendor.id}
                            characterId={selectedCharacterId || ''}
                            item={item}
                            onSell={() => handleSell(item.id)}
                            disabled={isSelling}
                          />
                        </Grid>
                      ))
                  ) : (
                    <Grid item xs={12}>
                      <Typography variant="caption" sx={{ fontSize: '0.75rem', color: 'text.secondary' }}>
                        Нет предметов для продажи
                      </Typography>
                    </Grid>
                  )}
                </Grid>
              )}
            </>
          ) : (
            <Paper elevation={1} sx={{ p: 3, textAlign: 'center' }}>
              <Typography variant="body2" sx={{ fontSize: '0.85rem', color: 'text.secondary' }}>
                Выберите торговца из списка слева
              </Typography>
            </Paper>
          )}
        </Box>
      </GameLayout>
    </Box>
  )
}

