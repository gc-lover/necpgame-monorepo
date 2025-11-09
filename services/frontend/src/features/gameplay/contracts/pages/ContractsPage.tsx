/**
 * ContractsPage — управление контрактами, escrow и спорами
 * ⭐ Использует shared GameLayout и библиотеку UI
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
import GavelIcon from '@mui/icons-material/Gavel'
import { GameLayout } from '@/shared/ui/layout'
import { CyberpunkButton } from '@/shared/ui/buttons'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'
import { useGameState } from '@/features/game/hooks/useGameState'
import { ContractCard } from '../components/ContractCard'
import { ContractTemplateCard } from '../components/ContractTemplateCard'
import { EscrowStatusCard } from '../components/EscrowStatusCard'
import { DisputeCard } from '../components/DisputeCard'

const contractTypes: Array<'ALL' | 'EXCHANGE' | 'SERVICE' | 'COURIER' | 'AUCTION'> = [
  'ALL',
  'EXCHANGE',
  'SERVICE',
  'COURIER',
  'AUCTION',
]

const contractStatuses: Array<'ALL' | 'DRAFT' | 'PENDING' | 'ACTIVE' | 'COMPLETED' | 'CANCELLED' | 'DISPUTED'> = [
  'ALL',
  'DRAFT',
  'PENDING',
  'ACTIVE',
  'COMPLETED',
  'CANCELLED',
  'DISPUTED',
]

const demoContracts = [
  {
    contractId: 'contract-AB9123',
    title: 'Night Market Supply Run',
    type: 'COURIER' as const,
    status: 'ACTIVE' as const,
    collateral: 5000,
    escrowHeld: 18000,
    expiresIn: '2h 45m',
    participants: [
      { characterId: 'char-VALKYRIE-1', role: 'CREATOR' as const, reputation: 78 },
      { characterId: 'char-COURIER-9', role: 'ACCEPTOR' as const, reputation: 65 },
    ],
  },
  {
    contractId: 'contract-HD7721',
    title: 'VIP Protection Service',
    type: 'SERVICE' as const,
    status: 'PENDING' as const,
    collateral: 12000,
    escrowHeld: 32000,
    expiresIn: '5h 10m',
    participants: [
      { characterId: 'char-NOMAD-3', role: 'CREATOR' as const, reputation: 84 },
      { characterId: 'char-MERC-7', role: 'ACCEPTOR' as const, reputation: 71 },
    ],
  },
]

const demoTemplates = [
  {
    templateId: 'tpl-COURIER-24H',
    name: 'Courier — 24h Delivery',
    type: 'COURIER' as const,
    recommendedCollateral: 4000,
    autoExecution: true,
    tags: ['delivery', 'timer'],
  },
  {
    templateId: 'tpl-SERVICE-SEC',
    name: 'Security Service Contract',
    type: 'SERVICE' as const,
    recommendedCollateral: 15000,
    autoExecution: false,
    tags: ['escort', 'night-shift'],
  },
]

const demoEscrow = {
  escrowId: 'escrow-7781',
  totalHeld: 52000,
  released: 18000,
  disputed: 4000,
  releaseCondition: 'Auto-release upon delivery confirmation or arbiter verdict',
}

const demoDisputes = [
  {
    disputeId: 'dispute-1002',
    contractId: 'contract-AB9123',
    status: 'IN_REVIEW' as const,
    filedBy: 'char-COURIER-9',
    assignedArbiter: 'arbiter-DELTA',
    evidenceCount: 4,
  },
]

export const ContractsPage: React.FC = () => {
  const navigate = useNavigate()
  const { selectedCharacterId } = useGameState()
  const [typeFilter, setTypeFilter] = useState<'ALL' | 'EXCHANGE' | 'SERVICE' | 'COURIER' | 'AUCTION'>('ALL')
  const [statusFilter, setStatusFilter] = useState<
    'ALL' | 'DRAFT' | 'PENDING' | 'ACTIVE' | 'COMPLETED' | 'CANCELLED' | 'DISPUTED'
  >('ALL')

  const filteredContracts = demoContracts.filter((contract) => {
    const typeMatch = typeFilter === 'ALL' || contract.type === typeFilter
    const statusMatch = statusFilter === 'ALL' || contract.status === statusFilter
    return typeMatch && statusMatch
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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info.main">
        Contracts Hub
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Escrow, collateral, reputation impact, arbitration — всё в одном месте.
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        label="Type"
        size="small"
        value={typeFilter}
        onChange={(event) => setTypeFilter(event.target.value as typeof typeFilter)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {contractTypes.map((value) => (
          <MenuItem key={value} value={value} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {value}
          </MenuItem>
        ))}
      </TextField>
      <TextField
        select
        label="Status"
        size="small"
        value={statusFilter}
        onChange={(event) => setStatusFilter(event.target.value as typeof statusFilter)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {contractStatuses.map((value) => (
          <MenuItem key={value} value={value} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {value}
          </MenuItem>
        ))}
      </TextField>
      <CyberpunkButton variant="primary" size="small" fullWidth>
        Создать контракт
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth>
        Заполнить шаблон
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth>
        Открыть спор
      </CyberpunkButton>
    </Stack>
  )

  const rightPanel = (
    <Stack spacing={2}>
      <EscrowStatusCard status={demoEscrow} />
      {demoDisputes.map((dispute) => (
        <DisputeCard key={dispute.disputeId} dispute={dispute} />
      ))}
    </Stack>
  )

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <GavelIcon sx={{ fontSize: '1.4rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Contract Operations Center
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Управляй контрактами: escrow, collateral, репутация, споры и авто-выплаты. Multi-party сделки с
        шаблонами, арбитрами и SLA.
      </Alert>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
            Active Contracts
          </Typography>
          <Stack spacing={1} mt={0.5}>
            {filteredContracts.map((contract) => (
              <ContractCard key={contract.contractId} contract={contract} />
            ))}
            {filteredContracts.length === 0 && (
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                Контракты не найдены под выбранные фильтры
              </Typography>
            )}
          </Stack>
        </Grid>
        <Grid item xs={12} md={6}>
          <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
            Templates
          </Typography>
          <Stack spacing={1} mt={0.5}>
            {demoTemplates.map((template) => (
              <ContractTemplateCard key={template.templateId} template={template} />
            ))}
          </Stack>
        </Grid>
      </Grid>
    </Stack>
  )

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>
}

export default ContractsPage


