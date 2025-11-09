/**
 * AuctionSearchResultCard - карточка результата поиска аукциона
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Chip, Box } from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import { CompactCard } from '@/shared/ui/cards';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

interface AuctionSearchResultCardProps {
  lot: {
    lot_id?: string;
    item_name?: string;
    rarity?: string;
    current_price?: number;
    buyout_price?: number;
    time_remaining?: number;
    seller_name?: string;
    category?: string;
  };
  onOpen?: () => void;
}

export const AuctionSearchResultCard: React.FC<AuctionSearchResultCardProps> = ({ lot, onOpen }) => {
  const getRarityColor = (rarity?: string) => {
    switch (rarity) {
      case 'legendary':
      case 'LEGENDARY':
        return 'error';
      case 'epic':
      case 'EPIC':
        return 'purple';
      case 'rare':
      case 'RARE':
        return 'info';
      case 'uncommon':
      case 'UNCOMMON':
        return 'success';
      default:
        return 'default';
    }
  };

  const secondsToReadable = (seconds?: number) => {
    if (!seconds) {
      return '—';
    }
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    if (hours > 0) {
      return `${hours}ч ${minutes}м`;
    }
    return `${minutes}м`;
  };

  return (
    <CompactCard color={getRarityColor(lot.rarity) as any} glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <SearchIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {lot.item_name}
            </Typography>
          </Box>
          <Box display="flex" gap={0.3}>
            {lot.category && (
              <Chip label={lot.category} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />
            )}
            {lot.rarity && (
              <Chip
                label={lot.rarity}
                size="small"
                color={getRarityColor(lot.rarity) as any}
                sx={{ height: 14, fontSize: '0.55rem' }}
              />
            )}
          </Box>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Seller: {lot.seller_name ?? 'unknown'}
        </Typography>
        <Box display="flex" justifyContent="space-between">
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="warning.main">
            Current: {lot.current_price ?? 0}¥
          </Typography>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="success.main">
            Buyout: {lot.buyout_price ?? 0}¥
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Time left: {secondsToReadable(lot.time_remaining)}
        </Typography>
        {onOpen && (
          <CyberpunkButton variant="primary" size="small" fullWidth onClick={onOpen}>
            Открыть лот
          </CyberpunkButton>
        )}
      </Stack>
    </CompactCard>
  );
};


