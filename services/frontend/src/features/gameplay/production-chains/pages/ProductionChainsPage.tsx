/**
 * ProductionChainsPage - управление производственными цепочками
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
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
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import HubIcon from '@mui/icons-material/Hub'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { ChainsOverviewCard } from '../components/ChainsOverviewCard'
import { ChainStagesCard } from '../components/ChainStagesCard'
import { ProductionJobsCard } from '../components/ProductionJobsCard'
import { ProfitabilityCard } from '../components/ProfitabilityCard'
import { OptimizationCard } from '../components/OptimizationCard'
import { useGameState } from '@/features/game/hooks/useGameState'

const categories = ['WEAPONS', 'ARMOR', 'IMPLANTS']

const demoChains = [
  {
    chainId: 'chain-legendary-weapons',
    name: 'Legendary Weapons',
    category: 'WEAPONS',
    stages: 5,
    cycleTime: '4h',
    status: 'optimal' as const,
  },
  {
    chainId: 'chain-combat-implants',
    name: 'Combat Implants',
    category: 'IMPLANTS',
    stages: 4,
    cycleTime: '3h',
    status: 'bottleneck' as const,
  },
]

const demoStages = [
  { stageId: 'stage-ore', name: 'Smelt Ingots', duration: '30m', inputs: 'Chromium Ore x20', outputs: 'Ingots x10' },
  { stageId: 'stage-components', name: 'Assemble Components', duration: '45m', inputs: 'Ingots x10', outputs: 'Weapon Parts x4' },
  { stageId: 'stage-assembly', name: 'Weapon Assembly', duration: '60m', inputs: 'Weapon Parts x4', outputs: 'Rare Weapons x2' },
  { stageId: 'stage-enhance', name: 'Legendary Enhancement', duration: '90m', inputs: 'Rare Weapons x2 + Legendary Core', outputs: 'Legendary Weapon x1' },
]

const demoJobs = [
  {
    jobId: 'job-123',
    stage: 'Legendary Enhancement',
    progressPercent: 68,
    timeLeft: '55m',
    facility: 'Night City Forge T3',
    status: 'running' as const,
  },
  {
    jobId: 'job-124',
    stage: 'Weapon Assembly',
    progressPercent: 22,
    timeLeft: '2h 10m',
    facility: 'Watson Assembly Line',
    status: 'queued' as const,
  },
]

export const ProductionChainsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [category, setCategory] = useState('WEAPONS')

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
      {selectedCharacterId && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Character: {selectedCharacterId}
        </Typography>
      )}
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info">
        Production Chains
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Weapon / Armor / Implant pipelines
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтр цепочек
      </Typography>
      <TextField
        select
        size="small"
        label="Category"
        value={category}
        onChange={(event) => setCategory(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {categories.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Запустить производство
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Оптимизировать AI
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Советы производству
      </Typography>
      <Divider />
      <Stack spacing={0.3}>
        {[
          'Следите за bottleneck стадиями и повышайте уровень facility',
          'Используйте AI optimization перед крупными партиями',
          'Profitability > 25% → рассмотреть расширение мощностей',
          'Bulk production снижает затраты на 12% в среднем',
        ].map((tip) => (
          <Typography key={tip} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {tip}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <HubIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Production Chains Dashboard
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Полный обзор производственных цепочек: стадии, активные задания, прибыльность и AI-оптимизация.
        Управляйте supply chain так, чтобы легендарные предметы выходили вовремя.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <ChainsOverviewCard chains={demoChains.filter((chain) => chain.category === category || category === 'WEAPONS')} />
        </Grid>
        <Grid item xs={12} md={6}>
          <ChainStagesCard stages={demoStages} finalProduct="Legendary Weapon" />
        </Grid>
        <Grid item xs={12} md={6}>
          <ProductionJobsCard jobs={demoJobs} maxConcurrent={3} />
        </Grid>
        <Grid item xs={12} md={6}>
          <ProfitabilityCard
            chainName="Legendary Weapons"
            profitPerCycle={12500}
            roiPercent={32.5}
            cycleTime="4h"
            recommendations={["Upgrade smelter to T3", 'Add Night Market delivery slot']}
          />
        </Grid>
        <Grid item xs={12}>
          <OptimizationCard
            goal="Maximize profit per day"
            expectedImprovement="+18%"
            tips={[{ label: 'Schedule', value: 'Chain assembly at 02:00-05:00' }, { label: 'Resource', value: 'Bulk order Chromium Ore x500' }]}
          />
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default ProductionChainsPage

