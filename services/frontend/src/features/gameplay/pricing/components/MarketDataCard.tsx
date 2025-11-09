import React from 'react';
import { Typography, Stack, Box, Chip } from '@mui/material';
import QueryStatsIcon from '@mui/icons-material/QueryStats';
import { CompactCard } from '@/shared/ui/cards';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface MarketDataCardProps {
  data: {
    category?: string;
    region?: string;
    timestamp?: string;
    averagePrices?: Record<string, number>;
    trendingUp?: string[];
    trendingDown?: string[];
    highDemand?: string[];
    lowSupply?: string[];
  };
}

export const MarketDataCard: React.FC<MarketDataCardProps> = ({ data }) => (
  <CompactCard color="cyan" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.75}>
          <QueryStatsIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Market Data
          </Typography>
        </Box>
        <Chip
          label={data.timestamp ?? '—'}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Box display="flex" gap={1} flexWrap="wrap">
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
          Category: {data.category ?? '—'}
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
          Region: {data.region ?? 'GLOBAL'}
        </Typography>
      </Box>
      {data.averagePrices && (
        <Stack spacing={0.2}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Average Prices
          </Typography>
          {Object.entries(data.averagePrices).map(([item, price]) => (
            <Typography key={item} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • {item}: {price}¥
            </Typography>
          ))}
        </Stack>
      )}
      <Box display="flex" gap={1} flexWrap="wrap">
        {data.trendingUp?.slice(0, 3).map((item) => (
          <Chip
            key={`up-${item}`}
            label={`▲ ${item}`}
            size="small"
            color="success"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        ))}
        {data.trendingDown?.slice(0, 3).map((item) => (
          <Chip
            key={`down-${item}`}
            label={`▼ ${item}`}
            size="small"
            color="warning"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        ))}
      </Box>
      {(data.highDemand?.length || data.lowSupply?.length) && (
        <Stack spacing={0.1}>
          {data.highDemand?.slice(0, 3).map((item) => (
            <Typography key={`demand-${item}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • High demand: {item}
            </Typography>
          ))}
          {data.lowSupply?.slice(0, 3).map((item) => (
            <Typography key={`supply-${item}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • Low supply: {item}
            </Typography>
          ))}
        </Stack>
      )}
    </Stack>
  </CompactCard>
);

export default MarketDataCard;


