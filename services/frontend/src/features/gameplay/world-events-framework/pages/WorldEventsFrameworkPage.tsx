/**
 * WorldEventsFrameworkPage - страница фреймворка мировых событий
 * 
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Typography, Stack, Divider, Alert, Grid, Box } from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import PublicIcon from '@mui/icons-material/Public';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { WorldEventCard } from '../components/WorldEventCard';
import { useGameState } from '@/features/game/hooks/useGameState';

export const WorldEventsFrameworkPage: React.FC = () => {
  const navigate = useNavigate();
  const { selectedCharacterId } = useGameState();

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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="primary">
        World Events
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Фреймворк мировых событий
      </Typography>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Эпохи
      </Typography>
      <Stack spacing={0.3}>
        {['1990-2000', '2000-2020', '2020-2040', '2040-2060', '2060-2077', '2078-2090', '2090-2093'].map((e, i) => (
          <Typography key={i} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {e}
          </Typography>
        ))}
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Типы событий
      </Typography>
      <Stack spacing={0.3}>
        {['GLOBAL', 'REGIONAL', 'LOCAL', 'FACTION', 'ECONOMIC', 'TECHNOLOGICAL'].map((t, i) => (
          <Typography key={i} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {t}
          </Typography>
        ))}
      </Stack>
    </Stack>
  );

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Механики
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {['DC scaling', 'AI sliders', 'D&D generators (d100)', 'Economic multipliers', 'Technology access', 'Quest hooks'].map((f, i) => (
          <Typography key={i} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {f}
          </Typography>
        ))}
      </Stack>
    </Stack>
  );

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <PublicIcon sx={{ fontSize: '1.5rem', color: 'primary.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Фреймворк мировых событий
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        7 эпох (1990-2093)! DC scaling, AI sliders, D&D event generators (d100), economic multipliers, technology access!
      </Alert>
      <Typography variant="body2" fontSize={cyberpunkTokens.fonts.sm}>
        Активных событий: 0
      </Typography>
      <Grid container spacing={1}>
        {[].map((event, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <WorldEventCard event={event} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default WorldEventsFrameworkPage;

