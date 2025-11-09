import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box, Divider } from '@mui/material'
import SwapHorizIcon from '@mui/icons-material/SwapHoriz'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import TrendingDownIcon from '@mui/icons-material/TrendingDown'

interface CurrencyPairProps {
  pair: {
    pair_name?: string
    base_currency?: string
    quote_currency?: string
    rate?: number
    spread?: number
    change_24h?: number
    volume_24h?: number
    pair_type?: string
  }
}

export const CurrencyPairCard: React.FC<CurrencyPairProps> = ({ pair }) => {
  const isPositiveChange = (pair.change_24h || 0) >= 0

  return (
    <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box display="flex" alignItems="center" gap={0.5}>
              <SwapHorizIcon sx={{ fontSize: '1rem', color: 'primary.main' }} />
              <Typography variant="caption" fontSize="0.75rem" fontWeight="bold">
                {pair.base_currency}/{pair.quote_currency}
              </Typography>
            </Box>
            <Chip label={pair.pair_type || 'major'} size="small" color="primary" sx={{ height: 16, fontSize: '0.6rem' }} />
          </Box>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Typography variant="body2" fontSize="0.8rem" fontWeight="bold">
              {pair.rate?.toFixed(4) || '0.0000'}
            </Typography>
            <Box display="flex" alignItems="center" gap={0.3}>
              {isPositiveChange ? <TrendingUpIcon sx={{ fontSize: '0.9rem', color: 'success.main' }} /> : <TrendingDownIcon sx={{ fontSize: '0.9rem', color: 'error.main' }} />}
              <Typography variant="caption" fontSize="0.65rem" color={isPositiveChange ? 'success.main' : 'error.main'}>
                {isPositiveChange ? '+' : ''}
                {pair.change_24h?.toFixed(2)}%
              </Typography>
            </Box>
          </Box>
          <Divider sx={{ my: 0.5 }} />
          <Box display="flex" justifyContent="space-between">
            <Typography variant="caption" fontSize="0.6rem" color="text.secondary">
              Spread: {pair.spread}%
            </Typography>
            <Typography variant="caption" fontSize="0.6rem" color="text.secondary">
              Vol: {pair.volume_24h?.toLocaleString()}
            </Typography>
          </Box>
        </Stack>
      </CardContent>
    </Card>
  )
}

