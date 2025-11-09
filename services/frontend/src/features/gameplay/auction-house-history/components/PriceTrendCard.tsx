/**
 * PriceTrendCard - карточка трендов цен аукциона
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Chip, Box } from '@mui/material';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import TrendingDownIcon from '@mui/icons-material/TrendingDown';
import SwapCallsIcon from '@mui/icons-material/SwapCalls';
import { CompactCard } from '@/shared/ui/cards';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

type TrendType = 'increasing' | 'decreasing' | 'stable' | 'volatile';

interface PriceTrendCardProps {
  itemName?: string;
  trend?: TrendType;
  priceChange7d?: number;
  priceChange30d?: number;
  volatility?: number;
}

export const PriceTrendCard: React.FC<PriceTrendCardProps> = ({
  itemName,
  trend = 'stable',
  priceChange7d = 0,
  priceChange30d = 0,
  volatility = 0,
}) => {
  const renderTrendIcon = () => {
    if (trend === 'increasing') {
      return <TrendingUpIcon sx={{ fontSize: '1rem', color: 'success.main' }} />;
    }
    if (trend === 'decreasing') {
      return <TrendingDownIcon sx={{ fontSize: '1rem', color: 'error.main' }} />;
    }
    return <SwapCallsIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />;
  };

  const trendColor =
    trend === 'increasing'
      ? 'success'
      : trend === 'decreasing'
      ? 'error'
      : trend === 'volatile'
      ? 'warning'
      : 'info';

  return (
    <CompactCard color={trendColor as any} glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            {renderTrendIcon()}
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {itemName ?? 'Unknown item'}
            </Typography>
          </Box>
          <Chip
            label={trend.toUpperCase()}
            size="small"
            color={trendColor as any}
            sx={{ height: 14, fontSize: '0.55rem' }}
          />
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Δ7d: {priceChange7d.toFixed(2)}% | Δ30d: {priceChange30d.toFixed(2)}%
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Volatility: {volatility.toFixed(2)}%
        </Typography>
      </Stack>
    </CompactCard>
  );
};


