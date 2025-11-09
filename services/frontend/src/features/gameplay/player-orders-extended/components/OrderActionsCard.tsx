import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import BoltIcon from '@mui/icons-material/Bolt'
import { CompactCard } from '@/shared/ui/cards'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface OrderActionsCardProps {
  onCreateOrder?: () => void
  onViewContracts?: () => void
  onOpenEscrow?: () => void
}

export const OrderActionsCard: React.FC<OrderActionsCardProps> = ({ onCreateOrder, onViewContracts, onOpenEscrow }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <BoltIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Order Actions
        </Typography>
      </Box>
      <CyberpunkButton variant="primary" size="small" fullWidth onClick={onCreateOrder}>
        Создать заказ
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth onClick={onViewContracts}>
        Управление контрактами
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth onClick={onOpenEscrow}>
        Escrow операции
      </CyberpunkButton>
    </Stack>
  </CompactCard>
)

export default OrderActionsCard


