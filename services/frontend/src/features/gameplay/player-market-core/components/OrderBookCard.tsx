/**
 * OrderBookCard - карточка стакана заявок рынка игроков
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Box, Chip, Divider } from '@mui/material';
import ImportExportIcon from '@mui/icons-material/ImportExport';
import { CompactCard } from '@/shared/ui/cards';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

interface OrderLevel {
  price?: number;
  quantity?: number;
  total_orders?: number;
}

interface OrderBookCardProps {
  itemName?: string;
  spread?: number;
  lastTradePrice?: number;
  buyOrders?: OrderLevel[];
  sellOrders?: OrderLevel[];
}

export const OrderBookCard: React.FC<OrderBookCardProps> = ({
  itemName = 'Unknown',
  spread = 0,
  lastTradePrice = 0,
  buyOrders = [],
  sellOrders = [],
}) => {
  const renderLevel = (label: 'BUY' | 'SELL', level: OrderLevel, idx: number) => (
    <Box
      key={`${label}-${idx}`}
      display="flex"
      justifyContent="space-between"
      fontSize={cyberpunkTokens.fonts.xs}
      color={label === 'BUY' ? 'success.main' : 'error.main'}
    >
      <Typography fontSize={cyberpunkTokens.fonts.xs} width="33%">
        {level.price ?? 0}¥
      </Typography>
      <Typography fontSize={cyberpunkTokens.fonts.xs} width="33%" textAlign="center">
        {level.quantity ?? 0}
      </Typography>
      <Typography fontSize={cyberpunkTokens.fonts.xs} width="33%" textAlign="right">
        {level.total_orders ?? 0}
      </Typography>
    </Box>
  );

  return (
    <CompactCard color="purple" glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <ImportExportIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {itemName}
            </Typography>
          </Box>
          <Box display="flex" gap={0.3}>
            <Chip
              label={`Spread: ${spread}¥`}
              size="small"
              sx={{ height: 14, fontSize: '0.55rem' }}
            />
            <Chip
              label={`Last: ${lastTradePrice}¥`}
              size="small"
              sx={{ height: 14, fontSize: '0.55rem' }}
            />
          </Box>
        </Box>
        <Typography
          variant="caption"
          fontSize={cyberpunkTokens.fonts.xs}
          color="text.secondary"
          textAlign="center"
        >
          Стакан заявок (price | qty | orders)
        </Typography>
        <Divider sx={{ borderColor: 'rgba(255, 255, 255, 0.08)' }} />
        <Stack spacing={0.5}>
          <Typography
            variant="caption"
            fontSize={cyberpunkTokens.fonts.xs}
            fontWeight="bold"
            color="success.main"
          >
            BUY ORDERS
          </Typography>
          {buyOrders.slice(0, 4).map((level, idx) => renderLevel('BUY', level, idx))}
        </Stack>
        <Divider sx={{ borderColor: 'rgba(255, 255, 255, 0.08)' }} />
        <Stack spacing={0.5}>
          <Typography
            variant="caption"
            fontSize={cyberpunkTokens.fonts.xs}
            fontWeight="bold"
            color="error.main"
          >
            SELL ORDERS
          </Typography>
          {sellOrders.slice(0, 4).map((level, idx) => renderLevel('SELL', level, idx))}
        </Stack>
      </Stack>
    </CompactCard>
  );
};


