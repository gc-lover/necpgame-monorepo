/**
 * InvestmentsPage - страница инвестиций
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */
 
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Typography, Stack, Divider, Alert, Grid, Box, TextField, MenuItem } from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import AccountBalanceIcon from '@mui/icons-material/AccountBalance'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { InvestmentCard, InvestmentOpportunitySummary } from '../components/InvestmentCard'
import { PortfolioOverviewCard } from '../components/PortfolioOverviewCard'
import { RiskDistributionCard } from '../components/RiskDistributionCard'
import { DividendScheduleCard } from '../components/DividendScheduleCard'

const typeOptions: Array<'ALL' | InvestmentOpportunitySummary['type']> = [
  'ALL',
  'CORPORATE',
  'FACTION',
  'REGIONAL',
  'REAL_ESTATE',
  'PRODUCTION_CHAINS',
]

const riskOptions: Array<'ALL' | InvestmentOpportunitySummary['riskLevel']> = [
  'ALL',
  'LOW',
  'MEDIUM',
  'HIGH',
  'VERY_HIGH',
]

const demoOpportunities: InvestmentOpportunitySummary[] = [
  {
    opportunityId: 'inv-neoair-784',
    name: 'NeoAir Freight Expansion',
    type: 'PRODUCTION_CHAINS',
    riskLevel: 'MEDIUM',
    expectedRoi: 18,
    minInvestment: 12000,
    maxInvestment: 250000,
    fundedPercent: 64,
    dividends: 'Monthly 2.1%',
  },
  {
    opportunityId: 'inv-arasaka-retail',
    name: 'Arasaka Retail Bonds',
    type: 'CORPORATE',
    riskLevel: 'LOW',
    expectedRoi: 9,
    minInvestment: 5000,
    maxInvestment: 150000,
    fundedPercent: 82,
    dividends: 'Quarterly 1.8%',
  },
  {
    opportunityId: 'inv-badlands17',
    name: 'Badlands Solar Grid',
    type: 'REGIONAL',
    riskLevel: 'HIGH',
    expectedRoi: 26,
    minInvestment: 18000,
    maxInvestment: 400000,
    fundedPercent: 38,
    dividends: 'Yearly 6.5%',
  },
]

const demoPortfolio = {
  totalValue: 1_450_000,
  investedCapital: 1_100_000,
  unrealizedProfit: 350_000,
  roiPercent: 23.8,
}

const demoRiskDistribution = [
  { level: 'LOW', percent: 35 },
  { level: 'MEDIUM', percent: 40 },
  { level: 'HIGH', percent: 20 },
  { level: 'VERY_HIGH', percent: 5 },
]

const demoDividends = [
  { fundName: 'NeoAir Freight Expansion', payoutDate: '2077-11-20', expectedAmount: 6200, status: 'PLANNED' as const },
  { fundName: 'Arasaka Retail Bonds', payoutDate: '2077-12-05', expectedAmount: 3400, status: 'PLANNED' as const },
]

export const InvestmentsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [selectedType, setSelectedType] = useState<(typeof typeOptions)[number]>('ALL')
  const [selectedRisk, setSelectedRisk] = useState<(typeof riskOptions)[number]>('ALL')

  const filteredOpportunities = demoOpportunities.filter((item) => {
    const typeMatch = selectedType === 'ALL' || item.type === selectedType
    const riskMatch = selectedRisk === 'ALL' || item.riskLevel === selectedRisk
    return typeMatch && riskMatch
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
      {selectedCharacterId && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Character: {selectedCharacterId}
        </Typography>
      )}
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="success.main">
        Investment Hub
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Управляй портфелем, дивидендами и рисками. 5 типов инвестиций.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Filters
      </Typography>
      <TextField
        select
        label="Type"
        size="small"
        value={selectedType}
        onChange={(event) => setSelectedType(event.target.value as typeof selectedType)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {typeOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        label="Risk"
        size="small"
        value={selectedRisk}
        onChange={(event) => setSelectedRisk(event.target.value as typeof selectedRisk)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {riskOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Инвестировать
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Пополнить портфель
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Вывести средства
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <PortfolioOverviewCard portfolio={demoPortfolio} />
      <RiskDistributionCard distribution={demoRiskDistribution} />
      <DividendScheduleCard schedule={demoDividends} />
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <AccountBalanceIcon sx={{ fontSize: '1.4rem', color: 'success.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Investment Control Center
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Portfolio management, risk diversification, ROI analytics и дивиденды — полный контроль инвестиционной
        экосистемы Night City.
      </Alert>
      <Grid container spacing={1}>
        {filteredOpportunities.map((opportunity) => (
          <Grid item xs={12} md={6} lg={4} key={opportunity.opportunityId}>
            <InvestmentCard investment={opportunity} />
          </Grid>
        ))}
        {filteredOpportunities.length === 0 && (
          <Grid item xs={12}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Нет возможностей под выбранные фильтры
            </Typography>
          </Grid>
        )}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default InvestmentsPage

