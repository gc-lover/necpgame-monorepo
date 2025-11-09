import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import ReportIcon from '@mui/icons-material/Report'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface ReportSummaryItem {
  reportId: string
  cheatType: string
  status: 'PENDING' | 'UNDER_REVIEW' | 'CONFIRMED' | 'DISMISSED'
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  lastSeen: string
}

const severityColor: Record<ReportSummaryItem['severity'], string> = {
  LOW: '#05ffa1',
  MEDIUM: '#00f7ff',
  HIGH: '#fef86c',
  CRITICAL: '#ff2a6d',
}

export interface ReportSummaryCardProps {
  reports: ReportSummaryItem[]
}

export const ReportSummaryCard: React.FC<ReportSummaryCardProps> = ({ reports }) => (
  <CompactCard color="cyan" glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <ReportIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Suspicious Reports
          </Typography>
        </Box>
        <Chip
          label={reports.length}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Stack spacing={0.3}>
        {reports.map((report) => (
          <Box key={report.reportId} display="flex" flexDirection="column" gap={0.1}>
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
                #{report.reportId.slice(0, 8)} · {report.cheatType}
              </Typography>
              <Typography
                variant="caption"
                fontSize={cyberpunkTokens.fonts.xs}
                sx={{ color: severityColor[report.severity] }}
              >
                {report.severity}
              </Typography>
            </Box>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Status: {report.status} · Last seen {report.lastSeen}
            </Typography>
          </Box>
        ))}
        {reports.length === 0 && (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Репортов нет
          </Typography>
        )}
      </Stack>
    </Stack>
  </CompactCard>
)

export default ReportSummaryCard


