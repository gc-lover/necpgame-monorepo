import React from 'react'
import { Typography, Stack, Box, LinearProgress } from '@mui/material'
import PersonIcon from '@mui/icons-material/Person'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ModeratorPerformance {
  moderatorId: string
  handledCases: number
  slaCompliancePercent: number
  averageResolutionTime: string
}

export interface ModeratorPerformanceCardProps {
  performance: ModeratorPerformance[]
}

export const ModeratorPerformanceCard: React.FC<ModeratorPerformanceCardProps> = ({ performance }) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" alignItems="center" gap={0.6}>
        <PersonIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
          Moderator Performance
        </Typography>
      </Box>
      <Stack spacing={0.3}>
        {performance.map((item) => (
          <Box key={item.moderatorId}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                {item.moderatorId}
              </Typography>
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                {item.handledCases} cases · {item.averageResolutionTime}
              </Typography>
            </Box>
            <LinearProgress
              variant="determinate"
              value={Math.min(100, item.slaCompliancePercent)}
              sx={{ height: 4, borderRadius: 1, mt: 0.2, bgcolor: 'rgba(255,255,255,0.1)' }}
            />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              SLA compliance: {item.slaCompliancePercent}%
            </Typography>
          </Box>
        ))}
        {performance.length === 0 && (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Нет данных по модераторам
          </Typography>
        )}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ModeratorPerformanceCard


