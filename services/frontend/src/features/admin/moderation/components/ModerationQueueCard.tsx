import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import AssignmentIcon from '@mui/icons-material/Assignment'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ModerationQueueItem {
  caseId: string
  category: 'CHAT' | 'VOICE' | 'GAMEPLAY' | 'ECONOMY'
  status: 'NEW' | 'IN_PROGRESS' | 'ESCALATED'
  reportedAt: string
  assignee?: string
}

const statusColor: Record<ModerationQueueItem['status'], string> = {
  NEW: '#00f7ff',
  IN_PROGRESS: '#fef86c',
  ESCALATED: '#ff2a6d',
}

export interface ModerationQueueCardProps {
  cases: ModerationQueueItem[]
}

export const ModerationQueueCard: React.FC<ModerationQueueCardProps> = ({ cases }) => (
  <CompactCard color="cyan" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <AssignmentIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Moderation Queue
          </Typography>
        </Box>
        <Chip
          label={cases.length}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Stack spacing={0.3}>
        {cases.map((item) => (
          <Box key={item.caseId} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                #{item.caseId.slice(0, 6)} · {item.category}
              </Typography>
              <Typography
                variant="caption"
                fontSize={cyberpunkTokens.fonts.xs}
                sx={{ color: statusColor[item.status] }}
              >
                {item.status}
              </Typography>
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Reported: {item.reportedAt} {item.assignee ? `· ${item.assignee}` : ''}
            </Typography>
          </Box>
        ))}
        {cases.length === 0 && (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Очередь пуста
          </Typography>
        )}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ModerationQueueCard


