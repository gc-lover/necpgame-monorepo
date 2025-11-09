/**
 * CompactCard - базовая компактная карточка
 * 
 * Переиспользуемая карточка с киберпанк стилем для отображения информации
 * 
 * КРИТИЧНО:
 * - Маленькие шрифты (наследуются от MUI)
 * - MMORPG стиль (скос углов)
 * - Неоновое свечение по цвету
 * - Компактный padding (p: 1.5)
 */

import { ReactNode } from 'react';
import { Card, CardContent, CardProps } from '@mui/material';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface CompactCardProps extends Omit<CardProps, 'color'> {
  /** Содержимое карточки */
  children: ReactNode;
  /** Цвет неонового свечения */
  color?: 'cyan' | 'pink' | 'green' | 'purple' | 'yellow' | 'default';
  /** Интенсивность свечения */
  glowIntensity?: 'none' | 'weak' | 'normal' | 'strong';
  /** Компактный режим (меньше padding) */
  compact?: boolean;
}

/**
 * Базовая компактная карточка
 * 
 * Используется во всех features для отображения информации
 */
export function CompactCard({ 
  children, 
  color = 'default', 
  glowIntensity = 'normal',
  compact = false,
  sx,
  ...rest 
}: CompactCardProps) {
  const colorMap = {
    cyan: { 
      border: cyberpunkTokens.colors.neonCyan, 
      shadow: 'rgba(0, 247, 255, 0.3)' 
    },
    pink: { 
      border: cyberpunkTokens.colors.neonPink, 
      shadow: 'rgba(255, 42, 109, 0.3)' 
    },
    green: { 
      border: cyberpunkTokens.colors.neonGreen, 
      shadow: 'rgba(5, 255, 161, 0.3)' 
    },
    purple: { 
      border: cyberpunkTokens.colors.neonPurple, 
      shadow: 'rgba(216, 23, 255, 0.3)' 
    },
    yellow: { 
      border: cyberpunkTokens.colors.neonYellow, 
      shadow: 'rgba(254, 248, 108, 0.3)' 
    },
    default: { 
      border: 'rgba(255, 255, 255, 0.1)', 
      shadow: 'rgba(0, 0, 0, 0.3)' 
    },
  };

  const colorConfig = colorMap[color];
  
  const glowMap = {
    none: '0',
    weak: `0 0 8px ${colorConfig.shadow}`,
    normal: `0 0 15px ${colorConfig.shadow}`,
    strong: `0 0 25px ${colorConfig.shadow}`,
  };

  const boxShadowBase = `0 2px 8px rgba(0, 0, 0, 0.4), 0 1px 4px rgba(0, 0, 0, 0.3), inset 0 1px 1px rgba(255, 255, 255, 0.05), inset 0 -1px 1px rgba(0, 0, 0, 0.3)`;
  const boxShadowHover = `0 4px 12px rgba(0, 0, 0, 0.5), 0 2px 6px rgba(0, 0, 0, 0.4), inset 0 1px 1px rgba(255, 255, 255, 0.08), inset 0 -1px 1px rgba(0, 0, 0, 0.4)`;

  return (
    <Card
      {...rest}
      sx={{
        border: '1px solid',
        borderColor: colorConfig.border,
        bgcolor: cyberpunkTokens.colors.cardBgAlpha,
        background: cyberpunkTokens.gradients.cardBg,
        boxShadow: `${boxShadowBase}, ${glowMap[glowIntensity]}`,
        clipPath: cyberpunkTokens.clipPath.corner,
        transition: cyberpunkTokens.transitions.normal,
        '&:hover': {
          boxShadow: `${boxShadowHover}, ${glowMap[glowIntensity]}`,
          transform: 'translateY(-1px)',
        },
        ...sx,
      }}
    >
      <CardContent 
        sx={{ 
          p: compact ? 1 : 1.5, 
          '&:last-child': { pb: compact ? 1 : 1.5 } 
        }}
      >
        {children}
      </CardContent>
    </Card>
  );
}

