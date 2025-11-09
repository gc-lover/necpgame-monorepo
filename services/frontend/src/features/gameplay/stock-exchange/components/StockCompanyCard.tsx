import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box } from '@mui/material'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import TrendingDownIcon from '@mui/icons-material/TrendingDown'
import type { StockCompany } from '@/api/generated/stock-exchange/models'

interface StockCompanyCardProps {
  company: StockCompany
  onClick?: (ticker: string) => void
}

export const StockCompanyCard: React.FC<StockCompanyCardProps> = ({ company, onClick }) => {
  const isPositive = (company.price_change_24h || 0) >= 0
  const changeColor = isPositive ? 'success.main' : 'error.main'
  const TrendIcon = isPositive ? TrendingUpIcon : TrendingDownIcon

  return (
    <Card
      sx={{
        border: '1px solid',
        borderColor: 'divider',
        cursor: onClick ? 'pointer' : 'default',
        '&:hover': onClick ? { borderColor: 'primary.main' } : {},
      }}
      onClick={() => onClick && company.ticker && onClick(company.ticker)}
    >
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box>
              <Typography variant="subtitle2" fontWeight="bold" fontSize="0.875rem">
                {company.ticker}
              </Typography>
              <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
                {company.name}
              </Typography>
            </Box>
            <Chip label={company.sector} size="small" sx={{ height: 18, fontSize: '0.65rem' }} />
          </Box>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box>
              <Typography variant="body2" fontSize="0.8rem" fontWeight="bold">
                €${company.current_price?.toLocaleString()}
              </Typography>
              <Typography variant="caption" fontSize="0.65rem" color="text.secondary">
                Cap: €${(company.market_cap || 0) / 1000000}M
              </Typography>
            </Box>
            <Box display="flex" alignItems="center" gap={0.3}>
              <TrendIcon sx={{ fontSize: '0.9rem', color: changeColor }} />
              <Typography variant="body2" fontSize="0.75rem" fontWeight="bold" color={changeColor}>
                {isPositive ? '+' : ''}
                {company.price_change_24h?.toFixed(2)}%
              </Typography>
            </Box>
          </Box>
          {company.dividend_yield && (
            <Typography variant="caption" fontSize="0.65rem" color="success.main">
              Дивиденды: {company.dividend_yield}%
            </Typography>
          )}
        </Stack>
      </CardContent>
    </Card>
  )
}

