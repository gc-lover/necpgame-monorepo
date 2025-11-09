/**
 * AuctionHouseHistoryPage - страница истории цен аукциона
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
import TimelineIcon from '@mui/icons-material/Timeline';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { PriceHistoryCard } from '../components/PriceHistoryCard';
import { PriceTrendCard } from '../components/PriceTrendCard';
import { useGameState } from '@/features/game/hooks/useGameState';

const periodOptions = ['1d', '7d', '30d', '90d', '1y'];
const intervalOptions = ['1h', '4h', '1d'];

export const AuctionHouseHistoryPage: React.FC = () => {
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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info">
        История цен
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Аналитика аукциона
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Параметры
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
          label="Период"
          defaultValue="30d"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
        >
          {periodOptions.map((period) => (
            <MenuItem key={period} value={period} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
              {period}
            </MenuItem>
          ))}
        </TextField>
        <TextField
          size="small"
          select
          label="Интервал"
          defaultValue="1d"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
        >
          {intervalOptions.map((interval) => (
            <MenuItem key={interval} value={interval} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
              {interval}
            </MenuItem>
          ))}
        </TextField>
        <CyberpunkButton variant="primary" size="small" fullWidth startIcon={<TimelineIcon />}>
          Обновить
        </CyberpunkButton>
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрый выбор
      </Typography>
      <Stack spacing={0.3}>
        {['Legendary Katana', 'Cyberdeck Mk.III', 'Militech Railgun'].map((item) => (
          <Typography key={item} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {item}
          </Typography>
        ))}
      </Stack>
    </Stack>
  );

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Метрики
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['Средняя цена за период', 'Минимум/Максимум', 'Волатильность', 'Объемы торгов'].map(
          (metric) => (
            <Typography key={metric} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • {metric}
            </Typography>
          ),
        )}
      </Stack>
    </Stack>
  );

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <TimelineIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          История и статистика
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        История цен, статистика и тренды! Графики цен, средние значения, волатильность и объемы
        торгов по каждому предмету.
      </Alert>
      <Grid container spacing={1}>
        {[].map((history, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <PriceHistoryCard
              itemName={history.item_name}
              period={history.period}
              interval={history.interval}
              dataPoints={history.data_points}
            />
          </Grid>
        ))}
      </Grid>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Тренды
      </Typography>
      <Grid container spacing={1}>
        {[].map((trend, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <PriceTrendCard
              itemName={trend.item_name}
              trend={trend.trend}
              priceChange7d={trend.price_change_7d}
              priceChange30d={trend.price_change_30d}
              volatility={trend.volatility}
            />
          </Grid>
        ))}
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default AuctionHouseHistoryPage;


