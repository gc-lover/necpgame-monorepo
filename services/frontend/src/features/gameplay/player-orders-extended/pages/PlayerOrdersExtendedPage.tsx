/**
 * PlayerOrdersExtendedPage - страница заказов игроков
 * 
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React, { useMemo, useState } from 'react'
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
  Chip,
} from '@mui/material'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import AssignmentIcon from '@mui/icons-material/Assignment'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { PlayerOrderCard } from '../components/PlayerOrderCard'
import { OrderBoardCard } from '../components/OrderBoardCard'
import { OrderDetailCard } from '../components/OrderDetailCard'
import { OrderEconomyCard } from '../components/OrderEconomyCard'
import { OrderReputationCard } from '../components/OrderReputationCard'
import { OrderActionsCard } from '../components/OrderActionsCard'

const orderTypes = ['ALL', 'CRAFTING', 'GATHERING', 'COMBAT_ASSISTANCE', 'TRANSPORTATION', 'SERVICE'] as const
const orderDifficulties = ['ALL', 'EASY', 'MEDIUM', 'HARD', 'EXPERT'] as const

const demoOrders = [
  {
    orderId: 'order-neo-001',
    title: 'Craft smart sniper rifle',
    type: 'CRAFTING' as const,
    difficulty: 'EXPERT' as const,
    payment: 62000,
    reputationRequired: 140,
    expiresInHours: 16,
    successRate: 54,
    customer: 'Fixer Rogue',
    status: 'ESCROW' as const,
    escrow: 18000,
    reputationImpact: 18,
    deliverables: ['Smart sniper rifle Mk.IV', 'Proof of calibration'],
    requirements: [
      { label: 'Skill', value: 'Engineering 9+' },
      { label: 'Materials', value: 'Smart core x1, Carbon fiber x6' },
    ],
  },
  {
    orderId: 'order-neo-002',
    title: 'Escort convoy through Badlands',
    type: 'COMBAT_ASSISTANCE' as const,
    difficulty: 'HARD' as const,
    payment: 28000,
    reputationRequired: 90,
    expiresInHours: 6,
    successRate: 68,
    customer: 'Nomad Clan Aldecaldo',
    status: 'OPEN' as const,
    escrow: 9000,
    reputationImpact: 12,
    deliverables: ['Safe convoy arrival', 'Combat report'],
    requirements: [
      { label: 'Squad size', value: '3+' },
      { label: 'Vehicle', value: 'Armored car or better' },
    ],
  },
  {
    orderId: 'order-neo-003',
    title: 'Gather neon mushrooms',
    type: 'GATHERING' as const,
    difficulty: 'MEDIUM' as const,
    payment: 8200,
    reputationRequired: 40,
    expiresInHours: 4,
    successRate: 84,
    customer: 'Doc Chrome',
    status: 'IN_PROGRESS' as const,
    escrow: 2600,
    reputationImpact: 6,
    deliverables: ['Neon mushrooms x20', 'Environmental scan log'],
    requirements: [
      { label: 'Biology kit', value: 'Tier 2+' },
      { label: 'Stealth', value: 'Reputation ≥ 30' },
    ],
  },
]

const demoEconomy = {
  totalVolume: 412000,
  escrowLocked: 136000,
  averageFee: 6.8,
  premiumOrders: 32,
  recurringOrders: 21,
  marketDemand: 74,
}

const demoReputation = {
  executorId: 'exec-omega',
  name: 'Taskforce Omega',
  tier: 'PLATINUM' as const,
  score: 910,
  completedOrders: 214,
  cancelRate: 2,
  reviewsPositive: 188,
  reviewsNegative: 3,
}

export const PlayerOrdersExtendedPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [typeFilter, setTypeFilter] = useState<(typeof orderTypes)[number]>('ALL')
  const [difficultyFilter, setDifficultyFilter] = useState<(typeof orderDifficulties)[number]>('ALL')

  const filteredOrders = useMemo(
    () =>
      demoOrders.filter(
        (order) =>
          (typeFilter === 'ALL' || order.type === typeFilter) &&
          (difficultyFilter === 'ALL' || order.difficulty === difficultyFilter),
      ),
    [typeFilter, difficultyFilter],
  )

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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="primary">
        Player Orders Extended
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Board · Economy · Reputation · Escrow
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Filters
      </Typography>
      <TextField
        select
        size="small"
        label="Order type"
        value={typeFilter}
        onChange={(event) => setTypeFilter(event.target.value as typeof orderTypes[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {orderTypes.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        size="small"
        label="Difficulty"
        value={difficultyFilter}
        onChange={(event) => setDifficultyFilter(event.target.value as typeof orderDifficulties[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {orderDifficulties.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <OrderActionsCard />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Mechanics
      </Typography>
      <Stack spacing={0.3}>
        {['Escrow safety', 'NPC delegation', 'Bulk & recurring orders', 'Market-driven pricing'].map((item) => (
          <Typography key={item} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {item}
          </Typography>
        ))}
      </Stack>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <OrderEconomyCard economy={demoEconomy} />
      <OrderReputationCard reputation={demoReputation} />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Premium orders активны. Поддерживай репутацию ≥ 80 для доступа к PLATINUM tier.
      </Alert>
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <AssignmentIcon sx={{ fontSize: '1.4rem', color: 'primary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Player Order Command Center
        </Typography>
        <Chip label={`Orders: ${filteredOrders.length}`} size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Управляй заказами: CRAFTING/GATHERING/COMBAT/TRANSPORT/SERVICE, escrow, репутация, NPC исполнители и экономика рынка. Всё на одном экране.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <OrderBoardCard title="Highlighted Orders" orders={demoOrders} />
        </Grid>
        <Grid item xs={12} md={6}>
          {filteredOrders[0] && <OrderDetailCard order={filteredOrders[0]} />}
        </Grid>
      </Grid>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Active Orders
      </Typography>
      <Grid container spacing={1}>
        {filteredOrders.map((order) => (
          <Grid key={order.orderId} item xs={12} md={6} lg={4}>
            <PlayerOrderCard order={order} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default PlayerOrdersExtendedPage

