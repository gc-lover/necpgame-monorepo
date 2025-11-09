/**
 * PlayerOrderCard - карточка ордера игрока на рынке
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Box, Chip } from '@mui/material';
import AssignmentTurnedInIcon from '@mui/icons-material/AssignmentTurnedIn';
import { CompactCard } from '@/shared/ui/cards';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

type OrderSide = 'BUY' | 'SELL';
type OrderType = 'MARKET' | 'LIMIT';
type OrderStatus = 'PENDING' | 'PARTIAL' | 'FILLED' | 'CANCELLED';

interface PlayerOrderCardProps {
  order: {
    order_id?: string;
    item_name?: string;
    side?: OrderSide;
    type?: OrderType;
    status?: OrderStatus;
    price?: number;
    quantity?: number;
    filled_quantity?: number;
  };
  onCancel?: () => void;
}

export const PlayerOrderCard: React.FC<PlayerOrderCardProps> = ({ order, onCancel }) => {
  const sidePaletteColor = order.side === 'BUY' ? 'info' : 'warning';
  const cardColor = order.side === 'BUY' ? 'cyan' : 'yellow';
  const statusColor =
    order.status === 'FILLED'
      ? 'success'
      : order.status === 'PARTIAL'
      ? 'warning'
      : order.status === 'CANCELLED'
      ? 'default'
      : 'info';

  const filled = order.filled_quantity ?? 0;
  const total = order.quantity ?? 0;
  const fillPercent = total > 0 ? Math.round((filled / total) * 100) : 0;

  return (
    <CompactCard color={cardColor} glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <AssignmentTurnedInIcon sx={{ fontSize: '1rem', color: 'primary.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {order.item_name ?? 'Unknown item'}
            </Typography>
          </Box>
          <Box display="flex" gap={0.3}>
            {order.side && (
              <Chip
                label={order.side}
                size="small"
                color={sidePaletteColor as any}
                sx={{ height: 14, fontSize: '0.55rem' }}
              />
            )}
            {order.type && (
              <Chip
                label={order.type}
                size="small"
                sx={{ height: 14, fontSize: '0.55rem' }}
              />
            )}
            {order.status && (
              <Chip
                label={order.status}
                size="small"
                color={statusColor as any}
                sx={{ height: 14, fontSize: '0.55rem' }}
              />
            )}
          </Box>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Price: {order.price ?? 0}¥
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Filled: {filled}/{total} ({fillPercent}%)
        </Typography>
        {onCancel && order.status === 'PENDING' && (
          <CyberpunkButton variant="danger" size="small" fullWidth onClick={onCancel}>
            Отменить
          </CyberpunkButton>
        )}
      </Stack>
    </CompactCard>
  );
};


