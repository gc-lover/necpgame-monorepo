/**
 * ProgressBar - универсальный прогресс-бар
 * 
 * Компактный прогресс-бар для XP, навыков, заданий
 * 
 * КРИТИЧНО:
 * - Маленький шрифт (0.65rem)
 * - Неоновое свечение
 * - Компактная высота
 */

import { Box, LinearProgress, Typography } from '@mui/material';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface ProgressBarProps {
  /** Текущее значение (0-100) */
  value: number;
  /** Название */
  label?: string;
  /** Цвет */
  color?: 'cyan' | 'pink' | 'green' | 'purple' | 'yellow';
  /** Показывать процент */
  showPercent?: boolean;
  /** Компактный режим */
  compact?: boolean;
  /** Кастомный текст вместо процента */
  customText?: string;
}

/**
 * Универсальный прогресс-бар
 * 
 * Используется для XP, навыков, прогресса заданий
 */
export function ProgressBar({ 
  value, 
  label,
  color = 'cyan',
  showPercent = true,
  compact = false,
  customText,
}: ProgressBarProps) {
  const percent = Math.min(100, Math.max(0, value));
  
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
  const displayText = customText || (showPercent ? `${Math.round(percent)}%` : '');

  return (
    <Box sx={{ width: '100%' }}>
      {/* Label и процент */}
      {!compact && (label || displayText) && (
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
          {displayText && (
            <Typography 
              variant="caption" 
              sx={{ 
                fontSize: cyberpunkTokens.fonts.xs, // 0.65rem
                fontWeight: 'bold',
                fontFamily: cyberpunkTokens.fonts.mono,
              }}
            >
              {displayText}
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

