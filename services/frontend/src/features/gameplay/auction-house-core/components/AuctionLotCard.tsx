/**
 * AuctionLotCard - карточка аукционного лота
 * 
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Chip, Box } from '@mui/material';
import GavelIcon from '@mui/icons-material/Gavel';
import { CompactCard } from '@/shared/ui/cards';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

interface AuctionLotCardProps {
  lot: {
    lot_id?: string;
    item_name?: string;
    quantity?: number;
    current_bid?: number;
    buyout_price?: number;
    seller_name?: string;
    time_left?: string;
    rarity?: string;
  };
  onBid?: () => void;
  onBuyout?: () => void;
}

export const AuctionLotCard: React.FC<AuctionLotCardProps> = ({ lot, onBid, onBuyout }) => {
  const getRarityColor = (rarity?: string) => {
    switch (rarity) {
      case 'LEGENDARY': return 'error';
      case 'EPIC': return 'purple';
      case 'RARE': return 'info';
      case 'UNCOMMON': return 'success';
      case 'COMMON': return 'default';
      default: return 'default';
    }
  };

  return (
    <CompactCard color={getRarityColor(lot.rarity) as any} glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <GavelIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {lot.item_name}
            </Typography>
          </Box>
          {lot.quantity && lot.quantity > 1 && (
            <Chip label={`x${lot.quantity}`} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />
          )}
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Seller: {lot.seller_name}
        </Typography>
        <Box display="flex" gap={0.3} flexWrap="wrap" alignItems="center">
          {lot.rarity && <Chip label={lot.rarity} size="small" color={getRarityColor(lot.rarity) as any} sx={{ height: 14, fontSize: '0.55rem' }} />}
          {lot.time_left && <Chip label={lot.time_left} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />}
        </Box>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="warning.main">
            Bid: {lot.current_bid}¥
          </Typography>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="success.main">
            Buyout: {lot.buyout_price}¥
          </Typography>
        </Box>
        <Box display="flex" gap={0.5}>
          {onBid && (
            <CyberpunkButton variant="warning" size="small" fullWidth onClick={onBid}>
              Ставка
            </CyberpunkButton>
          )}
          {onBuyout && (
            <CyberpunkButton variant="success" size="small" fullWidth onClick={onBuyout}>
              Выкуп
            </CyberpunkButton>
          )}
        </Box>
      </Stack>
    </CompactCard>
  );
};

