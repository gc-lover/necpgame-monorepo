/**
 * HealthBar - полоска здоровья
 * 
 * Компактный прогресс-бар для отображения здоровья/энергии/маны
 * 
 * КРИТИЧНО:
 * - Маленький шрифт для значений (0.65rem)
 * - Неоновое свечение
 * - Компактная высота
 */

import { Box, LinearProgress, Typography } from '@mui/material';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface HealthBarProps {
  /** Текущее значение */
  current: number;
  /** Максимальное значение */
  max: number;
  /** Название (HP, Energy, Mana...) */
  label?: string;
  /** Цвет */
  color?: 'cyan' | 'pink' | 'green' | 'purple' | 'yellow';
  /** Показывать значения */
  showValues?: boolean;
  /** Компактный режим (без label) */
  compact?: boolean;
}

/**
 * Полоска здоровья
 * 
 * Используется для отображения HP, Energy, Mana и других показателей
 */
export function HealthBar({ 
  current, 
  max, 
  label,
  color = 'cyan',
  showValues = true,
  compact = false,
}: HealthBarProps) {
  const percent = Math.min(100, Math.max(0, (current / max) * 100));
  
  const colorMap = {
    cyan: {
      gradient: 'linear-gradient(90deg, #00F7FF, #33F8FF)',
      shadow: 'rgba(0, 247, 255, 0.5)',
    },
    pink: {
      gradient: 'linear-gradient(90deg, #ff2a6d, #ff5789)',
      shadow: 'rgba(255, 42, 109, 0.5)',
    },
    green: {
      gradient: 'linear-gradient(90deg, #05ffa1, #37FFB4)',
      shadow: 'rgba(5, 255, 161, 0.5)',
    },
    purple: {
      gradient: 'linear-gradient(90deg, #d817ff, #e847ff)',
      shadow: 'rgba(216, 23, 255, 0.5)',
    },
    yellow: {
      gradient: 'linear-gradient(90deg, #fef86c, #fef986)',
      shadow: 'rgba(254, 248, 108, 0.5)',
    },
  };

  const colorConfig = colorMap[color];

  return (
    <Box sx={{ width: '100%' }}>
      {/* Label и значения */}
      {!compact && (label || showValues) && (
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 0.5 }}>
          {label && (
            <Typography 
              variant="caption" 
              sx={{ 
                fontSize: cyberpunkTokens.fonts.xs, // 0.65rem - очень мелкий!
                fontWeight: 600,
                textTransform: 'uppercase',
                letterSpacing: '0.05em',
                color: 'text.secondary',
              }}
            >
              {label}
            </Typography>
          )}
          {showValues && (
            <Typography 
              variant="caption" 
              sx={{ 
                fontSize: cyberpunkTokens.fonts.xs, // 0.65rem
                fontWeight: 'bold',
                fontFamily: cyberpunkTokens.fonts.mono,
              }}
            >
              {Math.round(current)} / {max}
            </Typography>
          )}
        </Box>
      )}
      
      {/* Прогресс-бар */}
      <LinearProgress 
        variant="determinate" 
        value={percent}
        sx={{
          height: compact ? 6 : 8,
          borderRadius: cyberpunkTokens.borderRadius.sm,
          backgroundColor: cyberpunkTokens.colors.darkBg,
          border: '1px solid rgba(255, 255, 255, 0.1)',
          '& .MuiLinearProgress-bar': {
            background: colorConfig.gradient,
            boxShadow: `0 0 10px ${colorConfig.shadow}`,
            borderRadius: cyberpunkTokens.borderRadius.sm,
          },
        }}
      />
    </Box>
  );
}

