import React from 'react';
import { Typography, Stack, Box, Chip } from '@mui/material';
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn';
import { CompactCard } from '@/shared/ui/cards';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

interface MultiplierInfo {
  name: string;
  value?: number | string;
}

export interface ItemPriceCardProps {
  item?: {
    itemId?: string;
    itemName?: string;
    basePrice?: number;
    currentPrice?: number;
    vendorSellPrice?: number;
    vendorBuyPrice?: number;
  };
  multipliers?: MultiplierInfo[];
}

export const ItemPriceCard: React.FC<ItemPriceCardProps> = ({ item, multipliers = [] }) => (
  <CompactCard color="green" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.75}>
          <MonetizationOnIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {item?.itemName ?? 'Unknown Item'}
          </Typography>
        </Box>
        <Chip
          label={`${item?.currentPrice ?? 0}¥`}
          size="small"
          color="success"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        ID: {item?.itemId ?? '—'}
      </Typography>
      <Box display="flex" flexWrap="wrap" gap={1}>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
          Base: {item?.basePrice ?? 0}¥
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
          Vendor Sell: {item?.vendorSellPrice ?? 0}¥
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
          Vendor Buy: {item?.vendorBuyPrice ?? 0}¥
        </Typography>
      </Box>
      {multipliers.length > 0 && (
        <Stack spacing={0.25}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Multipliers
          </Typography>
          <Box display="flex" flexWrap="wrap" gap={0.5}>
            {multipliers.map((multiplier) => (
              <Chip
                key={multiplier.name}
                label={`${multiplier.name}: ${multiplier.value ?? '—'}`}
                size="small"
                sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
              />
            ))}
          </Box>
        </Stack>
      )}
    </Stack>
  </CompactCard>
);

export default ItemPriceCard;


