import React from 'react'
import { Typography, Stack, Box, LinearProgress } from '@mui/material'
import AccountBalanceIcon from '@mui/icons-material/AccountBalance'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface EscrowStatusSummary {
  escrowId: string
  totalHeld: number
  released: number
  disputed: number
  releaseCondition: string
}

export interface EscrowStatusCardProps {
  status: EscrowStatusSummary
}

export const EscrowStatusCard: React.FC<EscrowStatusCardProps> = ({ status }) => {
  const releasePercent = Math.min(100, Math.round((status.released / status.totalHeld) * 100))
  const disputedPercent = Math.min(100, Math.round((status.disputed / status.totalHeld) * 100))

  return (
    <CompactCard color="yellow" glowIntensity="weak" compact>
      <Stack spacing={0.5}>
        <Box display="flex" alignItems="center" gap={0.6}>
          <AccountBalanceIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Escrow #{status.escrowId.slice(0, 6)}
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Held: {status.totalHeld}¥ · Released: {status.released}¥ · Disputed: {status.disputed}¥
        </Typography>
        <Stack spacing={0.2}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="success.main">
            Released {releasePercent}%
          </Typography>
          <LinearProgress
            variant="determinate"
            value={releasePercent}
            sx={{ height: 4, borderRadius: 1, bgcolor: 'rgba(255,255,255,0.1)' }}
          />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="error.main">
            Disputed {disputedPercent}%
          </Typography>
          <LinearProgress
            variant="determinate"
            value={disputedPercent}
            sx={{ height: 4, borderRadius: 1, bgcolor: 'rgba(255,255,255,0.1)' }}
          />
        </Stack>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Release condition: {status.releaseCondition}
        </Typography>
      </Stack>
    </CompactCard>
  )
}

export default EscrowStatusCard


