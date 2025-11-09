import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import GavelIcon from '@mui/icons-material/Gavel'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface AppealQueueItem {
  appealId: string
  banId: string
  submittedAt: string
  status: 'NEW' | 'IN_REVIEW' | 'RESOLVED'
}

const statusColor: Record<AppealQueueItem['status'], string> = {
  NEW: '#00f7ff',
  IN_REVIEW: '#fef86c',
  RESOLVED: '#05ffa1',
}

export interface AppealsQueueCardProps {
  appeals: AppealQueueItem[]
}

export const AppealsQueueCard: React.FC<AppealsQueueCardProps> = ({ appeals }) => (
  <CompactCard color="purple" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <GavelIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Appeals Queue
          </Typography>
        </Box>
        <Chip
          label={appeals.length}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Stack spacing={0.3}>
        {appeals.map((appeal) => (
          <Box key={appeal.appealId} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                Appeal #{appeal.appealId.slice(0, 6)} · Ban {appeal.banId.slice(0, 6)}
              </Typography>
              <Typography
                variant="caption"
                fontSize={cyberpunkTokens.fonts.xs}
                sx={{ color: statusColor[appeal.status] }}
              >
                {appeal.status}
              </Typography>
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Submitted: {appeal.submittedAt}
            </Typography>
          </Box>
        ))}
        {appeals.length === 0 && (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Апелляций нет
          </Typography>
        )}
      </Stack>
    </Stack>
  </CompactCard>
)

export default AppealsQueueCard


