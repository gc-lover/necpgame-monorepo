import React from 'react'
import { Box, Typography, Stack, Divider, Chip } from '@mui/material'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import TrendingDownIcon from '@mui/icons-material/TrendingDown'
import type { OrderBook } from '@/api/generated/player-market/models'

interface OrderBookDisplayProps {
  orderBook: OrderBook
}

export const OrderBookDisplay: React.FC<OrderBookDisplayProps> = ({ orderBook }) => {
  return (
    <Box sx={{ border: '1px solid', borderColor: 'divider', borderRadius: 1, p: 1.5 }}>
      <Stack spacing={1}>
        <Typography variant="subtitle2" fontSize="0.875rem" fontWeight="bold">
          Order Book (Стакан заявок)
        </Typography>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
            Spread: €${orderBook.spread}
          </Typography>
          <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
            Last Trade: €${orderBook.last_trade_price}
          </Typography>
        </Box>
        <Divider />

        {/* Sell Orders (красные) */}
        <Box>
          <Box display="flex" alignItems="center" gap={0.5} mb={0.5}>
            <TrendingUpIcon sx={{ fontSize: '0.9rem', color: 'error.main' }} />
            <Typography variant="caption" fontSize="0.75rem" fontWeight="bold" color="error.main">
              Sell Orders
            </Typography>
          </Box>
          <Stack spacing={0.3}>
            {orderBook.sell_orders?.slice(0, 5).map((order, i) => (
              <Box key={i} display="flex" justifyContent="space-between" sx={{ bgcolor: 'rgba(244, 67, 54, 0.05)', p: 0.5, borderRadius: 0.5 }}>
                <Typography variant="caption" fontSize="0.7rem" fontWeight="bold" color="error.main">
                  €${order.price?.toLocaleString()}
                </Typography>
                <Typography variant="caption" fontSize="0.7rem">
                  x{order.quantity} ({order.total_orders} ордеров)
                </Typography>
              </Box>
            ))}
          </Stack>
        </Box>

        <Divider />

        {/* Buy Orders (зеленые) */}
        <Box>
          <Box display="flex" alignItems="center" gap={0.5} mb={0.5}>
            <TrendingDownIcon sx={{ fontSize: '0.9rem', color: 'success.main' }} />
            <Typography variant="caption" fontSize="0.75rem" fontWeight="bold" color="success.main">
              Buy Orders
            </Typography>
          </Box>
          <Stack spacing={0.3}>
            {orderBook.buy_orders?.slice(0, 5).map((order, i) => (
              <Box key={i} display="flex" justifyContent="space-between" sx={{ bgcolor: 'rgba(76, 175, 80, 0.05)', p: 0.5, borderRadius: 0.5 }}>
                <Typography variant="caption" fontSize="0.7rem" fontWeight="bold" color="success.main">
                  €${order.price?.toLocaleString()}
                </Typography>
                <Typography variant="caption" fontSize="0.7rem">
                  x{order.quantity} ({order.total_orders} ордеров)
                </Typography>
              </Box>
            ))}
          </Stack>
        </Box>
      </Stack>
    </Box>
  )
}

