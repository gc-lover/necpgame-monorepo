/**
 * MentorshipExtendedPage - расширенная система наставничества
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
import SchoolIcon from '@mui/icons-material/School';
import GroupWorkIcon from '@mui/icons-material/GroupWork';
import StarsIcon from '@mui/icons-material/Stars';
import AutoFixHighIcon from '@mui/icons-material/AutoFixHigh';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { useGameState } from '@/features/game/hooks/useGameState';
import { MentorExtendedCard } from '../components/MentorExtendedCard';
import { MentorshipRelationshipCard } from '../components/MentorshipRelationshipCard';
import { MentorshipLessonCard } from '../components/MentorshipLessonCard';
import { MentorshipAbilityCard } from '../components/MentorshipAbilityCard';
import { GraduationStatusCard } from '../components/GraduationStatusCard';
import { MentorshipSummaryCard } from '../components/MentorshipSummaryCard';

const typeOptions = ['ALL', 'COMBAT', 'TECH', 'NETRUNNING', 'SOCIAL', 'ECONOMY', 'MEDICAL'];

const demoMentors = [
  {
    mentor_id: 'mentor-morgan',
    name: 'Morgan Blackhand',
    type: 'COMBAT' as const,
    specialization: 'Solo combat tactics, bladework',
    mentor_rank: 'LEGENDARY' as const,
    legendary: true,
    skill_level: 20,
    reputation: 95,
    success_rate: 88,
    unique_abilities: ['Adrenal Surge', 'Blade Mastery', 'Urban Warfare Insights', 'Last Stand'],
    student_slots: 3,
    current_students: 1,
    bond_strength: 68,
  },
  {
    mentor_id: 'mentor-juliet',
    name: 'Juliet Silverhand',
    type: 'SOCIAL' as const,
    specialization: 'Influence networks, diplomatic pressure',
    mentor_rank: 'MASTER' as const,
    legendary: false,
    skill_level: 18,
    reputation: 87,
    success_rate: 82,
    unique_abilities: ['Citywide Contacts', 'Corporate Blackmail Toolkit'],
    student_slots: 4,
    current_students: 2,
    bond_strength: 54,
  },
  {
    mentor_id: 'mentor-rex',
    name: 'Rex Prototype',
    type: 'TECH' as const,
    specialization: 'Implant optimisation, drone squads',
    mentor_rank: 'ELITE' as const,
    legendary: false,
    skill_level: 17,
    reputation: 80,
    success_rate: 76,
    unique_abilities: ['Drone Command Mesh', 'Implant Overclock'],
    student_slots: 2,
    current_students: 1,
    bond_strength: 48,
  },
];

const demoRelationships = [
  {
    relationshipId: 'rel-rogue',
    mentorName: 'Rogue Amendiares',
    type: 'SOCIAL' as const,
    stage: 'ADVANCED' as const,
    bondStrength: 72,
    trust: 64,
    lessonsCompleted: 8,
    totalLessons: 12,
    startedAt: '2077-10-01T00:00:00Z',
  },
  {
    relationshipId: 'rel-panacea',
    mentorName: 'Panacea Doc',
    type: 'MEDICAL' as const,
    stage: 'ACTIVE' as const,
    bondStrength: 58,
    trust: 61,
    lessonsCompleted: 4,
    totalLessons: 9,
    startedAt: '2077-09-18T00:00:00Z',
  },
];

const demoLessons = [
  {
    lessonId: 'lesson-quickhack',
    title: 'Advanced Quickhacks',
    stage: 'ADVANCED',
    difficulty: 'LEGENDARY' as const,
    requirements: ['Cyberdeck Mk.IV', 'INT 80+', 'Complete “Neon Ghost” quest'],
    reward: '+20% quickhack damage',
    recommendedScore: 85,
  },
  {
    lessonId: 'lesson-blades',
    title: 'Urban Blade Duelists',
    stage: 'ACTIVE',
    difficulty: 'HARD' as const,
    requirements: ['STR 70+', 'Reflexes 65+'],
    reward: 'Unlock counter-cut finisher',
    recommendedScore: 75,
  },
];

const demoAbilities = [
  {
    abilityId: 'ability-overclock',
    name: 'Neural Overclock',
    description: 'Instantly refreshes quickhack cooldowns and grants +25% CPU regeneration for 20s.',
    rarity: 'LEGENDARY' as const,
    activationCost: '40 RAM',
    cooldown: '90s',
  },
  {
    abilityId: 'ability-steelcore',
    name: 'Steelcore Shield',
    description: 'Absorbs 300 damage and converts 30% to stamina.',
    rarity: 'EPIC' as const,
    activationCost: '30 stamina',
    cooldown: '60s',
  },
];

const graduationStatus = {
  stage: 'IN_PROGRESS' as const,
  progress: 64,
  mentorApproval: 72,
  reputationImpact: '+150 reputation with Afterlife',
  requirements: [
    { label: 'Complete legendary lesson', completed: false },
    { label: 'Bond strength 70+', completed: true },
    { label: 'Deliver AI thesis to mentor', completed: false },
  ],
};

const summaryStats = {
  activeMentors: 3,
  pendingRequests: 1,
  activeLessons: 4,
  uniqueAbilitiesUnlocked: 7,
  worldImpactScore: 1280,
};

export const MentorshipExtendedPage: React.FC = () => {
  const navigate = useNavigate();
  const { selectedCharacterId } = useGameState();
  const [typeFilter, setTypeFilter] = useState<string>('ALL');
  const [showLegendaryOnly, setShowLegendaryOnly] = useState<boolean>(false);
  const [autoEnroll, setAutoEnroll] = useState<boolean>(true);

  const filteredMentors = demoMentors.filter((mentor) => {
    const matchesType = typeFilter === 'ALL' || mentor.type === typeFilter;
    const matchesLegendary = !showLegendaryOnly || mentor.legendary;
    return matchesType && matchesLegendary;
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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info.main">
        Mentorship Extended
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        6 типов наставничества · abilities transfer · легендарные NPC · graduation system
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
        onChange={(event) => setTypeFilter(event.target.value)}
        InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
      >
        {typeOptions.map((option) => (
          <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
            {option}
          </MenuItem>
        ))}
      </TextField>
      <FormControlLabel
        control={
          <Switch
            size="small"
            checked={showLegendaryOnly}
            onChange={(event) => setShowLegendaryOnly(event.target.checked)}
          />
        }
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Legendary mentors only</Typography>}
      />
      <FormControlLabel
        control={
          <Switch
            size="small"
            checked={autoEnroll}
            onChange={(event) => setAutoEnroll(event.target.checked)}
          />
        }
        label={<Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>Auto enroll on request</Typography>}
      />
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые действия
      </Typography>
      <CyberpunkButton variant="primary" size="small" fullWidth startIcon={<GroupWorkIcon />}>
        Найти наставника
      </CyberpunkButton>
      <CyberpunkButton variant="secondary" size="small" fullWidth startIcon={<AutoFixHighIcon />}>
        Получить способность
      </CyberpunkButton>
      <CyberpunkButton variant="outlined" size="small" fullWidth startIcon={<StarsIcon />}>
        Легендарные программы
      </CyberpunkButton>
    </Stack>
  );

  const rightPanel = (
    <Stack spacing={2}>
      <MentorshipSummaryCard {...summaryStats} />
      <GraduationStatusCard {...graduationStatus} />
      <Alert severity="warning" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Legend-tier выпускники повышают мировой уровень событий и открывают эксклюзивные рейды.
      </Alert>
    </Stack>
  );

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <SchoolIcon sx={{ fontSize: '1.4rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Mentorship Operations Center
        </Typography>
        <Chip label="extended" size="small" sx={{ ml: 'auto', fontSize: cyberpunkTokens.fonts.xs }} />
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Управляй наставничеством: legendary NPCs, abilities transfer, мировое влияние, graduation system, AI-гид уроков.
      </Alert>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Available Mentors
      </Typography>
      <Grid container spacing={1}>
        {filteredMentors.map((mentor) => (
          <Grid key={mentor.mentor_id} item xs={12} md={6} lg={4}>
            <MentorExtendedCard mentor={mentor} onRequest={() => undefined} />
          </Grid>
        ))}
      </Grid>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Active Relationships
      </Typography>
      <Grid container spacing={1}>
        {demoRelationships.map((relationship) => (
          <Grid key={relationship.relationshipId} item xs={12} md={6}>
            <MentorshipRelationshipCard relationship={relationship} />
          </Grid>
        ))}
      </Grid>

      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold" color="text.secondary">
        Lessons & Abilities
      </Typography>
      <Grid container spacing={1}>
        {demoLessons.map((lesson) => (
          <Grid key={lesson.lessonId} item xs={12} md={6}>
            <MentorshipLessonCard lesson={lesson} />
          </Grid>
        ))}
        {demoAbilities.map((ability) => (
          <Grid key={ability.abilityId} item xs={12} md={6}>
            <MentorshipAbilityCard ability={ability} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default MentorshipExtendedPage;

