import React from 'react'
import { Typography, Stack, Box, Chip, Divider } from '@mui/material'
import GavelIcon from '@mui/icons-material/Gavel'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ContractParticipant {
  characterId: string
  role: 'CREATOR' | 'ACCEPTOR' | 'WITNESS'
  reputation: number
}

export interface ContractSummary {
  contractId: string
  type: 'EXCHANGE' | 'SERVICE' | 'COURIER' | 'AUCTION'
  status: 'DRAFT' | 'PENDING' | 'ACTIVE' | 'COMPLETED' | 'CANCELLED' | 'DISPUTED'
  title: string
  collateral: number
  escrowHeld: number
  expiresIn: string
  participants: ContractParticipant[]
}

const statusColor: Record<ContractSummary['status'], string> = {
  DRAFT: '#9ba3c7',
  PENDING: '#00f7ff',
  ACTIVE: '#05ffa1',
  COMPLETED: '#8be47b',
  CANCELLED: '#ff2a6d',
  DISPUTED: '#fef86c',
}

export interface ContractCardProps {
  contract: ContractSummary
}

export const ContractCard: React.FC<ContractCardProps> = ({ contract }) => (
  <CompactCard color="cyan" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <GavelIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {contract.title}
          </Typography>
        </Box>
        <Chip
          label={contract.status}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            bgcolor: 'rgba(255,255,255,0.06)',
            color: statusColor[contract.status],
            border: `1px solid ${statusColor[contract.status]}`,
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        #{contract.contractId.slice(0, 8)} · Type: {contract.type}
      </Typography>
      <Divider sx={{ my: 0.4 }} />
      <Box display="flex" flexWrap="wrap" gap={0.4}>
        <Chip
          label={`Collateral ${contract.collateral}¥`}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
        <Chip
          label={`Escrow ${contract.escrowHeld}¥`}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
        <Chip
          label={`Expires ${contract.expiresIn}`}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Stack spacing={0.2}>
        {contract.participants.map((participant) => (
          <Typography
            key={participant.characterId}
            variant="caption"
            fontSize={cyberpunkTokens.fonts.xs}
            color="text.secondary"
          >
            {participant.role}: {participant.characterId.slice(0, 6)} · REP {participant.reputation}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ContractCard


