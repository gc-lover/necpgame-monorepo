import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import InsightsIcon from '@mui/icons-material/Insights'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface IndicatorValue {
  label: string
  value?: number | string
  signal?: string
}

export interface TechnicalIndicatorsCardProps {
  timeframe?: string
  indicators?: IndicatorValue[]
}

export const TechnicalIndicatorsCard: React.FC<TechnicalIndicatorsCardProps> = ({
  timeframe,
  indicators = [],
}) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <InsightsIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Technical Indicators
          </Typography>
        </Box>
        {timeframe && (
          <Chip
            label={timeframe}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        )}
      </Box>
      {indicators.length === 0 ? (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Нет активных индикаторов
        </Typography>
      ) : (
        <Stack spacing={0.25}>
          {indicators.map((indicator) => (
            <Box
              key={`${indicator.label}-${indicator.value}`}
              display="flex"
              justifyContent="space-between"
              alignItems="center"
            >
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                {indicator.label}
              </Typography>
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                {indicator.value ?? '—'}
              </Typography>
              <Typography
                variant="caption"
                fontSize={cyberpunkTokens.fonts.xs}
                sx={{ color: indicator.signal === 'bearish' ? 'error.main' : 'success.main' }}
              >
                {indicator.signal ?? 'neutral'}
              </Typography>
            </Box>
          ))}
        </Stack>
      )}
    </Stack>
  </CompactCard>
)

export default TechnicalIndicatorsCard

