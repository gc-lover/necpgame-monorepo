/**
 * CharacterCard - карточка персонажа
 * 
 * Компактная карточка для отображения информации о персонаже
 * 
 * КРИТИЧНО:
 * - Маленькие шрифты (0.65rem - 0.75rem)
 * - Компактный layout
 * - Неоновые эффекты
 */

import { ReactNode } from 'react';
import { Stack, Typography, Chip, Box, Avatar } from '@mui/material';
import { CompactCard } from '../CompactCard';
import { HealthBar } from '../../stats';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface CharacterCardProps {
  /** ID персонажа */
  characterId: string;
  /** Имя персонажа */
  name: string;
  /** Уровень */
  level: number;
  /** Класс */
  className?: string;
  /** Текущее HP */
  currentHp?: number;
  /** Максимальное HP */
  maxHp?: number;
  /** Текущая энергия */
  currentEnergy?: number;
  /** Максимальная энергия */
  maxEnergy?: number;
  /** Аватар URL (опционально) */
  avatarUrl?: string;
  /** Статус (online, offline, in_combat) */
  status?: 'online' | 'offline' | 'in_combat';
  /** Дополнительные действия */
  actions?: ReactNode;
  /** Обработчик клика */
  onClick?: () => void;
  /** Компактный режим */
  compact?: boolean;
}

/**
 * Карточка персонажа
 * 
 * Используется для отображения информации о персонаже в списках, party, друзьях
 */
export function CharacterCard({
  characterId,
  name,
  level,
  className,
  currentHp,
  maxHp,
  currentEnergy,
  maxEnergy,
  avatarUrl,
  status = 'offline',
  actions,
  onClick,
  compact = false,
}: CharacterCardProps) {
  const statusColorMap = {
    online: 'success',
    offline: 'default',
    in_combat: 'error',
  };

  return (
    <CompactCard 
      color="cyan" 
      glowIntensity="weak" 
      compact={compact}
      onClick={onClick}
      sx={{ cursor: onClick ? 'pointer' : 'default' }}
    >
      <Stack spacing={compact ? 0.5 : 1}>
        {/* Header: Аватар + Имя + Уровень */}
        <Box display="flex" alignItems="center" gap={1}>
          {!compact && (
            <Avatar 
              src={avatarUrl} 
              sx={{ 
                width: 32, 
                height: 32,
                border: '2px solid',
                borderColor: 'primary.main',
                boxShadow: cyberpunkTokens.effects.neonGlowWeak,
              }}
            >
              {name.charAt(0)}
            </Avatar>
          )}
          <Box flex={1}>
            <Typography 
              fontSize={cyberpunkTokens.fonts.sm} 
              fontWeight="bold"
              sx={{ 
                color: 'primary.main',
                textShadow: cyberpunkTokens.effects.neonGlowWeak,
              }}
            >
              {name}
            </Typography>
            {className && (
              <Typography 
                fontSize={cyberpunkTokens.fonts.xs} 
                color="text.secondary"
              >
                Level {level} {className}
              </Typography>
            )}
          </Box>
          <Chip 
            label={status} 
            color={statusColorMap[status] as any}
            size="small" 
            sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
          />
        </Box>

        {/* Stats: HP + Energy */}
        {!compact && (currentHp !== undefined || currentEnergy !== undefined) && (
          <Stack spacing={0.5}>
            {currentHp !== undefined && maxHp !== undefined && (
              <HealthBar 
                current={currentHp} 
                max={maxHp} 
                label="HP" 
                color="cyan"
                compact
              />
            )}
            {currentEnergy !== undefined && maxEnergy !== undefined && (
              <HealthBar 
                current={currentEnergy} 
                max={maxEnergy} 
                label="Energy" 
                color="green"
                compact
              />
            )}
          </Stack>
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

