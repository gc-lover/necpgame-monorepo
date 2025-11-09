/**
 * PriceHistoryCard - карточка истории цен аукциона
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Chip, Box } from '@mui/material';
import TimelineIcon from '@mui/icons-material/Timeline';
import { CompactCard } from '@/shared/ui/cards';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

interface PricePoint {
  timestamp?: string;
  average_price?: number;
  min_price?: number;
  max_price?: number;
  volume?: number;
  trades_count?: number;
}

interface PriceHistoryCardProps {
  itemName?: string;
  period?: string;
  interval?: string;
  dataPoints?: PricePoint[];
}

export const PriceHistoryCard: React.FC<PriceHistoryCardProps> = ({
  itemName,
  period,
  interval,
  dataPoints = [],
}) => {
  const latest = dataPoints[dataPoints.length - 1];
  const avgPrice = latest?.average_price ?? 0;
  const minPrice = latest?.min_price ?? 0;
  const maxPrice = latest?.max_price ?? 0;
  const volume = latest?.volume ?? 0;

  return (
    <CompactCard color="cyan" glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <TimelineIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {itemName ?? 'Unknown item'}
            </Typography>
          </Box>
          <Box display="flex" gap={0.3}>
            {period && (
              <Chip
                label={`Period: ${period}`}
                size="small"
                sx={{ height: 14, fontSize: '0.55rem' }}
              />
            )}
            {interval && (
              <Chip
                label={`Interval: ${interval}`}
                size="small"
                sx={{ height: 14, fontSize: '0.55rem' }}
              />
            )}
          </Box>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Последний срез: 평균 {avgPrice}¥ / мин {minPrice}¥ / макс {maxPrice}¥
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Volume: {volume} (trades {latest?.trades_count ?? 0})
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Points: {dataPoints.length}
        </Typography>
      </Stack>
    </CompactCard>
  );
};


