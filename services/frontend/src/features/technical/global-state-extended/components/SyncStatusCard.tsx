import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import SyncIcon from '@mui/icons-material/Sync'
import { CompactCard } from '@/shared/ui/cards'
import { ProgressBar } from '@/shared/ui/stats/ProgressBar'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface SyncNodeSummary {
  node: string
  latencyMs: number
  lastAck: string
  driftMs: number
}

export interface SyncStatusCardProps {
  syncStatus: 'HEALTHY' | 'DEGRADED' | 'OUT_OF_SYNC'
  syncQueueSize: number
  successRate: number
  nodes: SyncNodeSummary[]
}

const statusColor: Record<SyncStatusCardProps['syncStatus'], string> = {
  HEALTHY: '#05ffa1',
  DEGRADED: '#fef86c',
  OUT_OF_SYNC: '#ff2a6d',
}

export const SyncStatusCard: React.FC<SyncStatusCardProps> = ({ syncStatus, syncQueueSize, successRate, nodes }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <SyncIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Sync Status
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} sx={{ color: statusColor[syncStatus] }}>
          {syncStatus}
        </Typography>
      </Box>
      <ProgressBar value={successRate} label="Success rate" color="green" compact />
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Queue size: {syncQueueSize}
      </Typography>
      <Stack spacing={0.2}>
        {nodes.slice(0, 3).map((node) => (
          <Typography key={node.node} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {node.node}: {node.latencyMs}ms latency · drift {node.driftMs}ms · ack {node.lastAck}
          </Typography>
        ))}
      </Stack>
    </Stack>
  </CompactCard>
)

export default SyncStatusCard


