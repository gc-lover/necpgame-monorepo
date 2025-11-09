/**
 * Карточка предмета для торговли
 * Данные из OpenAPI: TradeItem
 */
import { Card, CardContent, Typography, Chip, Stack, Button, Box } from '@mui/material'
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart'
import SellIcon from '@mui/icons-material/Sell'
import type { TradeItem } from '@/api/generated/trading/models'

interface TradeItemCardProps {
  item: TradeItem
  mode: 'buy' | 'sell'
  onTrade: () => void
  disabled?: boolean
  loadingPrice?: boolean
}

export function TradeItemCard({ item, mode, onTrade, disabled, loadingPrice }: TradeItemCardProps) {
  const price = mode === 'buy' ? item.buyPrice : item.sellPrice
  const priceLabel = loadingPrice ? '...' : `${price}€$`
  const isActionDisabled = disabled || loadingPrice || (mode === 'buy' && item.quantity === 0)

  return (
    <Card
      sx={{
        border: '1px solid',
        borderColor: 'divider',
        '&:hover': {
          borderColor: 'primary.main',
          boxShadow: 1,
        },
      }}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={1}>
          <Stack direction="row" justifyContent="space-between" alignItems="flex-start">
            <Typography variant="subtitle2" sx={{ fontSize: '0.85rem', fontWeight: 'bold' }}>
              {item.name}
            </Typography>
            {item.quantity > 0 && (
              <Chip label={`x${item.quantity}`} size="small" sx={{ height: 16, fontSize: '0.6rem' }} />
            )}
          </Stack>

          {item.category && (
            <Typography variant="caption" sx={{ fontSize: '0.65rem', color: 'text.secondary' }}>
              {item.category}
            </Typography>
          )}

          <Stack direction="row" justifyContent="space-between" alignItems="center">
            <Box>
              <Typography variant="caption" sx={{ fontSize: '0.7rem', color: 'text.secondary' }}>
                {mode === 'buy' ? 'Цена покупки:' : 'Цена продажи:'}
              </Typography>
              <Typography variant="body2" sx={{ fontSize: '0.85rem', fontWeight: 'bold', color: 'success.main' }}>
                {priceLabel}
              </Typography>
            </Box>
            
            <Button
              variant="contained"
              size="small"
              color={mode === 'buy' ? 'primary' : 'success'}
              onClick={onTrade}
              disabled={isActionDisabled}
              startIcon={mode === 'buy' ? <ShoppingCartIcon sx={{ fontSize: '0.8rem' }} /> : <SellIcon sx={{ fontSize: '0.8rem' }} />}
              sx={{ fontSize: '0.7rem', py: 0.5, px: 1 }}
            >
              {mode === 'buy' ? 'Купить' : 'Продать'}
            </Button>
          </Stack>
        </Stack>
      </CardContent>
    </Card>
  )
}

