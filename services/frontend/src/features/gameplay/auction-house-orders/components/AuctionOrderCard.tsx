/**
 * AuctionOrderCard - карточка аукционного ордера
 * 
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Chip, Box } from '@mui/material';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import SellIcon from '@mui/icons-material/Sell';
import { CompactCard } from '@/shared/ui/cards';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

interface AuctionOrderCardProps {
  order: {
    order_id?: string;
    item_name?: string;
    quantity?: number;
    price?: number;
    filled_quantity?: number;
    status?: string;
    expires_at?: string;
    order_type?: 'BUY' | 'SELL';
  };
  onCancel?: () => void;
}

export const AuctionOrderCard: React.FC<AuctionOrderCardProps> = ({ order, onCancel }) => {
  const isBuyOrder = order.order_type === 'BUY';
  const color = isBuyOrder ? 'cyan' : 'yellow';
  const Icon = isBuyOrder ? ShoppingCartIcon : SellIcon;

  const getStatusColor = (status?: string) => {
    switch (status) {
      case 'filled': return 'success';
      case 'partially_filled': return 'warning';
      case 'pending': return 'info';
      default: return 'default';
    }
  };

  const fillPercent = order.quantity ? ((order.filled_quantity || 0) / order.quantity) * 100 : 0;

  return (
    <CompactCard color={color as any} glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <Icon sx={{ fontSize: '1rem', color: isBuyOrder ? 'info.main' : 'warning.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {order.item_name}
            </Typography>
          </Box>
          <Chip label={order.order_type} size="small" color={isBuyOrder ? 'info' : 'warning'} sx={{ height: 14, fontSize: '0.55rem' }} />
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {isBuyOrder ? 'Max price' : 'Min price'}: {order.price}¥
        </Typography>
        <Box display="flex" gap={0.3} flexWrap="wrap" alignItems="center">
          <Chip label={`${order.filled_quantity || 0}/${order.quantity}`} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />
          {order.status && <Chip label={order.status} size="small" color={getStatusColor(order.status) as any} sx={{ height: 14, fontSize: '0.55rem' }} />}
          {order.expires_at && <Chip label={`Expires: ${order.expires_at}`} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />}
        </Box>
        <Box>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Filled: {fillPercent.toFixed(0)}%
          </Typography>
        </Box>
        {onCancel && order.status === 'pending' && (
          <CyberpunkButton variant="danger" size="small" fullWidth onClick={onCancel}>
            Отменить ордер
          </CyberpunkButton>
        )}
      </Stack>
    </CompactCard>
  );
};

