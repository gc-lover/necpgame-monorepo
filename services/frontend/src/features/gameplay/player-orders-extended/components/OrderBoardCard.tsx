import React from 'react'
import { Typography, Stack, Box, Chip, Divider } from '@mui/material'
import AssignmentIcon from '@mui/icons-material/Assignment'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface OrderBoardEntry {
  orderId: string
  title: string
  type: 'CRAFTING' | 'GATHERING' | 'COMBAT_ASSISTANCE' | 'TRANSPORTATION' | 'SERVICE'
  difficulty: 'EASY' | 'MEDIUM' | 'HARD' | 'EXPERT'
  payment: number
  reputationRequired: number
  expiresInHours: number
  successRate: number
}

const typeColor: Record<OrderBoardEntry['type'], 'cyan' | 'green' | 'pink' | 'yellow' | 'purple'> = {
  CRAFTING: 'cyan',
  GATHERING: 'green',
  COMBAT_ASSISTANCE: 'pink',
  TRANSPORTATION: 'yellow',
  SERVICE: 'purple',
}

export interface OrderBoardCardProps {
  title: string
  orders: OrderBoardEntry[]
}

export const OrderBoardCard: React.FC<OrderBoardCardProps> = ({ title, orders }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <AssignmentIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {title}
        </Typography>
      </Box>
      <Divider sx={{ borderColor: 'rgba(255,255,255,0.08)' }} />
      <Stack spacing={0.4}>
        {orders.slice(0, 4).map((order) => (
          <Box key={order.orderId} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight={600}>
                {order.title}
              </Typography>
              <Chip
                label={order.type}
                size="small"
                color={typeColor[order.type]}
                sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
              />
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Pay: {order.payment}¥ · Rep {order.reputationRequired} · Expires {order.expiresInHours}h
            </Typography>
            <ProgressBar value={order.successRate} compact color="cyan" customText={`${order.successRate}% success`} />
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default OrderBoardCard


