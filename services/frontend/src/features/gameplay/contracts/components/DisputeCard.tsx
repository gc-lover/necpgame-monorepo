import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import BalanceIcon from '@mui/icons-material/Balance'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface DisputeSummary {
  disputeId: string
  contractId: string
  status: 'OPEN' | 'IN_REVIEW' | 'RESOLVED'
  filedBy: string
  assignedArbiter: string
  resolution?: string
  evidenceCount: number
}

const statusColor: Record<DisputeSummary['status'], string> = {
  OPEN: '#ff2a6d',
  IN_REVIEW: '#fef86c',
  RESOLVED: '#05ffa1',
}

export interface DisputeCardProps {
  dispute: DisputeSummary
}

export const DisputeCard: React.FC<DisputeCardProps> = ({ dispute }) => (
  <CompactCard color="pink" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <BalanceIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Dispute #{dispute.disputeId.slice(0, 6)}
          </Typography>
        </Box>
        <Chip
          label={dispute.status}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            bgcolor: 'rgba(255,255,255,0.05)',
            color: statusColor[dispute.status],
            border: `1px solid ${statusColor[dispute.status]}`,
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Contract: {dispute.contractId.slice(0, 8)} · Evidence: {dispute.evidenceCount}
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Filed by: {dispute.filedBy.slice(0, 8)} · Arbiter: {dispute.assignedArbiter.slice(0, 8)}
      </Typography>
      {dispute.resolution && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="success.main">
          Resolution: {dispute.resolution}
        </Typography>
      )}
    </Stack>
  </CompactCard>
)

export default DisputeCard


