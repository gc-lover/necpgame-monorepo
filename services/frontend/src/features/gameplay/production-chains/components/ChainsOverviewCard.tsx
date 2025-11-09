import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import HubIcon from '@mui/icons-material/Hub'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ChainsOverviewItem {
  chainId: string
  name: string
  category: string
  stages: number
  cycleTime: string
  status?: 'optimal' | 'bottleneck' | 'expanding'
}

export interface ChainsOverviewCardProps {
  chains: ChainsOverviewItem[]
}

const statusColor: Record<NonNullable<ChainsOverviewItem['status']>, string> = {
  optimal: '#05ffa1',
  bottleneck: '#ff2a6d',
  expanding: '#00f7ff',
}

export const ChainsOverviewCard: React.FC<ChainsOverviewCardProps> = ({ chains }) => (
  <CompactCard color="cyan" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <HubIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Production Chains
          </Typography>
        </Box>
        <Chip
          label={chains.length}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Stack spacing={0.3}>
        {chains.map((chain) => (
          <Box key={chain.chainId} display="flex" flexDirection="column" gap={0.15}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                {chain.name}
              </Typography>
              <Chip
                label={chain.category}
                size="small"
                sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
              />
            </Box>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                {chain.stages} stages · Cycle: {chain.cycleTime}
              </Typography>
              {chain.status && (
                <Typography
                  variant="caption"
                  fontSize={cyberpunkTokens.fonts.xs}
                  sx={{ color: statusColor[chain.status] }}
                >
                  {chain.status}
                </Typography>
              )}
            </Box>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ChainsOverviewCard


