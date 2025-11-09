import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import DescriptionIcon from '@mui/icons-material/Description'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface HiringContractSummary {
  contractId: string
  npcName: string
  termDays: number
  dailyRate: number
  signingBonus: number
  clauses: string[]
  risk: 'LOW' | 'MEDIUM' | 'HIGH'
}

const riskColor: Record<HiringContractSummary['risk'], string> = {
  LOW: '#05ffa1',
  MEDIUM: '#fef86c',
  HIGH: '#ff2a6d',
}

export interface HiringContractCardProps {
  contract: HiringContractSummary
}

export const HiringContractCard: React.FC<HiringContractCardProps> = ({ contract }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <DescriptionIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Contract — {contract.npcName}
          </Typography>
        </Box>
        <Chip
          label={`${contract.termDays} days`}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Daily rate: {contract.dailyRate}¥ · Signing bonus: {contract.signingBonus}¥
      </Typography>
      <Chip
        label={`${contract.risk} risk`}
        size="small"
        sx={{
          alignSelf: 'flex-start',
          height: 16,
          fontSize: cyberpunkTokens.fonts.xs,
          border: `1px solid ${riskColor[contract.risk]}`,
          color: riskColor[contract.risk],
        }}
      />
      <Stack spacing={0.2}>
        {contract.clauses.slice(0, 3).map((clause) => (
          <Typography key={clause} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {clause}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default HiringContractCard


