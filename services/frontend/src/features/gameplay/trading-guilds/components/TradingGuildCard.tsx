import React from 'react'
import { Card, CardContent, Typography, Stack, Chip, Box, Divider } from '@mui/material'
import StoreIcon from '@mui/icons-material/Store'
import PeopleIcon from '@mui/icons-material/People'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'

interface TradingGuildProps {
  guild: {
    guild_id?: string
    name?: string
    type?: string
    level?: number
    member_count?: number
    treasury_balance?: number
    reputation_score?: number
    trade_volume_monthly?: number
    region?: string
  }
}

export const TradingGuildCard: React.FC<TradingGuildProps> = ({ guild }) => {
  const getGuildTypeColor = (type?: string) => {
    switch (type) {
      case 'MERCHANT':
        return 'success'
      case 'CRAFTSMAN':
        return 'warning'
      case 'TRANSPORT':
        return 'info'
      case 'FINANCIAL':
        return 'error'
      case 'MIXED':
        return 'primary'
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
              <StoreIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
              <Typography variant="caption" fontSize="0.75rem" fontWeight="bold">
                {guild.name}
              </Typography>
            </Box>
            <Chip label={`LVL ${guild.level || 1}`} size="small" color="primary" sx={{ height: 16, fontSize: '0.6rem' }} />
          </Box>
          <Box display="flex" gap={0.3} flexWrap="wrap">
            <Chip label={guild.type} size="small" color={getGuildTypeColor(guild.type)} sx={{ height: 14, fontSize: '0.55rem' }} />
            {guild.region && <Chip label={guild.region} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />}
          </Box>
          <Divider sx={{ my: 0.5 }} />
          <Box display="flex" justifyContent="space-between">
            <Box display="flex" alignItems="center" gap={0.3}>
              <PeopleIcon sx={{ fontSize: '0.8rem', color: 'text.secondary' }} />
              <Typography variant="caption" fontSize="0.65rem">
                {guild.member_count || 0}
              </Typography>
            </Box>
            <Box display="flex" alignItems="center" gap={0.3}>
              <TrendingUpIcon sx={{ fontSize: '0.8rem', color: 'success.main' }} />
              <Typography variant="caption" fontSize="0.65rem" color="success.main">
                {guild.treasury_balance?.toLocaleString() || 0} €$
              </Typography>
            </Box>
          </Box>
          <Typography variant="caption" fontSize="0.6rem" color="text.secondary">
            Репутация: {guild.reputation_score || 0}
          </Typography>
        </Stack>
      </CardContent>
    </Card>
  )
}

