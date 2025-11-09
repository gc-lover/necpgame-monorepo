import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import PolicyIcon from '@mui/icons-material/Policy'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { SessionPolicy } from '../types'

export interface SessionPoliciesCardProps {
  policies: SessionPolicy[]
}

export const SessionPoliciesCard: React.FC<SessionPoliciesCardProps> = ({ policies }) => (
  <CompactCard color='green' glowIntensity='weak' compact>
    <Stack spacing={0.5}>
      <Box display='flex' alignItems='center' gap={0.6}>
        <PolicyIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant='caption' fontSize={cyberpunkTokens.fonts.sm} fontWeight='bold'>
          Session Policies
        </Typography>
      </Box>
      {policies.slice(0, 4).map((policy) => (
        <Box key={policy.name} display='flex' flexDirection='column' gap={0.1}>
          <Typography variant='caption' fontSize={cyberpunkTokens.fonts.xs} color='text.secondary'>
            {policy.name}: {policy.value}
          </Typography>
          <Typography variant='caption' fontSize={cyberpunkTokens.fonts.xs} color='text.secondary'>
            {policy.description}
          </Typography>
        </Box>
      ))}
      {policies.length === 0 && (
        <Typography variant='caption' fontSize={cyberpunkTokens.fonts.xs} color='text.secondary'>
          Политики не найдены.
        </Typography>
      )}
    </Stack>
  </CompactCard>
)

export default SessionPoliciesCard


