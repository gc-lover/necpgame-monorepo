/**
 * PlayerMarketOrdersPage - расширенная система ордеров рынка игроков
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Typography,
  Stack,
  Divider,
  Alert,
  Grid,
  Box,
  TextField,
  MenuItem,
  ToggleButtonGroup,
  ToggleButton,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong';
import FilterAltIcon from '@mui/icons-material/FilterAlt';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { MarketOrderCard } from '../components/MarketOrderCard';
import { OrderHistoryCard } from '../components/OrderHistoryCard';
import { useGameState } from '@/features/game/hooks/useGameState';

const demoActiveOrders = [
  {
    orderId: 'PM-1024',
    character: 'ChromeFox',
    itemName: 'Legendary Smart Rifle',
    side: 'buy' as const,
    orderType: 'limit' as const,
    status: 'pending' as const,
    limitPrice: 6200,
    quantity: 10,
    filledQuantity: 3,
    timeInForce: 'GTC' as const,
  },
  {
    orderId: 'PM-998',
    character: 'NeonWitch',
    itemName: 'Cyberdeck Mk.V',
    side: 'sell' as const,
    orderType: 'market' as const,
    status: 'partially_filled' as const,
    price: 14800,
    quantity: 4,
    filledQuantity: 2,
    timeInForce: 'IOC' as const,
  },
];

const demoHistory = [
  {
    orderId: 'PM-870',
    itemName: 'Implant Reflex+2',
    side: 'sell' as const,
    orderType: 'limit' as const,
    executedPrice: 9800,
    quantity: 6,
    filledAt: '2077-11-03 18:42',
    pnl: 1200,
    fees: 150,
  },
  {
    orderId: 'PM-812',
    itemName: 'Nanofiber Armor',
    side: 'buy' as const,
    orderType: 'market' as const,
    executedPrice: 13400,
    quantity: 2,
    filledAt: '2077-11-02 09:17',
    pnl: -600,
    fees: 90,
  },
];

export const PlayerMarketOrdersPage: React.FC = () => {
  const navigate = useNavigate();
  const { selectedCharacterId } = useGameState();
  const [filterSide, setFilterSide] = React.useState<'all' | 'buy' | 'sell'>('all');

  const leftPanel = (
    <Stack spacing={2}>
      <CyberpunkButton
        variant="outlined"
        size="small"
        fullWidth
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/game')}
      >
        Назад
      </CyberpunkButton>
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info">
        Player Market Orders
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Управление ордерами
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрое создание
      </Typography>
      <Stack spacing={1}>
        <TextField
          size="small"
          label="Item ID"
          variant="outlined"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs } }}
        />
        <TextField
          size="small"
          label="Quantity"
          type="number"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs } }}
        />
        <TextField
          size="small"
          select
          label="Order type"
          defaultValue="market"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
        >
          {[
            { value: 'market', label: 'Market' },
            { value: 'limit', label: 'Limit' },
          ].map((option) => (
            <MenuItem key={option.value} value={option.value} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
              {option.label}
            </MenuItem>
          ))}
        </TextField>
        <CyberpunkButton variant="primary" size="small" fullWidth>
          Создать ордер
        </CyberpunkButton>
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Time in force
      </Typography>
      <Stack spacing={0.3}>
        {['GTC - Good Till Cancel', 'IOC - Immediate Or Cancel', 'FOK - Fill Or Kill'].map((line) => (
          <Typography key={line} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {line}
          </Typography>
        ))}
      </Stack>
    </Stack>
  );

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Метрики ордеров
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {[
          'Время реакции рынка: < 5 сек',
          'Средняя комиссия: 2.3%',
          'Частичное исполнение поддерживается',
          'Авто рефанд валюты/предметов',
        ].map((metric) => (
          <Typography key={metric} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {metric}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтр
      </Typography>
      <ToggleButtonGroup
        value={filterSide}
        exclusive
        color="info"
        size="small"
        onChange={(_, value) => {
          if (value) setFilterSide(value);
        }}
        sx={{ '& .MuiToggleButton-root': { fontSize: cyberpunkTokens.fonts.xs, px: 1.2 } }}
      >
        <ToggleButton value="all">ALL</ToggleButton>
        <ToggleButton value="buy">BUY</ToggleButton>
        <ToggleButton value="sell">SELL</ToggleButton>
      </ToggleButtonGroup>
    </Stack>
  );

  const filteredOrders = demoActiveOrders.filter((order) => filterSide === 'all' || order.side === filterSide);

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <ReceiptLongIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Player Market Orders
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }} icon={<FilterAltIcon fontSize="small" />}>
        Управляй ордерами: создание, отмена, активные заявки и история исполнения. Вдохновлено рынками EVE Online и Albion.
      </Alert>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Активные ордера ({filteredOrders.length})
      </Typography>
      <Grid container spacing={1}>
        {filteredOrders.map((order) => (
          <Grid item xs={12} md={6} key={order.orderId}>
            <MarketOrderCard order={order} onCancel={() => {}} />
          </Grid>
        ))}
        {filteredOrders.length === 0 && (
          <Grid item xs={12}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              Нет ордеров для выбранного фильтра.
            </Typography>
          </Grid>
        )}
      </Grid>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        История ордеров ({demoHistory.length})
      </Typography>
      <Grid container spacing={1}>
        {demoHistory.map((order) => (
          <Grid item xs={12} md={6} key={order.orderId}>
            <OrderHistoryCard order={order} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default PlayerMarketOrdersPage;


