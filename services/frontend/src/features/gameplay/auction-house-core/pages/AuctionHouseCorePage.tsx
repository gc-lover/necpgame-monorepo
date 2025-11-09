/**
 * AuctionHouseCorePage - страница аукцион дома
 * 
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Typography, Stack, Divider, Alert, Grid, Box } from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import GavelIcon from '@mui/icons-material/Gavel';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { AuctionLotCard } from '../components/AuctionLotCard';
import { useGameState } from '@/features/game/hooks/useGameState';

export const AuctionHouseCorePage: React.FC = () => {
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
        Auction House
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Аукцион дом
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Длительность лотов
      </Typography>
      <Stack spacing={0.3}>
        {['12 часов', '24 часа', '48 часов'].map((d, i) => (
          <Typography key={i} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {d}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Механики
      </Typography>
      <Stack spacing={0.3}>
        {['Bid system', 'Buyout', 'Buy/Sell orders', 'Regional markets'].map((m, i) => (
          <Typography key={i} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {m}
          </Typography>
        ))}
      </Stack>
    </Stack>
  );

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Комиссии
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['Listing fee (за выставление)', 'Exchange fee (при продаже)', 'История цен', 'Поиск и фильтрация'].map((f, i) => (
          <Typography key={i} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {f}
          </Typography>
        ))}
      </Stack>
    </Stack>
  );

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <GavelIcon sx={{ fontSize: '1.5rem', color: 'warning.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Аукцион дом
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Центральная система торговли! Bid/buyout, ордера, региональные рынки. Вдохновлено: WOW, GW2, FFXIV, Albion Online!
      </Alert>
      <Typography variant="body2" fontSize={cyberpunkTokens.fonts.sm}>
        Активных лотов: 0
      </Typography>
      <Grid container spacing={1}>
        {[].map((lot, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <AuctionLotCard lot={lot} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default AuctionHouseCorePage;

