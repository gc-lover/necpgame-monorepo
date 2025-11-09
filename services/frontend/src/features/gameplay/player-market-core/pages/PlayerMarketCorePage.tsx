/**
 * PlayerMarketCorePage - страница рынка игроков (core)
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
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ImportExportIcon from '@mui/icons-material/ImportExport';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { OrderBookCard } from '../components/OrderBookCard';
import { PlayerOrderCard } from '../components/PlayerOrderCard';
import { useGameState } from '@/features/game/hooks/useGameState';

const orderTypes = ['MARKET', 'LIMIT'];
const sides = ['BUY', 'SELL'];

export const PlayerMarketCorePage: React.FC = () => {
  const navigate = useNavigate();
  const { selectedCharacterId } = useGameState();

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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="warning">
        Player Market
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Биржа игроков
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Новый ордер
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
          select
          label="Type"
          defaultValue="MARKET"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
        >
          {orderTypes.map((type) => (
            <MenuItem key={type} value={type} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
              {type}
            </MenuItem>
          ))}
        </TextField>
        <TextField
          size="small"
          select
          label="Side"
          defaultValue="BUY"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
        >
          {sides.map((side) => (
            <MenuItem key={side} value={side} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
              {side}
            </MenuItem>
          ))}
        </TextField>
        <TextField
          size="small"
          label="Price"
          type="number"
          variant="outlined"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs } }}
        />
        <TextField
          size="small"
          label="Quantity"
          type="number"
          variant="outlined"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs } }}
        />
        <CyberpunkButton variant="primary" size="small" fullWidth startIcon={<ImportExportIcon />}>
          Создать ордер
        </CyberpunkButton>
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Комиссии
      </Typography>
      <Stack spacing={0.3}>
        {['Listing fee 0.5%', 'Exchange fee 2%', 'Priority: price/time'].map((line) => (
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
        Подсказки
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {[
          'Market order заполняет стакан мгновенно.',
          'Limit order ждёт цену, можно отменить в любой момент.',
          'Price/Time priority: быстрее ставить ордер выше в очереди.',
        ].map((tip) => (
          <Typography key={tip} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {tip}
          </Typography>
        ))}
      </Stack>
    </Stack>
  );

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <ImportExportIcon sx={{ fontSize: '1.5rem', color: 'warning.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Player Market Core
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Стакан заявок, создание ордеров, просмотр своих ордеров. Market/Limit, buy/sell, price/time
        priority, комиссии 0.5-5%.
      </Alert>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Стакан заявок
      </Typography>
      <Grid container spacing={1}>
        {[].map((orderBook, index) => (
          <Grid item xs={12} md={6} key={index}>
            <OrderBookCard
              itemName={orderBook.item_name}
              spread={orderBook.spread}
              lastTradePrice={orderBook.last_trade_price}
              buyOrders={orderBook.buy_orders}
              sellOrders={orderBook.sell_orders}
            />
          </Grid>
        ))}
      </Grid>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Мои ордера
      </Typography>
      <Grid container spacing={1}>
        {[].map((order, index) => (
          <Grid item xs={12} md={6} key={index}>
            <PlayerOrderCard order={order} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default PlayerMarketCorePage;


