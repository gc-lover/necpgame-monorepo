import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import ShieldIcon from '@mui/icons-material/Shield'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface InsurancePlan {
  name: string
  coverage: string
  premium: string
  perks?: string[]
}

export interface InsurancePlansCardProps {
  plans: InsurancePlan[]
}

export const InsurancePlansCard: React.FC<InsurancePlansCardProps> = ({ plans }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <ShieldIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Cargo Insurance
        </Typography>
      </Box>
      <Stack spacing={0.3}>
        {plans.map((plan) => (
          <Box key={plan.name} display="flex" flexDirection="column" gap={0.1}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
              {plan.name} — Coverage: {plan.coverage}
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Premium: {plan.premium}
            </Typography>
            {plan.perks && plan.perks.length > 0 && (
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                {plan.perks.map((perk) => `• ${perk}`).join(' | ')}
              </Typography>
            )}
          </Box>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default InsurancePlansCard


