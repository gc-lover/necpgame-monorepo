import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Button, Stack, Divider, Alert, TextField, MenuItem, Grid, Box } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import AddIcon from '@mui/icons-material/Add'
import GameLayout from '@/features/game/components/GameLayout'
import { OrderBookDisplay } from '../components/OrderBookDisplay'
import { useGameState } from '@/features/game/hooks/useGameState'
import {
  useGetOrderBook,
  useCreateOrder,
  useGetMyOrders,
  useCancelOrder,
} from '@/api/generated/player-market/player-market/player-market'

export const PlayerMarketPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [selectedItemId, setSelectedItemId] = useState('rare_weapon_001')
  const [orderType, setOrderType] = useState<'market' | 'limit'>('limit')
  const [side, setSide] = useState<'buy' | 'sell'>('buy')
  const [quantity, setQuantity] = useState(1)
  const [price, setPrice] = useState(1000)

  const { data: orderBookData } = useGetOrderBook({ item_id: selectedItemId, limit: 20 }, { query: { enabled: !!selectedItemId } })

  const { data: myOrdersData } = useGetMyOrders({ character_id: selectedCharacterId || '' }, { query: { enabled: !!selectedCharacterId } })

  const createOrderMutation = useCreateOrder()
  const cancelOrderMutation = useCancelOrder()

  const handleCreateOrder = () => {
    if (!selectedCharacterId) return
    createOrderMutation.mutate({
      data: {
        character_id: selectedCharacterId,
        order_type: orderType,
        side,
        item_id: selectedItemId,
        quantity,
        price: orderType === 'limit' ? price : undefined,
      },
    })
  }

  const leftPanel = (
    <Stack spacing={2}>
      <Button startIcon={<ArrowBackIcon />} onClick={() => navigate('/game')} fullWidth variant="outlined" size="small" sx={{ fontSize: '0.75rem' }}>
        Назад
      </Button>
      <Typography variant="h6" fontSize="1rem" fontWeight="bold" color="primary">
        Player Market
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        EVE Online / GW2
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Создать ордер
      </Typography>
      <TextField select label="Order Type" value={orderType} onChange={(e) => setOrderType(e.target.value as 'market' | 'limit')} size="small" fullWidth>
        <MenuItem value="market">Market (мгновенно)</MenuItem>
        <MenuItem value="limit">Limit (при цене)</MenuItem>
      </TextField>
      <TextField select label="Side" value={side} onChange={(e) => setSide(e.target.value as 'buy' | 'sell')} size="small" fullWidth>
        <MenuItem value="buy">Buy</MenuItem>
        <MenuItem value="sell">Sell</MenuItem>
      </TextField>
      <TextField label="Quantity" type="number" value={quantity} onChange={(e) => setQuantity(parseInt(e.target.value))} size="small" fullWidth />
      {orderType === 'limit' && <TextField label="Price" type="number" value={price} onChange={(e) => setPrice(parseFloat(e.target.value))} size="small" fullWidth />}
      <Button startIcon={<AddIcon />} onClick={handleCreateOrder} fullWidth variant="contained" size="small" disabled={createOrderMutation.isPending}>
        Создать ордер
      </Button>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Мои ордера
      </Typography>
      <Divider />
      <Typography variant="caption" fontSize="0.7rem">
        Активных: {myOrdersData?.orders?.filter((o) => o.status === 'active').length || 0}
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Исполнено: {myOrdersData?.orders?.filter((o) => o.status === 'filled').length || 0}
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
        Комиссии
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Listing: 0.5%
      </Typography>
      <Typography variant="caption" fontSize="0.7rem">
        Exchange: 2-5%
      </Typography>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Typography variant="h5" fontSize="1.25rem" fontWeight="bold">
        Рынок игроков (Player Market)
      </Typography>
      <Divider />
      <Alert severity="info" sx={{ fontSize: '0.75rem' }}>
        Система ордеров: Buy/Sell orders, Market/Limit orders. Order book (стакан), исполнение (частичное/полное). Комиссии: listing fee + exchange fee.
      </Alert>
      <Typography variant="body2" fontSize="0.75rem">
        <strong>Market orders:</strong> мгновенное исполнение по лучшей цене. <strong>Limit orders:</strong> исполнение при достижении цены.
      </Typography>
      {orderBookData && (
        <Box mt={2}>
          <OrderBookDisplay orderBook={orderBookData} />
        </Box>
      )}
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default PlayerMarketPage

