/**
 * MarketOrderCard - карточка активного ордера игрока
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Box, Chip } from '@mui/material';
import SwapVertIcon from '@mui/icons-material/SwapVert';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import { CompactCard } from '@/shared/ui/cards';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface MarketOrderCardProps {
  order: {
    orderId?: string;
    character?: string;
    itemName?: string;
    side?: 'buy' | 'sell';
    orderType?: 'market' | 'limit';
    status?: 'pending' | 'partially_filled' | 'filled' | 'cancelled';
    price?: number;
    limitPrice?: number;
    quantity?: number;
    filledQuantity?: number;
    timeInForce?: 'GTC' | 'IOC' | 'FOK';
  };
  onCancel?: (orderId?: string) => void;
}

export const MarketOrderCard: React.FC<MarketOrderCardProps> = ({ order, onCancel }) => {
  const sideColor = order.side === 'buy' ? 'cyan' : 'yellow';
  const chipColor = order.side === 'buy' ? 'info' : 'warning';

  const statusChipColor =
    order.status === 'filled'
      ? 'success'
      : order.status === 'partially_filled'
      ? 'warning'
      : order.status === 'cancelled'
      ? 'default'
      : 'info';

  const filled = order.filledQuantity ?? 0;
  const total = order.quantity ?? 0;
  const fillPercent = total > 0 ? Math.round((filled / total) * 100) : 0;

  return (
    <CompactCard color={sideColor} glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <SwapVertIcon sx={{ fontSize: '1rem', color: 'primary.main' }} />
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
            {order.status && (
              <Chip
                label={order.status.replace('_', ' ').toUpperCase()}
                size="small"
                color={statusChipColor as any}
                sx={{ height: 14, fontSize: '0.55rem' }}
              />
            )}
          </Box>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          #{order.orderId ?? '—'} • {order.character ?? 'Unknown runner'}
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
          Цена: {order.price ?? order.limitPrice ?? 0}¥ {order.orderType === 'limit' && order.limitPrice ? `(лимит ${order.limitPrice}¥)` : ''}
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
          Исполнено: {filled}/{total} ({fillPercent}%)
        </Typography>
        {order.timeInForce && (
          <Box display="flex" alignItems="center" gap={0.5}>
            <AccessTimeIcon sx={{ fontSize: '0.75rem', color: 'secondary.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              TIF: {order.timeInForce}
            </Typography>
          </Box>
        )}
        {onCancel && order.status === 'pending' && (
          <CyberpunkButton
            variant="secondary"
            size="small"
            fullWidth
            onClick={() => onCancel(order.orderId)}
          >
            Отменить ордер
          </CyberpunkButton>
        )}
      </Stack>
    </CompactCard>
  );
};


