import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import WarningIcon from '@mui/icons-material/Warning'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface RiskFactor {
  name: string
  probability: 'LOW' | 'MEDIUM' | 'HIGH'
  mitigation: string
}

const probabilityColor: Record<RiskFactor['probability'], string> = {
  LOW: '#05ffa1',
  MEDIUM: '#fef86c',
  HIGH: '#ff2a6d',
}

export interface RiskMatrixCardProps {
  risks: RiskFactor[]
}

export const RiskMatrixCard: React.FC<RiskMatrixCardProps> = ({ risks }) => (
  <CompactCard color="pink" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <WarningIcon sx={{ fontSize: '1rem', color: 'error.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Risk Matrix
        </Typography>
      </Box>
      <Stack spacing={0.3}>
        {risks.map((risk) => (
          <Box key={risk.name} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                {risk.name}
              </Typography>
              <Typography
                variant="caption"
                fontSize={cyberpunkTokens.fonts.xs}
                sx={{ color: probabilityColor[risk.probability] }}
              >
                {risk.probability}
              </Typography>
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Mitigation: {risk.mitigation}
            </Typography>
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default RiskMatrixCard


