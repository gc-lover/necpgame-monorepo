/**
 * HireableNPCExtendedCard - карточка нанимаемого NPC (extended)
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Box, Chip } from '@mui/material';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import ShieldIcon from '@mui/icons-material/Shield';
import MilitaryTechIcon from '@mui/icons-material/MilitaryTech';
import { CompactCard } from '@/shared/ui/cards';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { ProgressBar } from '@/shared/ui/stats/ProgressBar';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface HireableNPCSummary {
  npcId: string;
  name: string;
  role: 'COMBAT' | 'TECH' | 'HACKER' | 'MEDIC' | 'DRIVER' | 'MERCHANT' | 'BODYGUARD';
  specialization: string;
  squadAffiliation?: string;
  tier: 'A' | 'B' | 'S' | 'SS';
  legendary?: boolean;
  skillLevel: number;
  loyalty: number;
  effectiveness: number;
  salaryPerDay: number;
  riskLevel: 'LOW' | 'MEDIUM' | 'HIGH';
  traits: string[];
}

const roleColor: Record<HireableNPCSummary['role'], 'pink' | 'cyan' | 'purple' | 'green' | 'yellow'> = {
  COMBAT: 'pink',
  TECH: 'cyan',
  HACKER: 'purple',
  MEDIC: 'green',
  DRIVER: 'yellow',
  MERCHANT: 'green',
  BODYGUARD: 'pink',
};

export interface HireableNPCExtendedCardProps {
  npc: HireableNPCSummary;
  onHire?: (npcId: string) => void;
}

export const HireableNPCExtendedCard: React.FC<HireableNPCExtendedCardProps> = ({ npc, onHire }) => (
  <CompactCard color={roleColor[npc.role]} glowIntensity="normal" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <PersonAddIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            {npc.name}
          </Typography>
        </Box>
        <Box display="flex" gap={0.3}>
          <Chip
            label={`Tier ${npc.tier}`}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
            color={npc.tier === 'SS' ? 'warning' : npc.tier === 'S' ? 'info' : 'default'}
          />
          {npc.legendary && (
            <Chip
              icon={<MilitaryTechIcon sx={{ fontSize: '0.8rem !important' }} />}
              label="Legendary"
              size="small"
              sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
              color="secondary"
            />
          )}
        </Box>
      </Box>

      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        {npc.specialization} · {npc.squadAffiliation ?? 'Independent'}
      </Typography>

      <Box display="flex" gap={0.3} flexWrap="wrap">
        <Chip
          label={npc.role}
          size="small"
          color={roleColor[npc.role]}
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
        <Chip
          label={`Skill ${npc.skillLevel}`}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
        <Chip
          icon={<ShieldIcon sx={{ fontSize: '0.8rem !important' }} />}
          label={`${npc.riskLevel} risk`}
          size="small"
          sx={{
            height: 16,
            fontSize: cyberpunkTokens.fonts.xs,
            border: npc.riskLevel === 'HIGH' ? '1px solid #ff2a6d' : npc.riskLevel === 'MEDIUM' ? '1px solid #fef86c' : '1px solid #05ffa1',
            color: npc.riskLevel === 'HIGH' ? '#ff2a6d' : npc.riskLevel === 'MEDIUM' ? '#fef86c' : '#05ffa1',
          }}
        />
      </Box>

      <ProgressBar value={npc.loyalty} label="Loyalty" color="green" compact />
      <ProgressBar value={npc.effectiveness} label="Effectiveness" color="cyan" compact />

      <Stack spacing={0.2}>
        {npc.traits.slice(0, 3).map((trait) => (
          <Typography key={trait} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            • {trait}
          </Typography>
        ))}
        {npc.traits.length > 3 && (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            +{npc.traits.length - 3} more traits
          </Typography>
        )}
      </Stack>

      {onHire && (
        <CyberpunkButton
          variant="primary"
          size="small"
          fullWidth
          startIcon={<PersonAddIcon />}
          onClick={() => onHire(npc.npcId)}
        >
          Нанять — {npc.salaryPerDay}¥/день
        </CyberpunkButton>
      )}
    </Stack>
  </CompactCard>
);

export default HireableNPCExtendedCard;

