/**
 * MentorExtendedCard - карточка расширенного mentor
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Chip, Box } from '@mui/material';
import SchoolIcon from '@mui/icons-material/School';
import StarsIcon from '@mui/icons-material/Stars';
import MilitaryTechIcon from '@mui/icons-material/MilitaryTech';
import { CompactCard } from '@/shared/ui/cards';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { ProgressBar } from '@/shared/ui/stats';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface MentorExtendedCardProps {
  mentor: {
    mentor_id: string;
    name: string;
    type: 'COMBAT' | 'TECH' | 'NETRUNNING' | 'SOCIAL' | 'ECONOMY' | 'MEDICAL';
    specialization: string;
    mentor_rank: 'ELITE' | 'MASTER' | 'LEGENDARY';
    legendary?: boolean;
    skill_level: number;
    reputation: number;
    success_rate: number;
    unique_abilities: string[];
    student_slots: number;
    current_students: number;
    bond_strength?: number;
  };
  onRequest?: (mentorId: string) => void;
}

const typeColor: Record<MentorExtendedCardProps['mentor']['type'], 'pink' | 'cyan' | 'purple' | 'green' | 'yellow'> = {
  COMBAT: 'pink',
  TECH: 'cyan',
  NETRUNNING: 'purple',
  SOCIAL: 'green',
  ECONOMY: 'yellow',
  MEDICAL: 'green',
};

export const MentorExtendedCard: React.FC<MentorExtendedCardProps> = ({ mentor, onRequest }) => {
  const availableSlots = mentor.student_slots - mentor.current_students;
  const slotsPercent = mentor.student_slots === 0 ? 0 : (mentor.current_students / mentor.student_slots) * 100;

  return (
    <CompactCard color={typeColor[mentor.type]} glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <SchoolIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {mentor.name}
            </Typography>
          </Box>
          <Box display="flex" gap={0.3}>
            <Chip
              label={mentor.mentor_rank}
              size="small"
              sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
              color="info"
            />
            {mentor.legendary && (
              <Chip
                icon={<StarsIcon sx={{ fontSize: '0.8rem !important' }} />}
                label="Legendary"
                size="small"
                sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
                color="warning"
              />
            )}
          </Box>
        </Box>

        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {mentor.specialization}
        </Typography>

        <Box display="flex" gap={0.3} flexWrap="wrap">
          <Chip label={mentor.type} size="small" sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }} color={typeColor[mentor.type]} />
          <Chip
            label={`Skill ${mentor.skill_level}`}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
          <Chip
            icon={<MilitaryTechIcon sx={{ fontSize: '0.9rem !important' }} />}
            label={`${mentor.unique_abilities.length} abilities`}
            size="small"
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        </Box>

        <ProgressBar value={mentor.reputation} label="Reputation" color="cyan" compact />
        <ProgressBar value={mentor.success_rate} label="Success Rate" color="green" compact />
        {mentor.bond_strength !== undefined && (
          <ProgressBar value={mentor.bond_strength} label="Bond Strength" color="purple" compact />
        )}
        <ProgressBar
          value={slotsPercent}
          label="Student Slots"
          color="yellow"
          compact
          customText={`${mentor.current_students}/${mentor.student_slots} · free ${availableSlots}`}
        />

        <Stack spacing={0.2}>
          {mentor.unique_abilities.slice(0, 3).map((ability) => (
            <Typography key={ability} variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              • {ability}
            </Typography>
          ))}
          {mentor.unique_abilities.length > 3 && (
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              +{mentor.unique_abilities.length - 3} more abilities
            </Typography>
          )}
        </Stack>

        {onRequest && availableSlots > 0 && (
          <CyberpunkButton variant="primary" size="small" fullWidth onClick={() => onRequest(mentor.mentor_id)}>
            Запросить обучение
          </CyberpunkButton>
        )}
      </Stack>
    </CompactCard>
  );
};

export default MentorExtendedCard;

