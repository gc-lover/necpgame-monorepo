/**
 * PlayerMarketExecutionPage - управление исполнением ордеров
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
  ToggleButtonGroup,
  ToggleButton,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import BoltIcon from '@mui/icons-material/Bolt';
import SyncAltIcon from '@mui/icons-material/SyncAlt';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { ExecutionResultCard } from '../components/ExecutionResultCard';
import { TradeDetailsCard } from '../components/TradeDetailsCard';
import { useGameState } from '@/features/game/hooks/useGameState';

const demoExecutions = [
  {
    orderId: 'PM-EXEC-01',
    status: 'filled' as const,
    filledQuantity: 12,
    remainingQuantity: 0,
    averagePrice: 4950,
    totalCost: 59400,
    commission: 297,
    trades: [
      { tradeId: 'TRADE-101', price: 5000, quantity: 7, executedAt: '2077-11-07 16:15' },
      { tradeId: 'TRADE-102', price: 4900, quantity: 5, executedAt: '2077-11-07 16:15' },
    ],
  },
  {
    orderId: 'PM-EXEC-02',
    status: 'partially_filled' as const,
    filledQuantity: 6,
    remainingQuantity: 4,
    averagePrice: 5300,
    totalCost: 31800,
    commission: 159,
    trades: [
      { tradeId: 'TRADE-103', price: 5350, quantity: 3, executedAt: '2077-11-07 15:54' },
      { tradeId: 'TRADE-104', price: 5250, quantity: 3, executedAt: '2077-11-07 15:55' },
    ],
  },
];

const demoTrades = [
  {
    tradeId: 'TRADE-104',
    buyOrderId: 'BUY-501',
    sellOrderId: 'SELL-998',
    itemId: 'legendary_smart_rifle',
    price: 5250,
    quantity: 3,
    executedAt: '2077-11-07 15:55',
    buyerId: 'ChromeFox',
    sellerId: 'NightDealer',
  },
  {
    tradeId: 'TRADE-105',
    buyOrderId: 'BUY-777',
    sellOrderId: 'SELL-333',
    itemId: 'cyberdeck_mk3',
    price: 7100,
    quantity: 2,
    executedAt: '2077-11-07 15:48',
    buyerId: 'Netrunner04',
    sellerId: 'FixerFox',
  },
];

export const PlayerMarketExecutionPage: React.FC = () => {
  const navigate = useNavigate();
  const { selectedCharacterId } = useGameState();
  const [executionType, setExecutionType] = React.useState<'market' | 'limit'>('market');

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
        Execution Control
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Исполнение ордеров игрока
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Исполнить ордер
      </Typography>
      <ToggleButtonGroup
        value={executionType}
        exclusive
        size="small"
        onChange={(_, value) => value && setExecutionType(value)}
        sx={{ '& .MuiToggleButton-root': { fontSize: cyberpunkTokens.fonts.xs, px: 1.2 } }}
      >
        <ToggleButton value="market">Market</ToggleButton>
        <ToggleButton value="limit">Limit</ToggleButton>
      </ToggleButtonGroup>
      <Stack spacing={1}>
        <TextField
          size="small"
          label="Order ID"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs } }}
        />
        <CyberpunkButton variant="primary" size="small" fullWidth startIcon={<BoltIcon />}>
          Исполнить {executionType}
        </CyberpunkButton>
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Сопоставление ордеров
      </Typography>
      <Stack spacing={1}>
        <TextField
          size="small"
          label="Item ID"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs } }}
        />
        <CyberpunkButton variant="secondary" size="small" fullWidth startIcon={<SyncAltIcon />}>
          Match Orders
        </CyberpunkButton>
      </Stack>
    </Stack>
  );

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Метрики исполнения
      </Typography>
      <Divider />
      <Stack spacing={0.4}>
        {[
          'Price/Time priority: лучшая цена → время заявки',
          'Partial fills активны по умолчанию',
          'Комиссия: 0.5% listing + 1.5% execution',
          'Auto-match запускается каждые 30 секунд',
        ].map((item) => (
          <Typography key={item} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {item}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Последние трейды
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Выберите трейд из списка или выполните запрос /execution/{'{trade_id}'}
      </Typography>
    </Stack>
  );

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <BoltIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Player Market Execution
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Исполняем market и limit ордера, управляем автоматическим сопоставлением и отслеживаем сделки.
        Вдохновение: EVE Online (market ticker), Albion Online (trade log), NYSE matching engine.
      </Alert>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Последние исполнения
      </Typography>
      <Grid container spacing={1}>
        {demoExecutions.map((execution) => (
          <Grid item xs={12} md={6} key={execution.orderId}>
            <ExecutionResultCard result={execution} />
          </Grid>
        ))}
      </Grid>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Детали сделок
      </Typography>
      <Grid container spacing={1}>
        {demoTrades.map((trade) => (
          <Grid item xs={12} md={6} key={trade.tradeId}>
            <TradeDetailsCard trade={trade} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default PlayerMarketExecutionPage;



