/**
 * InvestmentCard - карточка инвестиции
 * 
 * ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/! ⭐
 */

import React from 'react'
import { Typography, Stack, Chip, Box, Divider } from '@mui/material'
import TrendingUpIcon from '@mui/icons-material/TrendingUp'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface InvestmentOpportunitySummary {
  opportunityId: string
  name: string
  type: 'CORPORATE' | 'FACTION' | 'REGIONAL' | 'REAL_ESTATE' | 'PRODUCTION_CHAINS'
  riskLevel: 'LOW' | 'MEDIUM' | 'HIGH' | 'VERY_HIGH'
  expectedRoi: number
  minInvestment: number
  maxInvestment: number
  fundedPercent: number
  dividends?: string
}

export interface InvestmentCardProps {
  investment: InvestmentOpportunitySummary
}

const riskColor: Record<InvestmentOpportunitySummary['riskLevel'], string> = {
  LOW: '#05ffa1',
  MEDIUM: '#00f7ff',
  HIGH: '#fef86c',
  VERY_HIGH: '#ff2a6d',
}

export const InvestmentCard: React.FC<InvestmentCardProps> = ({ investment }) => (
  <CompactCard color="cyan" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <TrendingUpIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {investment.name}
          </Typography>
        </Box>
        <Chip
          label={`${investment.expectedRoi}% ROI`}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            bgcolor: 'rgba(5,255,161,0.15)',
            border: '1px solid rgba(5,255,161,0.4)',
            color: '#05ffa1',
          }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        #{investment.opportunityId.slice(0, 8)} · Type: {investment.type}
      </Typography>
      <Divider sx={{ my: 0.3 }} />
      <Box display="flex" gap={0.4} flexWrap="wrap">
        <Chip
          label={investment.type}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
        <Chip
          label={investment.riskLevel}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            bgcolor: 'rgba(255,255,255,0.05)',
            border: `1px solid ${riskColor[investment.riskLevel]}`,
            color: riskColor[investment.riskLevel],
          }}
        />
        <Chip
          label={`Min ${investment.minInvestment}¥`}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
        <Chip
          label={`Max ${investment.maxInvestment}¥`}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <ProgressBar value={investment.fundedPercent} compact color="green" />
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Funded {investment.fundedPercent}%
        </Typography>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {investment.dividends ?? 'Dividends: TBA'}
        </Typography>
      </Box>
    </Stack>
  </CompactCard>
)

