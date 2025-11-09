/**
 * ModerationPage — центр модерации (админ)
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
import GavelIcon from '@mui/icons-material/Gavel'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ModerationQueueCard } from '../components/ModerationQueueCard'
import { SanctionStatsCard } from '../components/SanctionStatsCard'
import { ModeratorPerformanceCard } from '../components/ModeratorPerformanceCard'

interface FilterState {
  status: 'ALL' | 'NEW' | 'IN_PROGRESS' | 'ESCALATED'
  category: 'ALL' | 'CHAT' | 'VOICE' | 'GAMEPLAY' | 'ECONOMY'
}

const statusOptions: FilterState['status'][] = ['ALL', 'NEW', 'IN_PROGRESS', 'ESCALATED']
const categoryOptions: FilterState['category'][] = ['ALL', 'CHAT', 'VOICE', 'GAMEPLAY', 'ECONOMY']

const demoCases = [
  {
    caseId: 'case-AC4412',
    category: 'CHAT' as const,
    status: 'IN_PROGRESS' as const,
    reportedAt: '4m ago',
    assignee: 'Mod-Kira',
  },
  {
    caseId: 'case-AC4408',
    category: 'ECONOMY' as const,
    status: 'ESCALATED' as const,
    reportedAt: '15m ago',
    assignee: 'Mod-Jay',
  },
]

const demoSanctions = {
  warnings: 56,
  temporaryBans: 22,
  permanentBans: 4,
  reinstated: 6,
}

const demoModerators = [
  { moderatorId: 'Mod-Kira', handledCases: 38, slaCompliancePercent: 94, averageResolutionTime: '16m' },
  { moderatorId: 'Mod-Jay', handledCases: 25, slaCompliancePercent: 88, averageResolutionTime: '22m' },
]

export const ModerationPage: React.FC = () => {
  const navigate = useNavigate()
  const [filters, setFilters] = useState<FilterState>({ status: 'ALL', category: 'ALL' })

  const filteredCases = demoCases.filter((item) => {
    const statusMatch = filters.status === 'ALL' || item.status === filters.status
    const categoryMatch = filters.category === 'ALL' || item.category === filters.category
    return statusMatch && categoryMatch
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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="primary.main">
        Moderation Desk
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Обработка репортов, санкции и SLA.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры кейсов
      </Typography>
      <TextField
        select
        size="small"
        label="Status"
        value={filters.status}
        onChange={(event) => setFilters((prev) => ({ ...prev, status: event.target.value as FilterState['status'] }))}
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
        size="small"
        label="Category"
        value={filters.category}
        onChange={(event) => setFilters((prev) => ({ ...prev, category: event.target.value as FilterState['category'] }))}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {categoryOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Назначить кейс
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Создать санкцию
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Экспорт отчета
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <SanctionStatsCard metrics={demoSanctions} />
      <ModeratorPerformanceCard performance={demoModerators} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <GavelIcon sx={{ fontSize: '1.4rem', color: 'primary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Moderation Control Center
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Управление репортами, санкциями и производительностью модераторов. SLA и очереди на одном экране.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12}>
          <ModerationQueueCard cases={filteredCases} />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default ModerationPage


