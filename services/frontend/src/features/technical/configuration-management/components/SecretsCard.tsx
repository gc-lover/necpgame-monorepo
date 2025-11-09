import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import LockIcon from '@mui/icons-material/Lock'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { SecretMetadata } from '../types'

export interface SecretsCardProps {
  secrets: SecretMetadata[]
}

export const SecretsCard: React.FC<SecretsCardProps> = ({ secrets }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <LockIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Secrets overview
        </Typography>
      </Box>
      {secrets.slice(0, 4).map((secret) => (
        <Typography key={secret.secretName} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {secret.secretName} • created {secret.createdAt} • updated {secret.updatedAt}
        </Typography>
      ))}
      {secrets.length === 0 && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Секреты отсутствуют.
        </Typography>
      )}
    </Stack>
  </CompactCard>
)

export default SecretsCard


