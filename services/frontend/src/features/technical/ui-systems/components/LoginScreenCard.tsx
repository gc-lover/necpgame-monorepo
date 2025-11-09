import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import LockIcon from '@mui/icons-material/Lock'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface LoginScreenDataSummary {
  title: string
  subtitle: string
  callToAction: string
  background: string
  rotatingTips: string[]
}

export interface LoginScreenCardProps {
  data: LoginScreenDataSummary
}

export const LoginScreenCard: React.FC<LoginScreenCardProps> = ({ data }) => (
  <CompactCard color="cyan" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <LockIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          {data.title}
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {data.subtitle}
      </Typography>
      <Chip
        label={data.callToAction}
        size="small"
        color="info"
        sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
      />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Background: {data.background}
      </Typography>
      <Stack spacing={0.2}>
        {data.rotatingTips.slice(0, 3).map((tip) => (
          <Typography key={tip} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {tip}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default LoginScreenCard


