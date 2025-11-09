import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import GridOnIcon from '@mui/icons-material/GridOn'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface HeatMapItem {
  itemId?: string
  itemName?: string
  priceChangePercent?: number
  volumeChangePercent?: number
}

export interface HeatMapCardProps {
  category?: string
  timeframe?: string
  items?: HeatMapItem[]
}

const getChangeColor = (value?: number) => {
  if (typeof value !== 'number') return 'text.secondary'
  if (value > 0) return 'success.main'
  if (value < 0) return 'error.main'
  return 'text.secondary'
}

export const HeatMapCard: React.FC<HeatMapCardProps> = ({
  category = 'all',
  timeframe = '1d',
  items = [],
}) => (
  <CompactCard color="pink" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <GridOnIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Heat Map
          </Typography>
        </Box>
        <Box display="flex" gap={0.3}>
          <Chip
            label={category.toUpperCase()}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
          <Chip
            label={timeframe}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        </Box>
      </Box>
      {items.length === 0 ? (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Нет данных тепловой карты
        </Typography>
      ) : (
        <Stack spacing={0.3}>
          {items.slice(0, 6).map((item) => (
            <Box
              key={item.itemId ?? item.itemName}
              display="flex"
              justifyContent="space-between"
              alignItems="center"
            >
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                {item.itemName ?? item.itemId}
              </Typography>
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} sx={{ color: getChangeColor(item.priceChangePercent) }}>
                {item.priceChangePercent ?? 0}%
              </Typography>
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} sx={{ color: getChangeColor(item.volumeChangePercent) }}>
                Vol {item.volumeChangePercent ?? 0}%
              </Typography>
            </Box>
          ))}
        </Stack>
      )}
    </Stack>
  </CompactCard>
)

export default HeatMapCard

