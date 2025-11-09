/**
 * NPCHiringExtendedPage - расширенный найм NPC
 */

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Typography,
  Stack,
  Divider,
  Alert,
  Grid,
  Box,
  TextField,
  MenuItem,
  FormControlLabel,
  Switch,
  Chip,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import WarningAmberIcon from '@mui/icons-material/WarningAmber';
import LocalFireDepartmentIcon from '@mui/icons-material/LocalFireDepartment';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { useGameState } from '@/features/game/hooks/useGameState';
import { HireableNPCExtendedCard, HireableNPCSummary } from '../components/HireableNPCExtendedCard';
import { HiringContractCard } from '../components/HiringContractCard';
import { MissionAssignmentCard } from '../components/MissionAssignmentCard';
import { SupportAssetCard } from '../components/SupportAssetCard';
import { LoyaltyProgramCard } from '../components/LoyaltyProgramCard';
import { HiringSummaryCard } from '../components/HiringSummaryCard';

const typeOptions: Array<'ALL' | HireableNPCSummary['role']> = [
  'ALL',
  'COMBAT',
  'TECH',
  'HACKER',
  'MEDIC',
  'DRIVER',
  'MERCHANT',
  'BODYGUARD',
];

const riskOptions = ['ALL', 'LOW', 'MEDIUM', 'HIGH'] as const;

const demoHireableNPCs: HireableNPCSummary[] = [
  {
    npcId: 'npc-ghost',
    name: 'Alpha Ghost',
    role: 'HACKER',
    specialization: 'Quickhack assassin · ICE breaker',
    squadAffiliation: 'Neon Phantoms',
    tier: 'S',
    legendary: true,
    skillLevel: 19,
    loyalty: 72,
    effectiveness: 88,
    salaryPerDay: 4200,
    riskLevel: 'MEDIUM',
    traits: ['Stealth deploy', 'ICE neutralizer', 'Combat quickhacks'],
  },
  {
    npcId: 'npc-riptide',
    name: 'Riptide',
    role: 'COMBAT',
    specialization: 'Heavy weapons · extraction ops',
    squadAffiliation: 'Steel Fangs',
    tier: 'SS',
    legendary: true,
    skillLevel: 21,
    loyalty: 66,
    effectiveness: 91,
    salaryPerDay: 5200,
    riskLevel: 'HIGH',
    traits: ['Suppressive fire', 'Tripwire expert', 'Exo-suit pilot'],
  },
  {
    npcId: 'npc-medic',
    name: 'Dr. Vex',
    role: 'MEDIC',
    specialization: 'On-field trauma support · combat stims',
    squadAffiliation: 'Trauma Dynasty',
    tier: 'A',
    legendary: false,
    skillLevel: 16,
    loyalty: 84,
    effectiveness: 76,
    salaryPerDay: 2800,
    riskLevel: 'LOW',
    traits: ['Trauma clamp', 'Nano-stim blend', 'Emergency extraction'],
  },
];

const demoContract = {
  contractId: 'contract-omega',
  npcName: 'Riptide',
  termDays: 30,
  dailyRate: 5200,
  signingBonus: 25000,
  clauses: ['Hazard pay in Badlands', 'Exclusive to player faction', 'Bonus if mission success > 80%'],
  risk: 'HIGH' as const,
};

const demoMission = {
  missionId: 'mission-convoy',
  title: 'Neon Convoy Escort',
  location: 'Night City → Badlands',
  dangerLevel: 'HIGH' as const,
  payout: '48 000¥',
  successChance: 72,
  members: [
    { npcName: 'Alpha Ghost', role: 'Hacker', effectiveness: 88 },
    { npcName: 'Riptide', role: 'Combat', effectiveness: 91 },
  ],
};

const demoAssets = [
  {
    assetId: 'asset-drone',
    name: 'Aquila Drone Squad',
    type: 'DRONE' as const,
    status: 'READY' as const,
    upkeepCost: 1200,
    capacity: 4,
    utilization: 65,
    bonuses: ['Recon mapping', 'Suppressive fire'],
  },
  {
    assetId: 'asset-vtol',
    name: 'VTOL Specter',
    type: 'VEHICLE' as const,
    status: 'DEPLOYED' as const,
    upkeepCost: 3200,
    capacity: 6,
    utilization: 82,
    bonuses: ['Hot extraction', 'Aerial overwatch'],
  },
];

const loyaltyProgram = {
  programName: 'Night Market Syndicate',
  currentPoints: 320,
  nextTierPoints: 400,
  tiers: [
    { tier: 'Bronze', benefits: '+5% loyalty gain', requiredPoints: 100, unlocked: true },
    { tier: 'Silver', benefits: 'Exclusive missions', requiredPoints: 250, unlocked: true },
    { tier: 'Gold', benefits: 'Reduced salary 10%', requiredPoints: 400, unlocked: false },
  ],
};

const summaryStats = {
  activeHires: 5,
  pendingContracts: 2,
  weeklyUpkeep: 19800,
  squadStrength: 86,
  missionCoverage: 72,
};

export const NPCHiringExtendedPage: React.FC = () => {
  const navigate = useNavigate();
  const { selectedCharacterId } = useGameState();
  const [typeFilter, setTypeFilter] = useState<typeof typeOptions[number]>('ALL');
  const [riskFilter, setRiskFilter] = useState<typeof riskOptions[number]>('ALL');
  const [includeLegendary, setIncludeLegendary] = useState<boolean>(true);

  const filteredNPCs = demoHireableNPCs.filter((npc) => {
    const matchesType = typeFilter === 'ALL' || npc.role === typeFilter;
    const matchesRisk = riskFilter === 'ALL' || npc.riskLevel === riskFilter;
    const matchesLegendary = includeLegendary || !npc.legendary;
    return matchesType && matchesRisk && matchesLegendary;
  });

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
        NPC Hiring Extended
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        7 типов специалистов · legendary найм · KPI и лояльность · Convoy/Extraction миссии
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Фильтры
      </Typography>
      <TextField
        select
        size="small"
        label="Type"
        value={typeFilter}
        onChange={(event) => setTypeFilter(event.target.value as typeof typeOptions[number])}
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
        size="small"
        label="Risk"
        value={riskFilter}
        onChange={(event) => setRiskFilter(event.target.value as typeof riskOptions[number])}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {riskOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={
          <Switch
            size="small"
            checked={includeLegendary}
            onChange={(event) => setIncludeLegendary(event.target.checked)}
          />
        }
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Show legendary</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth startIcon={<PersonAddIcon />}>
        Подписать контракт
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth startIcon={<WarningAmberIcon />}>
        Назначить миссию
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth startIcon={<LocalFireDepartmentIcon />}>
        Активация отряда
      </CyberpunkButton>
    </Stack>
  );

  const rightPanel = (
    <Stack spacing={2}>
      <HiringSummaryCard {...summaryStats} />
      <LoyaltyProgramCard {...loyaltyProgram} />
      <Alert severity="warning" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Высокий риск повышает шансы на конфликты и снижает лояльность. Используйте loyalty программы и легендарные бонусы.
      </Alert>
    </Stack>
  );

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <PersonAddIcon sx={{ fontSize: '1.4rem', color: 'success.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          NPC Hiring Command Center
        </Typography>
        <Chip label="extended" size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Управляй наёмниками: legendary бойцы, хакеры, медики, VR-пилоты. Контракты, конвои, лояльность, поддержка.
      </Alert>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Candidates
      </Typography>
      <Grid container spacing={1}>
        {filteredNPCs.map((npc) => (
          <Grid key={npc.npcId} item xs={12} md={6} lg={4}>
            <HireableNPCExtendedCard npc={npc} onHire={() => undefined} />
          </Grid>
        ))}
      </Grid>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Contracts & Missions
      </Typography>
      <Grid container spacing={1}>
        <Grid item xs={12} md={6}>
          <HiringContractCard contract={demoContract} />
        </Grid>
        <Grid item xs={12} md={6}>
          <MissionAssignmentCard mission={demoMission} />
        </Grid>
      </Grid>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Support Assets
      </Typography>
      <Grid container spacing={1}>
        {demoAssets.map((asset) => (
          <Grid key={asset.assetId} item xs={12} md={6}>
            <SupportAssetCard asset={asset} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default NPCHiringExtendedPage;

