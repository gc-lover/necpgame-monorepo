/**
 * CyberpunkButton - кнопка с киберпанк стилем
 * 
 * Переиспользуемая кнопка с неоновыми эффектами
 * 
 * КРИТИЧНО:
 * - Маленький шрифт (0.75rem)
 * - MMORPG стиль (скос углов)
 * - Неоновое свечение
 */

import { ButtonHTMLAttributes, ReactNode } from 'react';
import { Box } from '@mui/material';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface CyberpunkButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  /** Содержимое кнопки */
  children: ReactNode;
  /** Вариант стиля */
  variant?: 'primary' | 'secondary' | 'success' | 'warning' | 'outlined';
  /** Размер */
  size?: 'small' | 'medium' | 'large';
  /** Полная ширина */
  fullWidth?: boolean;
  /** Иконка слева */
  startIcon?: ReactNode;
  /** Иконка справа */
  endIcon?: ReactNode;
}

/**
 * Кнопка с киберпанк стилем
 * 
 * Используется для всех действий в игре
 */
export function CyberpunkButton({ 
  children, 
  variant = 'primary',
  size = 'medium',
  fullWidth = false,
  startIcon,
  endIcon,
  disabled,
  ...rest 
}: CyberpunkButtonProps) {
  const variantMap = {
    primary: {
      bg: 'rgba(0, 247, 255, 0.15)',
      bgGradient: cyberpunkTokens.gradients.activeButton,
      border: cyberpunkTokens.colors.neonCyan,
      color: cyberpunkTokens.colors.neonCyan,
      shadow: 'rgba(0, 247, 255, 0.3)',
    },
    secondary: {
      bg: 'rgba(255, 42, 109, 0.15)',
      bgGradient: 'linear-gradient(135deg, rgba(255, 42, 109, 0.2) 0%, rgba(255, 42, 109, 0.05) 50%, rgba(10, 14, 39, 0.8) 100%)',
      border: cyberpunkTokens.colors.neonPink,
      color: cyberpunkTokens.colors.neonPink,
      shadow: 'rgba(255, 42, 109, 0.3)',
    },
    success: {
      bg: 'rgba(5, 255, 161, 0.15)',
      bgGradient: 'linear-gradient(135deg, rgba(5, 255, 161, 0.2) 0%, rgba(5, 255, 161, 0.05) 50%, rgba(10, 14, 39, 0.8) 100%)',
      border: cyberpunkTokens.colors.neonGreen,
      color: cyberpunkTokens.colors.neonGreen,
      shadow: 'rgba(5, 255, 161, 0.3)',
    },
    warning: {
      bg: 'rgba(254, 248, 108, 0.15)',
      bgGradient: 'linear-gradient(135deg, rgba(254, 248, 108, 0.2) 0%, rgba(254, 248, 108, 0.05) 50%, rgba(10, 14, 39, 0.8) 100%)',
      border: cyberpunkTokens.colors.neonYellow,
      color: cyberpunkTokens.colors.neonYellow,
      shadow: 'rgba(254, 248, 108, 0.3)',
    },
    outlined: {
      bg: 'transparent',
      bgGradient: cyberpunkTokens.gradients.normalButton,
      border: 'rgba(255, 255, 255, 0.3)',
      color: '#FFFFFF',
      shadow: 'rgba(0, 0, 0, 0.3)',
    },
  };

  const sizeMap = {
    small: {
      px: 1.5,
      py: 0.5,
      fontSize: cyberpunkTokens.fonts.xs, // 0.65rem
      iconSize: '0.9rem',
    },
    medium: {
      px: 2,
      py: 1,
      fontSize: cyberpunkTokens.fonts.sm, // 0.75rem
      iconSize: '1.1rem',
    },
    large: {
      px: 3,
      py: 1.5,
      fontSize: cyberpunkTokens.fonts.md, // 0.875rem
      iconSize: '1.3rem',
    },
  };

  const variantConfig = variantMap[variant];
  const sizeConfig = sizeMap[size];

  return (
    <Box
      component="button"
      disabled={disabled}
      {...rest}
      sx={{
        width: fullWidth ? '100%' : 'auto',
        px: sizeConfig.px,
        py: sizeConfig.py,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        gap: 0.75,
        border: '2px solid',
        borderColor: variantConfig.border,
        bgcolor: variantConfig.bg,
        background: variantConfig.bgGradient,
        color: variantConfig.color,
        fontSize: sizeConfig.fontSize,
        fontWeight: 'bold',
        textTransform: 'uppercase',
        letterSpacing: '0.05em',
        position: 'relative',
        overflow: 'hidden',
        cursor: disabled ? 'not-allowed' : 'pointer',
        transition: cyberpunkTokens.transitions.normal,
        boxShadow: `0 2px 8px rgba(0, 0, 0, 0.4), 0 0 12px ${variantConfig.shadow}`,
        clipPath: cyberpunkTokens.clipPath.corner,
        opacity: disabled ? 0.5 : 1,
        '&:hover:not(:disabled)': {
          borderColor: variantConfig.color,
          boxShadow: `0 4px 12px rgba(0, 0, 0, 0.5), 0 0 20px ${variantConfig.shadow}`,
          transform: 'translateY(-1px)',
        },
        '&:active:not(:disabled)': {
          transform: 'translateY(0)',
        },
      }}
    >
      {/* Градиентный фон для глубины */}
      <Box
        sx={{
          position: 'absolute',
          inset: 0,
          background: cyberpunkTokens.gradients.depth,
          opacity: 0.6,
          transition: cyberpunkTokens.transitions.normal,
        }}
      />
      
      {/* Контент */}
      <Box sx={{ position: 'relative', display: 'flex', alignItems: 'center', gap: 0.75, zIndex: 10 }}>
        {startIcon && (
          <Box component="span" sx={{ fontSize: sizeConfig.iconSize, display: 'flex', alignItems: 'center' }}>
            {startIcon}
          </Box>
        )}
        <Box component="span">{children}</Box>
        {endIcon && (
          <Box component="span" sx={{ fontSize: sizeConfig.iconSize, display: 'flex', alignItems: 'center' }}>
            {endIcon}
          </Box>
        )}
      </Box>
    </Box>
  );
}

