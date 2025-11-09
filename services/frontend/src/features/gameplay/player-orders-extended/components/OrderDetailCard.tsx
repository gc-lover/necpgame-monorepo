import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import InfoIcon from '@mui/icons-material/Info'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface OrderRequirement {
  label: string
  value: string
}

export interface OrderDetailSummary {
  orderId: string
  customer: string
  status: 'OPEN' | 'IN_PROGRESS' | 'ESCROW' | 'COMPLETED'
  escrow: number
  reputationImpact: number
  deliverables: string[]
  requirements: OrderRequirement[]
}

const statusColor: Record<OrderDetailSummary['status'], string> = {
  OPEN: '#05ffa1',
  IN_PROGRESS: '#00f7ff',
  ESCROW: '#fef86c',
  COMPLETED: '#d817ff',
}

export interface OrderDetailCardProps {
  order: OrderDetailSummary
}

export const OrderDetailCard: React.FC<OrderDetailCardProps> = ({ order }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <InfoIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Order #{order.orderId.slice(0, 8)}
          </Typography>
        </Box>
        <Chip
          label={order.status}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: `1px solid ${statusColor[order.status]}`,
            color: statusColor[order.status],
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Customer: {order.customer}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Escrow: {order.escrow}¥ · Reputation: {order.reputationImpact >= 0 ? '+' : ''}{order.reputationImpact}
      </Typography>
      <ProgressBar value={order.status === 'COMPLETED' ? 100 : order.status === 'ESCROW' ? 75 : order.status === 'IN_PROGRESS' ? 40 : 10} compact color="purple" />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary" fontWeight={600}>
        Deliverables
      </Typography>
      <Stack spacing={0.2}>
        {order.deliverables.map((item) => (
          <Typography key={item} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {item}
          </Typography>
        ))}
      </Stack>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary" fontWeight={600}>
        Requirements
      </Typography>
      <Stack spacing={0.1}>
        {order.requirements.map((req) => (
          <Typography key={req.label} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {req.label}: {req.value}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default OrderDetailCard


