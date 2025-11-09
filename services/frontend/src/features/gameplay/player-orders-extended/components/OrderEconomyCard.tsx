import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import MonetizationOnIcon from '@mui/icons-material/MonetizationOn'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface OrderEconomySummary {
  totalVolume: number
  escrowLocked: number
  averageFee: number
  premiumOrders: number
  recurringOrders: number
  marketDemand: number
}

export interface OrderEconomyCardProps {
  economy: OrderEconomySummary
}

export const OrderEconomyCard: React.FC<OrderEconomyCardProps> = ({ economy }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.4}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <MonetizationOnIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Order Economy
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Total volume: {economy.totalVolume.toLocaleString()}¥ · Escrow locked: {economy.escrowLocked.toLocaleString()}¥
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Avg fee: {economy.averageFee}% · Premium orders: {economy.premiumOrders}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Recurring orders: {economy.recurringOrders}
      </Typography>
      <ProgressBar value={economy.marketDemand} label="Market demand" color="green" compact />
    </Stack>
  </CompactCard>
)

export default OrderEconomyCard


