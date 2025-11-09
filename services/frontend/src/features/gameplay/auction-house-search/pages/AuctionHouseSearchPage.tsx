/**
 * AuctionHouseSearchPage - страница поиска аукциона
 *
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Typography,
  Stack,
  Divider,
  Alert,
  Grid,
  Box,
  Chip,
  TextField,
  MenuItem,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SearchIcon from '@mui/icons-material/Search';
import { GameLayout } from '@/shared/ui/layout';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { AuctionSearchResultCard } from '../components/AuctionSearchResultCard';
import { useGameState } from '@/features/game/hooks/useGameState';

const rarityOptions = ['common', 'uncommon', 'rare', 'epic', 'legendary', 'iconic'];
const sortOptions = ['price_asc', 'price_desc', 'time_asc', 'time_desc', 'rarity'];

export const AuctionHouseSearchPage: React.FC = () => {
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
      <Typography variant="h6" fontSize={cyberpunkTokens.fonts.lg} fontWeight="bold" color="info">
        Поиск аукциона
      </Typography>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
        Фильтры и сортировка
      </Typography>
      <Divider />
      <Stack spacing={1}>
        <TextField
          size="small"
          label="Название"
          variant="outlined"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
          inputProps={{ style: { fontSize: cyberpunkTokens.fonts.xs } }}
        />
        <TextField
          size="small"
          select
          label="Редкость"
          defaultValue=""
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
        >
          <MenuItem value="">
            <em>Любая</em>
          </MenuItem>
          {rarityOptions.map((rarity) => (
            <MenuItem key={rarity} value={rarity} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
              {rarity.toUpperCase()}
            </MenuItem>
          ))}
        </TextField>
        <TextField
          size="small"
          select
          label="Сортировка"
          defaultValue="time_desc"
          InputLabelProps={{ sx: { fontSize: cyberpunkTokens.fonts.xs } }}
        >
          {sortOptions.map((option) => (
            <MenuItem key={option} value={option} sx={{ fontSize: cyberpunkTokens.fonts.xs }}>
              {option}
            </MenuItem>
          ))}
        </TextField>
        <CyberpunkButton variant="primary" size="small" fullWidth startIcon={<SearchIcon />}>
          Найти
        </CyberpunkButton>
      </Stack>
      <Divider />
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Быстрые фильтры
      </Typography>
      <Stack direction="row" flexWrap="wrap" gap={0.5}>
        {['Legendary', 'Cyberware', 'Katana', 'Militech', 'Arasaka'].map((tag) => (
          <Chip
            key={tag}
            label={tag}
            size="small"
            sx={{
              height: 16,
              fontSize: cyberpunkTokens.fonts.xs,
              bgcolor: 'rgba(33, 150, 243, 0.15)',
            }}
          />
        ))}
      </Stack>
    </Stack>
  );

  const rightPanel = (
    <Stack spacing={2}>
      <Typography variant="subtitle2" fontSize={cyberpunkTokens.fonts.md} fontWeight="bold">
        Подсказки
      </Typography>
      <Divider />
      <Stack spacing={0.5}>
        {[
          'Используй buy orders для мгновенного выкупа',
          'Сортировка по времени помогает ловить новые лоты',
          'Фильтры по бренду ищут уникальные предметы',
        ].map((tip) => (
          <Typography key={tip} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
            • {tip}
          </Typography>
        ))}
      </Stack>
    </Stack>
  );

  const centerContent = (
    <Stack spacing={2} height="100%">
      <Box display="flex" alignItems="center" gap={1}>
        <SearchIcon sx={{ fontSize: '1.5rem', color: 'info.main' }} />
        <Typography variant="h5" fontSize={cyberpunkTokens.fonts.xl} fontWeight="bold">
          Поиск лотов
        </Typography>
      </Box>
      <Divider />
      <Alert severity="info" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
        Мощный поиск аукциона! Фильтрация по категории, редкости, цене, бренду, уровню. Сортировка
        по цене, времени и редкости.
      </Alert>
      <Typography variant="body2" fontSize={cyberpunkTokens.fonts.sm}>
        Найдено результатов: 0
      </Typography>
      <Grid container spacing={1}>
        {[].map((lot, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <AuctionSearchResultCard lot={lot} />
          </Grid>
        ))}
      </Grid>
    </Stack>
  );

  return <GameLayout leftPanel={leftPanel} rightPanel={rightPanel}>{centerContent}</GameLayout>;
};

export default AuctionHouseSearchPage;


