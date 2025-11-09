import React from 'react';
import { Typography, Stack, Box, Chip, Divider } from '@mui/material';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import HourglassTopIcon from '@mui/icons-material/HourglassTop';
import { CompactCard } from '@/shared/ui/cards';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export type ExecutionStatus = 'filled' | 'partially_filled' | 'pending';

export interface ExecutionTrade {
  tradeId?: string;
  price?: number;
  quantity?: number;
  executedAt?: string;
}

export interface ExecutionResultCardProps {
  result: {
    orderId?: string;
    status?: ExecutionStatus;
    filledQuantity?: number;
    remainingQuantity?: number;
    averagePrice?: number;
    totalCost?: number;
    commission?: number;
    trades?: ExecutionTrade[];
  };
}

const statusColorMap: Record<ExecutionStatus, { card: 'green' | 'yellow' | 'purple'; chip: 'success' | 'warning' | 'info' }> = {
  filled: { card: 'green', chip: 'success' },
  partially_filled: { card: 'yellow', chip: 'warning' },
  pending: { card: 'purple', chip: 'info' },
};

export const ExecutionResultCard: React.FC<ExecutionResultCardProps> = ({ result }) => {
  const status = result.status ?? 'pending';
  const mapping = statusColorMap[status];

  return (
    <CompactCard color={mapping.card} glowIntensity="normal" compact>
      <Stack spacing={0.75}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.75}>
            {status === 'filled' ? (
              <CheckCircleIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
            ) : (
              <HourglassTopIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
            )}
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              Execution #{result.orderId ?? '—'}
            </Typography>
          </Box>
          <Chip
            label={status.replace('_', ' ').toUpperCase()}
            size="small"
            color={mapping.chip}
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        </Box>

        <Box display="flex" gap={1} flexWrap="wrap">
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            Filled: {result.filledQuantity ?? 0}
          </Typography>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            Remaining: {result.remainingQuantity ?? 0}
          </Typography>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            Avg price: {result.averagePrice ?? 0}¥
          </Typography>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            Total: {result.totalCost ?? 0}¥
          </Typography>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            Commission: {result.commission ?? 0}¥
          </Typography>
        </Box>

        {(result.trades?.length ?? 0) > 0 && (
          <Stack spacing={0.5}>
            <Divider sx={{ borderColor: 'rgba(255, 255, 255, 0.1)' }} />
            <Typography
              variant="caption"
              fontSize={cyberpunkTokens.fonts.xs}
              color="text.secondary"
            >
              Trades ({result.trades?.length ?? 0})
            </Typography>
            <Stack spacing={0.25}>
              {result.trades?.slice(0, 4).map((trade) => (
                <Box
                  key={trade.tradeId ?? Math.random()}
                  display="flex"
                  justifyContent="space-between"
                  fontSize={cyberpunkTokens.fonts.xs}
                >
                  <Typography fontSize={cyberpunkTokens.fonts.xs}>
                    #{trade.tradeId ?? '—'}
                  </Typography>
                  <Typography fontSize={cyberpunkTokens.fonts.xs}>
                    {trade.quantity ?? 0} @ {trade.price ?? 0}¥
                  </Typography>
                  <Typography fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                    {trade.executedAt ?? '—'}
                  </Typography>
                </Box>
              ))}
              {(result.trades?.length ?? 0) > 4 && (
                <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                  • +{(result.trades?.length ?? 0) - 4} trades hidden
                </Typography>
              )}
            </Stack>
          </Stack>
        )}
      </Stack>
    </CompactCard>
  );
};

export default ExecutionResultCard;


