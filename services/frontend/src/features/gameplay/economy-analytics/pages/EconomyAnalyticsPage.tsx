/**
 * EconomyAnalyticsPage - экономическая аналитика и графики
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
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
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import BarChartIcon from '@mui/icons-material/BarChart'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { PriceChartCard } from '../components/PriceChartCard'
import { TechnicalIndicatorsCard } from '../components/TechnicalIndicatorsCard'
import { MarketSentimentCard } from '../components/MarketSentimentCard'
import { HeatMapCard } from '../components/HeatMapCard'
import { PortfolioAnalyticsCard } from '../components/PortfolioAnalyticsCard'
import { TradeHistoryCard } from '../components/TradeHistoryCard'
import { AlertsCard } from '../components/AlertsCard'
import { useGameState } from '@/features/game/hooks/useGameState'

const CHART_TYPES = ['line', 'candlestick', 'ohlc', 'volume']
const TIMEFRAMES = ['1h', '4h', '1d', '1w', '1m']

const demoChart = {
  item_id: 'legendary_weapon_001',
  chart_type: 'candlestick',
  timeframe: '1d',
  data_points: [
    { timestamp: '2077-11-06T00:00:00Z', open: 4500, high: 4800, low: 4400, close: 4700, volume: 125 },
    { timestamp: '2077-11-07T00:00:00Z', open: 4700, high: 5050, low: 4650, close: 4980, volume: 162 },
  ],
}

const demoIndicators = [
  { label: 'MA(20)', value: 4820, signal: 'bullish' },
  { label: 'EMA(50)', value: 4685, signal: 'bullish' },
  { label: 'RSI(14)', value: 68.5, signal: 'overbought' },
  { label: 'MACD', value: '+2.2', signal: 'bullish' },
]

const demoHeatMapItems = [
  { itemName: 'Neon Katana', priceChangePercent: 12, volumeChangePercent: 24 },
  { itemName: 'Cyberdeck Mk.IV', priceChangePercent: 8, volumeChangePercent: 18 },
  { itemName: 'Plasma Pistol', priceChangePercent: -5, volumeChangePercent: -12 },
  { itemName: 'Nano Armor', priceChangePercent: 15, volumeChangePercent: 30 },
]

const demoPortfolio = {
  characterId: 'char_123',
  totalValue: 520000,
  totalInvested: 410000,
  profitLoss: 110000,
  roiPercent: 26.8,
  diversification: { weapons: 40, implants: 28, resources: 22, stocks: 10 },
  topPerformers: [
    { itemName: 'Legendary Rifle', roi: 155 },
    { itemName: 'Rare Implant', roi: 92 },
  ],
}

const demoTradeHistory = {
  statistics: { totalTrades: 112, winRate: 64.5, averageProfit: 5400 },
  trades: [
    { tradeId: 'trade-001', result: 'win', profit: 1800, timestamp: '2077-11-07 15:40' },
    { tradeId: 'trade-002', result: 'loss', profit: -650, timestamp: '2077-11-07 13:20' },
  ],
}

const demoAlerts = [
  { alertId: 'alert-01', itemName: 'Smart Rifle', alertType: 'price_above', targetPrice: 5200, notificationMethod: 'in_game' },
  { alertId: 'alert-02', itemName: 'Nano Armor', alertType: 'volume_spike', targetPrice: 0, notificationMethod: 'both' },
]

export const EconomyAnalyticsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [chartType, setChartType] = useState('candlestick')
  const [timeframe, setTimeframe] = useState('1d')

  const chartData = {
    ...demoChart,
    chart_type: chartType,
    timeframe,
  }

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
      {selectedCharacterId && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Character: {selectedCharacterId}
        </Typography>
      )}
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info">
        Economy Analytics
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        TradingView / Bloomberg style
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Параметры графика
      </Typography>
      <TextField
        size="small"
        label="Item ID"
        defaultValue="legendary_weapon_001"
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
        inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs } }}
      />
      <TextField
        select
        size="small"
        label="Тип графика"
        value={chartType}
        onChange={(event) => setChartType(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
        SelectProps={{ MenuProps: { PaperProps: { sx: { maxHeight: 240 } } } }}
      >
        {CHART_TYPES.map((type) => (
          <MenuItem key={type} value={type} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {type.toUpperCase()}
          </MenuItem>
        ))}
      </TextField>
      <Stack spacing={0.5}>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Таймфрейм
        </Typography>
        <ToggleButtonGroup
          exclusive
          size="small"
          value={timeframe}
          onChange={(_, value) => value && setTimeframe(value)}
          sx={{ '& .MuiToggleButton-root': { fontSize: cyberpunkTokens.fonts.xs, px: 1.1 } }}
        >
          {TIMEFRAMES.map((tf) => (
            <ToggleButton key={tf} value={tf}>
              {tf}
            </ToggleButton>
          ))}
        </ToggleButtonGroup>
      </Stack>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Обновить график
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <AlertsCard alerts={demoAlerts} />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Советы
      </Typography>
      <Stack spacing={0.3}>
        {[
          'MA/EMA + RSI = быстрый сигнал входа',
          'Heat Map показывает ТОП роста и падения',
          'ROI > 20% → рассмотреть фиксацию прибыли',
          'Alerts уведомят о пробоях уровней',
        ].map((tip) => (
          <Typography key={tip} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {tip}
          </Typography>
        ))}
      </Stack>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Создать alert
      </CyberpunkButton>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <BarChartIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Economy Analytics
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        TradingView / Bloomberg стиль: графики (line, candlestick, OHLC, volume), индикаторы (MA/EMA/RSI/MACD/Bollinger), sentiment, портфолио и alerts.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <PriceChartCard chart={chartData} />
        </Grid>
        <Grid item xs={12} md={6}>
          <TechnicalIndicatorsCard timeframe={timeframe} indicators={demoIndicators} />
        </Grid>
        <Grid item xs={12} md={6}>
          <MarketSentimentCard
            market="weapons"
            bullBearRatio={0.62}
            volumeTrend="rising"
            momentum="bullish"
            notes={['Высокий спрос на оружие', 'Объемы +18% неделя к предыдущей']}
          />
        </Grid>
        <Grid item xs={12} md={6}>
          <HeatMapCard category="weapons" timeframe={timeframe} items={demoHeatMapItems} />
        </Grid>
        <Grid item xs={12} md={6}>
          <PortfolioAnalyticsCard {...demoPortfolio} />
        </Grid>
        <Grid item xs={12} md={6}>
          <TradeHistoryCard statistics={demoTradeHistory.statistics} trades={demoTradeHistory.trades} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default EconomyAnalyticsPage

