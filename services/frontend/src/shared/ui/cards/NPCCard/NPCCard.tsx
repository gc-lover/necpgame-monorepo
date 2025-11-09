/**
 * NPCCard - карточка NPC
 * 
 * Компактная карточка для отображения информации о NPC
 * 
 * КРИТИЧНО:
 * - Маленькие шрифты (0.65rem - 0.75rem)
 * - Компактный layout
 * - Цвет по отношению
 */

import { ReactNode } from 'react';
import { Stack, Typography, Chip, Box, Avatar } from '@mui/material';
import { CompactCard } from '../CompactCard';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface NPCCardProps {
  /** ID NPC */
  npcId: string;
  /** Имя NPC */
  name: string;
  /** Роль/профессия */
  role?: string;
  /** Локация */
  location?: string;
  /** Отношение (friendly, neutral, hostile) */
  attitude?: 'friendly' | 'neutral' | 'hostile';
  /** Уровень */
  level?: number;
  /** Аватар URL */
  avatarUrl?: string;
  /** Доступные действия */
  availableActions?: string[];
  /** Иконка (React элемент) */
  icon?: ReactNode;
  /** Дополнительные действия */
  actions?: ReactNode;
  /** Обработчик клика */
  onClick?: () => void;
  /** Компактный режим */
  compact?: boolean;
}

/**
 * Карточка NPC
 * 
 * Используется для отображения NPC в локациях, квестах, найме
 */
export function NPCCard({
  npcId,
  name,
  role,
  location,
  attitude = 'neutral',
  level,
  avatarUrl,
  availableActions,
  icon,
  actions,
  onClick,
  compact = false,
}: NPCCardProps) {
  const attitudeColorMap = {
    friendly: { color: 'green', label: 'Friendly' },
    neutral: { color: 'default', label: 'Neutral' },
    hostile: { color: 'pink', label: 'Hostile' },
  };

  const attitudeConfig = attitudeColorMap[attitude];

  return (
    <CompactCard 
      color={attitudeConfig.color === 'default' ? undefined : (attitudeConfig.color as any)}
      glowIntensity="weak" 
      compact={compact}
      onClick={onClick}
      sx={{ cursor: onClick ? 'pointer' : 'default' }}
    >
      <Stack spacing={compact ? 0.3 : 0.5}>
        {/* Header: Аватар + Имя + Уровень */}
        <Box display="flex" alignItems="center" gap={0.5}>
          {!compact && (
            <Avatar 
              src={avatarUrl} 
              sx={{ 
                width: 28, 
                height: 28,
                border: '1px solid',
                borderColor: 'divider',
              }}
            >
              {icon || name.charAt(0)}
            </Avatar>
          )}
          <Box flex={1}>
            <Typography 
              fontSize={cyberpunkTokens.fonts.sm} 
              fontWeight="bold"
            >
              {name}
            </Typography>
            {role && (
              <Typography 
                fontSize={cyberpunkTokens.fonts.xs} 
                color="text.secondary"
              >
                {role}
              </Typography>
            )}
          </Box>
          {level && (
            <Chip 
              label={`Lvl ${level}`} 
              size="small" 
              sx={{ height: 14, fontSize: '0.55rem' }}
            />
          )}
        </Box>

        {/* Chips: Отношение + Локация */}
        <Box display="flex" gap={0.3} flexWrap="wrap">
          <Chip 
            label={attitudeConfig.label} 
            size="small" 
            color={attitudeConfig.color === 'default' ? undefined : (attitudeConfig.color as any)}
            sx={{ height: 14, fontSize: '0.55rem' }}
          />
          {location && (
            <Chip 
              label={location} 
              size="small" 
              sx={{ height: 14, fontSize: '0.55rem' }}
            />
          )}
        </Box>

        {/* Доступные действия */}
        {!compact && availableActions && availableActions.length > 0 && (
          <Box>
            <Typography 
              fontSize={cyberpunkTokens.fonts.xs} 
              color="text.secondary"
              sx={{ mb: 0.3 }}
            >
              Available:
            </Typography>
            <Box display="flex" gap={0.3} flexWrap="wrap">
              {availableActions.slice(0, 3).map((action, i) => (
                <Chip 
                  key={i}
                  label={action} 
                  size="small" 
                  variant="outlined"
                  sx={{ height: 14, fontSize: '0.55rem' }}
                />
              ))}
              {availableActions.length > 3 && (
                <Chip 
                  label={`+${availableActions.length - 3}`} 
                  size="small" 
                  sx={{ height: 14, fontSize: '0.55rem' }}
                />
              )}
            </Box>
          </Box>
        )}

        {/* Actions */}
        {actions && (
          <Box display="flex" gap={0.5} flexWrap="wrap">
            {actions}
          </Box>
        )}
      </Stack>
    </CompactCard>
  );
}

