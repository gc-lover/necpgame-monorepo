import React from 'react';
import { Typography, Stack, Box, Chip } from '@mui/material';
import TimelineIcon from '@mui/icons-material/Timeline';
import { CompactCard } from '@/shared/ui/cards';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface TradeDetailsCardProps {
  trade: {
    tradeId?: string;
    buyOrderId?: string;
    sellOrderId?: string;
    itemId?: string;
    price?: number;
    quantity?: number;
    executedAt?: string;
    buyerId?: string;
    sellerId?: string;
  };
}

export const TradeDetailsCard: React.FC<TradeDetailsCardProps> = ({ trade }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.5}>
          <TimelineIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Trade #{trade.tradeId ?? '—'}
          </Typography>
        </Box>
        <Chip
          label={trade.executedAt ?? '—'}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Item: {trade.itemId ?? 'unknown'}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Price: {trade.price ?? 0}¥ • Quantity: {trade.quantity ?? 0}
      </Typography>
      <Box display="flex" flexDirection="column" gap={0.25}>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Buyer: {trade.buyerId ?? '—'} (order {trade.buyOrderId ?? '—'})
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Seller: {trade.sellerId ?? '—'} (order {trade.sellOrderId ?? '—'})
        </Typography>
      </Box>
    </Stack>
  </CompactCard>
);

export default TradeDetailsCard;


