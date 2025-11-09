import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box, Divider } from '@mui/material'
import RouteIcon from '@mui/icons-material/Route'
import LocalShippingIcon from '@mui/icons-material/LocalShipping'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'

interface TradeRouteProps {
  route: {
    route_id?: string
    name?: string
    from_hub?: string
    to_hub?: string
    distance_km?: number
    delivery_time_hours?: number
    base_profit_margin?: number
    risk_level?: string
    available_cargo_types?: string[]
  }
}

export const TradeRouteCard: React.FC<TradeRouteProps> = ({ route }) => {
  const getRiskColor = (risk?: string) => {
    switch (risk) {
      case 'low':
        return 'success'
      case 'medium':
        return 'warning'
      case 'high':
        return 'error'
      default:
        return 'default'
    }
  }

  return (
    <Card sx={{ border: '1px solid', borderColor: 'divider' }}>
      <CardContent sx={{ p: 1.5, '&:last-child': { pb: 1.5 } }}>
        <Stack spacing={0.5}>
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <Box display="flex" alignItems="center" gap={0.5}>
              <RouteIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
              <Typography variant="caption" fontSize="0.75rem" fontWeight="bold">
                {route.name}
              </Typography>
            </Box>
          </Box>
          <Box display="flex" alignItems="center" gap={0.5}>
            <Typography variant="caption" fontSize="0.7rem" color="text.secondary">
              {route.from_hub} → {route.to_hub}
            </Typography>
          </Box>
          <Divider sx={{ my: 0.5 }} />
          <Box display="flex" gap={0.3} flexWrap="wrap">
            {route.risk_level && <Chip label={route.risk_level.toUpperCase()} size="small" color={getRiskColor(route.risk_level)} sx={{ height: 14, fontSize: '0.55rem' }} />}
            {route.distance_km && <Chip label={`${route.distance_km} km`} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />}
            {route.delivery_time_hours && <Chip label={`${route.delivery_time_hours}h`} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />}
          </Box>
          <Box display="flex" alignItems="center" gap={0.3}>
            <TrendingUpIcon sx={{ fontSize: '0.8rem', color: 'success.main' }} />
            <Typography variant="caption" fontSize="0.65rem" color="success.main">
              Прибыль: {route.base_profit_margin}%
            </Typography>
          </Box>
        </Stack>
      </CardContent>
    </Card>
  )
}

