/**
 * AntiCheatPage — админский центр анти-чита
 */

import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import {
  Typography,
  Stack,
  Divider,
  Alert,
  Grid,
  Box,
  TextField,
  MenuItem,
} from '@mui/material'
import ShieldIcon from '@mui/icons-material/Shield'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ReportSummaryCard } from '../components/ReportSummaryCard'
import { BanOverviewCard } from '../components/BanOverviewCard'
import { AppealsQueueCard } from '../components/AppealsQueueCard'
import { DetectionStatsCard } from '../components/DetectionStatsCard'

interface ReportFilterState {
  status: 'ALL' | 'PENDING' | 'UNDER_REVIEW' | 'CONFIRMED' | 'DISMISSED'
  severity: 'ALL' | 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
}

const statusOptions: ReportFilterState['status'][] = ['ALL', 'PENDING', 'UNDER_REVIEW', 'CONFIRMED', 'DISMISSED']
const severityOptions: ReportFilterState['severity'][] = ['ALL', 'LOW', 'MEDIUM', 'HIGH', 'CRITICAL']

const demoReports = [
  {
    reportId: 'rep-20871',
    cheatType: 'Aimbot/ESP',
    status: 'PENDING' as const,
    severity: 'CRITICAL' as const,
    lastSeen: '2m ago',
  },
  {
    reportId: 'rep-20855',
    cheatType: 'Economy exploitation',
    status: 'UNDER_REVIEW' as const,
    severity: 'HIGH' as const,
    lastSeen: '12m ago',
  },
]

const demoBans = {
  active: 124,
  pendingAppeals: 11,
  autoBans: 78,
  manualBans: 46,
}

const demoAppeals = [
  {
    appealId: 'apl-0773',
    banId: 'ban-9921',
    submittedAt: '2077-11-07 18:55',
    status: 'IN_REVIEW' as const,
  },
  {
    appealId: 'apl-0771',
    banId: 'ban-9908',
    submittedAt: '2077-11-07 18:25',
    status: 'NEW' as const,
  },
]

const demoDetection = {
  autoBansLast24h: 32,
  suspiciousPatterns: 9,
  manualQueue: 14,
  falsePositivesRate: 1.42,
}

export const AntiCheatPage: React.FC = () => {
  const navigate = useNavigate()
  const [filters, setFilters] = useState<ReportFilterState>({ status: 'ALL', severity: 'ALL' })

  const filteredReports = demoReports.filter((report) => {
    const statusMatch = filters.status === 'ALL' || report.status === filters.status
    const severityMatch = filters.severity === 'ALL' || report.severity === filters.severity
    return statusMatch && severityMatch
  })

  const leftPanel = (
    <Stack spacing={2}>
      <CyberpunkButton
        variant="outlined"
        size="small"
        fullWidth
        startIcon={<ArrowBackIcon />}
        onClick={() => navigate('/game')}
      >
        Назад
      </CyberpunkButton>
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info.main">
        Anti-Cheat Ops
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Контроль читов, банов и апелляций.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры репортов
      </Typography>
      <TextField
        select
        label="Status"
        size="small"
        value={filters.status}
        onChange={(event) => setFilters((prev) => ({ ...prev, status: event.target.value as ReportFilterState['status'] }))}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {statusOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        label="Severity"
        size="small"
        value={filters.severity}
        onChange={(event) => setFilters((prev) => ({ ...prev, severity: event.target.value as ReportFilterState['severity'] }))}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {severityOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Создать репорт
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Назначить ревьюера
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Экспорт логов
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <DetectionStatsCard metrics={demoDetection} />
      <BanOverviewCard metrics={demoBans} />
      <AppealsQueueCard appeals={demoAppeals} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <ShieldIcon sx={{ fontSize: '1.4rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Anti-Cheat Command Center
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Pattern detection, auto-bans, manual reviews и апелляции. Все показатели анти-чита на одном экране.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12}>
          <ReportSummaryCard reports={filteredReports} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default AntiCheatPage


