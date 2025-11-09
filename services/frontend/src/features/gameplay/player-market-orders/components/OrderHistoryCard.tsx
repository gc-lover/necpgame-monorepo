/**
 * OrderHistoryCard - карточка истории ордера
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Box, Chip } from '@mui/material';
import HistoryToggleOffIcon from '@mui/icons-material/HistoryToggleOff';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import { CompactCard } from '@/shared/ui/cards';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface OrderHistoryCardProps {
  order: {
    orderId?: string;
    itemName?: string;
    side?: 'buy' | 'sell';
    orderType?: 'market' | 'limit';
    executedPrice?: number;
    quantity?: number;
    filledAt?: string;
    pnl?: number;
    fees?: number;
  };
}

export const OrderHistoryCard: React.FC<OrderHistoryCardProps> = ({ order }) => {
  const sideColor = order.side === 'buy' ? 'cyan' : 'yellow';
  const chipColor = order.side === 'buy' ? 'info' : 'warning';
  const pnlColor = order.pnl && order.pnl >= 0 ? 'success.main' : 'error.main';

  return (
    <CompactCard color={sideColor} glowIntensity="weak" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <HistoryToggleOffIcon sx={{ fontSize: '1rem', color: 'primary.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {order.itemName ?? 'Неизвестный товар'}
            </Typography>
          </Box>
          <Box display="flex" gap={0.3}>
            {order.side && (
              <Chip
                label={order.side.toUpperCase()}
                size="small"
                color={chipColor as any}
                sx={{ height: 14, fontSize: '0.55rem' }}
              />
            )}
            {order.orderType && (
              <Chip
                label={order.orderType.toUpperCase()}
                size="small"
                sx={{ height: 14, fontSize: '0.55rem' }}
              />
            )}
          </Box>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          #{order.orderId ?? '—'} • Executed {order.filledAt ?? '—'}
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
          Цена исполнения: {order.executedPrice ?? 0}¥ • Кол-во: {order.quantity ?? 0}
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
          Комиссии: {order.fees ?? 0}¥
        </Typography>
        {order.pnl !== undefined && (
          <Box display="flex" alignItems="center" gap={0.5}>
            <TrendingUpIcon sx={{ fontSize: '0.8rem', color: pnlColor }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} sx={{ color: pnlColor }}>
              PnL: {order.pnl >= 0 ? '+' : ''}{order.pnl}¥
            </Typography>
          </Box>
        )}
      </Stack>
    </CompactCard>
  );
};


