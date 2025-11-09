import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import QueueIcon from '@mui/icons-material/Queue'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface OperationQueueEntry {
  opId: string
  type: string
  component: string
  retries: number
  etaMs: number
}

export interface OperationQueueCardProps {
  queueSize: number
  throughputPerMinute: number
  backlogMinutes: number
  operations: OperationQueueEntry[]
}

export const OperationQueueCard: React.FC<OperationQueueCardProps> = ({ queueSize, throughputPerMinute, backlogMinutes, operations }) => (
  <CompactCard color="yellow" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <QueueIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Mutation Queue
        </Typography>
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Queue size: {queueSize} · Throughput: {throughputPerMinute}/min · Backlog: {backlogMinutes}m
      </Typography>
      <ProgressBar
        value={Math.min(100, (queueSize / Math.max(throughputPerMinute * 5, 1)) * 100)}
        label="Load"
        color="yellow"
        compact
      />
      <Stack spacing={0.2}>
        {operations.slice(0, 3).map((op) => (
          <Typography key={op.opId} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {op.type} ({op.component}) — retries {op.retries}, eta {op.etaMs}ms
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default OperationQueueCard


