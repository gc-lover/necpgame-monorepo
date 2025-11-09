/**
 * PricingPage - система ценообразования
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
import PriceCheckIcon from '@mui/icons-material/PriceCheck';
import CalculateIcon from '@mui/icons-material/Calculate';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { ItemPriceCard } from '../components/ItemPriceCard';
import { PriceBreakdownCard } from '../components/PriceBreakdownCard';
import { MarketDataCard } from '../components/MarketDataCard';
import { ModifiersCard } from '../components/ModifiersCard';
import { useGameState } from '@/features/game/hooks/useGameState';

const qualities = ['POOR', 'COMMON', 'UNCOMMON', 'RARE', 'EPIC', 'LEGENDARY'];

const demoItemPrice = {
  itemId: 'item-neon-001',
  itemName: 'Neon Katana',
  basePrice: 3400,
  currentPrice: 5200,
  vendorSellPrice: 5400,
  vendorBuyPrice: 3200,
};

const demoMultipliers = [
  { name: 'Quality', value: 1.2 },
  { name: 'Rarity', value: 1.6 },
  { name: 'Regional', value: 1.05 },
  { name: 'Faction', value: 0.95 },
];

const demoBreakdown = [
  { label: 'Base', value: 3400, type: 'base' as const },
  { label: 'Quality Bonus', value: 400, type: 'bonus' as const },
  { label: 'Rarity Bonus', value: 1200, type: 'bonus' as const },
  { label: 'Durability Penalty', value: -200, type: 'penalty' as const },
  { label: 'Regional Adjustment', value: 150, type: 'bonus' as const },
  { label: 'Faction Discount', value: -150, type: 'penalty' as const },
];

const demoMarketData = {
  category: 'Weapons',
  region: 'NeoTokyo',
  timestamp: '2077-11-07 16:58',
  averagePrices: {
    smart_rifle: 5500,
    neon_katana: 5200,
    plasma_pistol: 2800,
  },
  trendingUp: ['smart_rifle', 'neon_katana'],
  trendingDown: ['plasma_pistol'],
  highDemand: ['nano_armor'],
  lowSupply: ['shock_grenade'],
};

const demoModifiers = {
  region: 'NeoTokyo',
  regionalModifiers: { Downtown: 1.1, Industrial: 0.95 },
  factionModifiers: { Arasaka: 0.9, Militech: 1.05 },
  eventModifiers: [
    { name: 'Night Market', description: '+10% prices for rare gear', value: 1.1 },
    { name: 'Faction War', description: '-5% Arasaka vendor prices', value: 0.95 },
  ],
};

export const PricingPage: React.FC = () => {
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
        Pricing System
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Расчет цен, модификаторы, рынки
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Получить цену предмета
      </Typography>
      <Stack spacing={1}>
        <TextField
          size="small"
          label="Item ID"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs } }}
        />
        <TextField
          size="small"
          select
          label="Quality"
          defaultValue="RARE"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
        >
          {qualities.map((quality) => (
            <MenuItem key={quality} value={quality} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
              {quality}
            </MenuItem>
          ))}
        </TextField>
        <TextField
          size="small"
          label="Region"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs } }}
        />
        <TextField
          size="small"
          label="Vendor Faction"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs } }}
        />
        <CyberpunkButton variant="primary" size="small" fullWidth startIcon={<PriceCheckIcon />}>
          Получить цену
        </CyberpunkButton>
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Рассчитать цену
      </Typography>
      <Stack spacing={1}>
        <TextField
          size="small"
          label="Durability %"
          type="number"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs }, min: 0, max: 100 }}
        />
        <TextField
          size="small"
          label="Quantity"
          type="number"
          defaultValue={1}
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs }, min: 1 }}
        />
        <CyberpunkButton variant="secondary" size="small" fullWidth startIcon={<CalculateIcon />}>
          Рассчитать цену
        </CyberpunkButton>
      </Stack>
    </Stack>
  );

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Советы по ценообразованию
      </Typography>
      <Divider />
      <Stack spacing={0.3}>
        {[
          'Quality/Rarity → прямой мультипликатор базовой цены',
          'Faction reputation может давать скидку/надбавку',
          'Regional modifiers обновляются каждые 6 часов',
          'Dynamic pricing реагирует на supply/demand',
        ].map((tip) => (
          <Typography key={tip} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {tip}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Запросы API
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        /pricing/item/{'{item_id}'}, /pricing/calculate, /pricing/market-data, /pricing/modifiers
      </Typography>
    </Stack>
  );

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <PriceCheckIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Pricing System
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Управляй ценами: формулы, модификаторы, тренды рынка и история цен.
        Вдохновение: EVE Online Market Analytics, Final Fantasy XIV vendor pricing.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <ItemPriceCard item={demoItemPrice} multipliers={demoMultipliers} />
        </Grid>
        <Grid item xs={12} md={6}>
          <PriceBreakdownCard total={5200} quantityTotal={10400} breakdown={demoBreakdown} />
        </Grid>
      </Grid>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <MarketDataCard data={demoMarketData} />
        </Grid>
        <Grid item xs={12} md={6}>
          <ModifiersCard {...demoModifiers} />
        </Grid>
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default PricingPage;



