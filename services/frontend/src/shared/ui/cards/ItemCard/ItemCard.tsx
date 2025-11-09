/**
 * ItemCard - карточка предмета
 * 
 * Компактная карточка для отображения игровых предметов
 * 
 * КРИТИЧНО:
 * - Маленькие шрифты (0.65rem - 0.75rem)
 * - Компактный layout
 * - Цвет по редкости
 */

import { ReactNode } from 'react';
import { Stack, Typography, Chip, Box } from '@mui/material';
import { CompactCard } from '../CompactCard';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface ItemCardProps {
  /** ID предмета */
  itemId: string;
  /** Название предмета */
  name: string;
  /** Описание */
  description?: string;
  /** Редкость */
  rarity?: 'common' | 'uncommon' | 'rare' | 'epic' | 'legendary';
  /** Количество */
  quantity?: number;
  /** Тип предмета */
  type?: string;
  /** Уровень требования */
  requiredLevel?: number;
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
 * Карточка предмета
 * 
 * Используется в инвентаре, магазине, лоте, крафте
 */
export function ItemCard({
  itemId,
  name,
  description,
  rarity = 'common',
  quantity,
  type,
  requiredLevel,
  icon,
  actions,
  onClick,
  compact = false,
}: ItemCardProps) {
  const rarityColorMap = {
    common: { color: 'default', glow: 'weak' },
    uncommon: { color: 'green', glow: 'weak' },
    rare: { color: 'cyan', glow: 'normal' },
    epic: { color: 'purple', glow: 'normal' },
    legendary: { color: 'yellow', glow: 'strong' },
  };

  const rarityConfig = rarityColorMap[rarity];

  return (
    <CompactCard 
      color={rarityConfig.color as any}
      glowIntensity={rarityConfig.glow as any}
      compact={compact}
      onClick={onClick}
      sx={{ cursor: onClick ? 'pointer' : 'default' }}
    >
      <Stack spacing={compact ? 0.3 : 0.5}>
        {/* Header: Иконка + Название + Количество */}
        <Box display="flex" alignItems="center" gap={0.5}>
          {icon && (
            <Box sx={{ fontSize: '1.2rem', display: 'flex', alignItems: 'center' }}>
              {icon}
            </Box>
          )}
          <Box flex={1}>
            <Typography 
              fontSize={cyberpunkTokens.fonts.sm} 
              fontWeight="bold"
            >
              {name}
            </Typography>
          </Box>
          {quantity !== undefined && quantity > 1 && (
            <Chip 
              label={`x${quantity}`} 
              size="small" 
              sx={{ height: 14, fontSize: cyberpunkTokens.fonts.xs }}
            />
          )}
        </Box>

        {/* Chips: Редкость + Тип */}
        <Box display="flex" gap={0.3} flexWrap="wrap">
          <Chip 
            label={rarity.toUpperCase()} 
            size="small" 
            color={rarityConfig.color === 'default' ? undefined : (rarityConfig.color as any)}
            sx={{ height: 14, fontSize: '0.55rem' }}
          />
          {type && (
            <Chip 
              label={type} 
              size="small" 
              sx={{ height: 14, fontSize: '0.55rem' }}
            />
          )}
          {requiredLevel && (
            <Chip 
              label={`Lvl ${requiredLevel}`} 
              size="small" 
              sx={{ height: 14, fontSize: '0.55rem' }}
            />
          )}
        </Box>

        {/* Description */}
        {!compact && description && (
          <Typography 
            fontSize={cyberpunkTokens.fonts.xs} 
            color="text.secondary"
            sx={{ 
              overflow: 'hidden',
              textOverflow: 'ellipsis',
              display: '-webkit-box',
              WebkitLineClamp: 2,
              WebkitBoxOrient: 'vertical',
            }}
          >
            {description}
          </Typography>
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

