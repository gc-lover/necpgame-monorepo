import React from 'react'
import { Typography, Stack, Box, Chip, LinearProgress } from '@mui/material'
import FactoryIcon from '@mui/icons-material/Factory'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ProductionJobSummary {
  jobId: string
  stage: string
  progressPercent: number
  timeLeft: string
  facility: string
  status: 'running' | 'queued' | 'paused'
}

export interface ProductionJobsCardProps {
  jobs: ProductionJobSummary[]
  maxConcurrent?: number
}

const statusColor: Record<ProductionJobSummary['status'], string> = {
  running: '#05ffa1',
  queued: '#00f7ff',
  paused: '#ff2a6d',
}

export const ProductionJobsCard: React.FC<ProductionJobsCardProps> = ({ jobs, maxConcurrent }) => (
  <CompactCard color="green" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <FactoryIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Active Jobs
          </Typography>
        </Box>
        {typeof maxConcurrent === 'number' && (
          <Chip
            label={`${jobs.length}/${maxConcurrent}`}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        )}
      </Box>
      <Stack spacing={0.3}>
        {jobs.map((job) => (
          <Box key={job.jobId} display="flex" flexDirection="column" gap={0.2}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                {job.stage}
              </Typography>
              <Typography
                variant="caption"
                fontSize={cyberpunkTokens.fonts.xs}
                sx={{ color: statusColor[job.status] }}
              >
                {job.status}
              </Typography>
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Facility: {job.facility} · {job.timeLeft} left
            </Typography>
            <LinearProgress
              variant="determinate"
              value={Math.min(Math.max(job.progressPercent, 0), 100)}
              sx={{ height: 4, borderRadius: 1, bgcolor: 'rgba(255,255,255,0.08)' }}
            />
          </Box>
        ))}
        {jobs.length === 0 && (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Производства не запущены
          </Typography>
        )}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ProductionJobsCard


