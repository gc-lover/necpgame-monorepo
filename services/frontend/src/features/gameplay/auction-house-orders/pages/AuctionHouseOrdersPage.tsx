/**
 * AuctionHouseOrdersPage - страница системы ордеров аукциона
 * 
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Typography, Stack, Divider, Alert, Grid, Box } from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ListAltIcon from '@mui/icons-material/ListAlt';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { AuctionOrderCard } from '../components/AuctionOrderCard';
import { useGameState } from '@/features/game/hooks/useGameState';

export const AuctionHouseOrdersPage: React.FC = () => {
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
        Auction Orders
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Система ордеров
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Типы ордеров
      </Typography>
      <Stack spacing={0.3}>
        {['Buy orders (покупка по max цене)', 'Sell orders (продажа по min цене)'].map((t, i) => (
          <Typography key={i} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {t}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Механики
      </Typography>
      <Stack spacing={0.3}>
        {['Автоисполнение', 'Частичное исполнение', 'Срок жизни (24-48ч)'].map((m, i) => (
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
        Статусы
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['filled - исполнен', 'partially_filled - частично', 'pending - ожидает'].map((f, i) => (
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
        <ListAltIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Система ордеров аукциона
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Buy/Sell orders с автоматическим исполнением! Вдохновлено: GW2, EVE Online!
      </Alert>
      <Typography variant="body2" fontSize={cyberpunkTokens.fonts.sm}>
        Активных ордеров: 0
      </Typography>
      <Grid container spacing={1}>
        {[].map((order, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <AuctionOrderCard order={order} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default AuctionHouseOrdersPage;

