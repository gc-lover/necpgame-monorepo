import React from 'react';
import { Typography, Stack, Box } from '@mui/material';
import FunctionsIcon from '@mui/icons-material/Functions';
import { CompactCard } from '@/shared/ui/cards';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface PriceBreakdownEntry {
  label: string;
  value?: number;
  type?: 'bonus' | 'penalty' | 'base';
}

export interface PriceBreakdownCardProps {
  total?: number;
  quantityTotal?: number;
  breakdown?: PriceBreakdownEntry[];
  modifiersApplied?: { name?: string; value?: number }[];
}

const typeColorMap: Record<string, string> = {
  bonus: '#05ffa1',
  penalty: '#ff2a6d',
  base: '#00f7ff',
};

export const PriceBreakdownCard: React.FC<PriceBreakdownCardProps> = ({
  total = 0,
  quantityTotal,
  breakdown = [],
  modifiersApplied = [],
}) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.75}>
          <FunctionsIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Price Breakdown
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {total}¥
        </Typography>
      </Box>
      {typeof quantityTotal === 'number' && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Total for quantity: {quantityTotal}¥
        </Typography>
      )}
      <Stack spacing={0.2}>
        {breakdown.map((entry) => (
          <Box
            key={entry.label}
            display="flex"
            justifyContent="space-between"
            fontSize={cyberpunkTokens.fonts.xs}
          >
            <Typography fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              {entry.label}
            </Typography>
            <Typography
              fontSize={cyberpunkTokens.fonts.xs}
              sx={{ color: typeColorMap[entry.type ?? 'base'] }}
            >
              {entry.value ?? 0}¥
            </Typography>
          </Box>
        ))}
      </Stack>
      {modifiersApplied.length > 0 && (
        <Stack spacing={0.2}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Modifiers Applied
          </Typography>
          {modifiersApplied.map((modifier) => (
            <Typography key={`${modifier.name}-${modifier.value}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • {modifier.name ?? 'Modifier'}: {modifier.value ?? 0}
            </Typography>
          ))}
        </Stack>
      )}
    </Stack>
  </CompactCard>
);

export default PriceBreakdownCard;


