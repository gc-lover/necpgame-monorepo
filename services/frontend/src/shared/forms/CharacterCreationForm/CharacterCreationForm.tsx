/**
 * CharacterCreationForm - форма создания персонажа
 * 
 * Готовая форма для создания нового персонажа
 * 
 * КРИТИЧНО:
 * - Маленькие шрифты (0.75rem)
 * - Компактный layout
 * - Валидация
 */

import { Stack, TextField, MenuItem, Button, Typography, Divider } from '@mui/material';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import PersonAddIcon from '@mui/icons-material/PersonAdd';

export interface CharacterCreationData {
  name: string;
  cityId: string;
  classId: string;
  factionId?: string;
  appearance?: {
    gender?: string;
    bodyType?: string;
  };
}

export interface CharacterCreationFormProps {
  /** Обработчик отправки */
  onSubmit: (data: CharacterCreationData) => void;
  /** Доступные города */
  cities: Array<{ id: string; name: string }>;
  /** Доступные классы */
  classes: Array<{ id: string; name: string; description?: string }>;
  /** Доступные фракции */
  factions?: Array<{ id: string; name: string }>;
  /** Загрузка */
  isLoading?: boolean;
  /** Компактный режим */
  compact?: boolean;
}

/**
 * Форма создания персонажа
 * 
 * Используется на странице создания персонажа
 */
export function CharacterCreationForm({
  onSubmit,
  cities,
  classes,
  factions = [],
  isLoading = false,
  compact = false,
}: CharacterCreationFormProps) {
  const [formData, setFormData] = React.useState<CharacterCreationData>({
    name: '',
    cityId: '',
    classId: '',
    factionId: '',
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  const handleChange = (field: keyof CharacterCreationData) => (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    setFormData(prev => ({ ...prev, [field]: e.target.value }));
  };

  return (
    <form onSubmit={handleSubmit}>
      <Stack spacing={compact ? 1.5 : 2}>
        <Typography 
          variant="h6" 
          fontSize={cyberpunkTokens.fonts.lg}
          fontWeight="bold"
          sx={{ color: 'primary.main', textShadow: cyberpunkTokens.effects.neonGlowWeak }}
        >
          Создание персонажа
        </Typography>

        <Divider />

        {/* Имя персонажа */}
        <TextField
          label="Имя персонажа"
          value={formData.name}
          onChange={handleChange('name')}
          required
          fullWidth
          size="small"
          sx={{ 
            '& .MuiInputBase-input': { fontSize: cyberpunkTokens.fonts.sm },
            '& .MuiInputLabel-root': { fontSize: cyberpunkTokens.fonts.sm },
          }}
        />

        {/* Город рождения */}
        <TextField
          select
          label="Город рождения"
          value={formData.cityId}
          onChange={handleChange('cityId')}
          required
          fullWidth
          size="small"
          sx={{ 
            '& .MuiInputBase-input': { fontSize: cyberpunkTokens.fonts.sm },
            '& .MuiInputLabel-root': { fontSize: cyberpunkTokens.fonts.sm },
          }}
        >
          {cities.map((city) => (
            <MenuItem key={city.id} value={city.id} sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
              {city.name}
            </MenuItem>
          ))}
        </TextField>

        {/* Класс */}
        <TextField
          select
          label="Класс"
          value={formData.classId}
          onChange={handleChange('classId')}
          required
          fullWidth
          size="small"
          sx={{ 
            '& .MuiInputBase-input': { fontSize: cyberpunkTokens.fonts.sm },
            '& .MuiInputLabel-root': { fontSize: cyberpunkTokens.fonts.sm },
          }}
        >
          {classes.map((cls) => (
            <MenuItem key={cls.id} value={cls.id} sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
              {cls.name}
            </MenuItem>
          ))}
        </TextField>

        {/* Фракция (опционально) */}
        {factions.length > 0 && (
          <TextField
            select
            label="Фракция (опционально)"
            value={formData.factionId}
            onChange={handleChange('factionId')}
            fullWidth
            size="small"
            sx={{ 
              '& .MuiInputBase-input': { fontSize: cyberpunkTokens.fonts.sm },
              '& .MuiInputLabel-root': { fontSize: cyberpunkTokens.fonts.sm },
            }}
          >
            <MenuItem value="" sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
              <em>Нет</em>
            </MenuItem>
            {factions.map((faction) => (
              <MenuItem key={faction.id} value={faction.id} sx={{ fontSize: cyberpunkTokens.fonts.sm }}>
                {faction.name}
              </MenuItem>
            ))}
          </TextField>
        )}

        {/* Кнопка создания */}
        <CyberpunkButton
          variant="primary"
          size="medium"
          fullWidth
          startIcon={<PersonAddIcon />}
          type="submit"
          disabled={isLoading || !formData.name || !formData.cityId || !formData.classId}
        >
          {isLoading ? 'Создание...' : 'Создать персонажа'}
        </CyberpunkButton>
      </Stack>
    </form>
  );
}

import React from 'react';

