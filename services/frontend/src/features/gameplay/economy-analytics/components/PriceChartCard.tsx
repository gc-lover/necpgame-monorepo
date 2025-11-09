import React from 'react'
import { Typography, Stack, Chip, Box } from '@mui/material'
import ShowChartIcon from '@mui/icons-material/ShowChart'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

interface PriceChartProps {
  chart: {
    item_id?: string
    chart_type?: string
    timeframe?: string
    data_points?: Array<{
      timestamp?: string
      open?: number
      high?: number
      low?: number
      close?: number
      volume?: number
    }>
  }
}

export const PriceChartCard: React.FC<PriceChartProps> = ({ chart }) => {
  const lastPoint = chart.data_points?.[chart.data_points.length - 1]

  return (
    <CompactCard color="cyan" glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <ShowChartIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {chart.item_id ?? 'Unknown asset'}
            </Typography>
          </Box>
          <Chip
            label={chart.timeframe ?? '1d'}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        </Box>
        <Box display="flex" gap={0.3} flexWrap="wrap">
          <Chip
            label={(chart.chart_type ?? 'line').toUpperCase()}
            size="small"
            color="info"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
          <Chip
            label={`Data: ${chart.data_points?.length ?? 0}`}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        </Box>
        {lastPoint ? (
          <Stack spacing={0.25}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                Close: {lastPoint.close ?? 0}¥
              </Typography>
              <TrendingUpIcon sx={{ fontSize: '0.9rem', color: 'success.main' }} />
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              High: {lastPoint.high ?? 0} | Low: {lastPoint.low ?? 0}
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Volume: {lastPoint.volume ?? 0}
            </Typography>
          </Stack>
        ) : (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Нет данных для выбранных параметров
          </Typography>
        )}
      </Stack>
    </CompactCard>
  )
}

