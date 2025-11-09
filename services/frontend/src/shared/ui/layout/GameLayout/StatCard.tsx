/**
 * StatCard - карточка статистики
 * 
 * Компактная карточка для отображения статистики персонажа
 * 
 * КРИТИЧНО:
 * - Маленькие шрифты (0.65rem для label, 1.25rem для value)
 * - MMORPG стиль (скос углов)
 * - Неоновое свечение по цвету
 */

import { ReactNode } from 'react';
import { Box } from '@mui/material';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface StatCardProps {
  /** Иконка (React элемент) */
  icon?: ReactNode;
  /** Название стата */
  label: string;
  /** Значение */
  value: string | number;
  /** Цвет (определяет неоновое свечение) */
  color?: 'cyan' | 'pink' | 'green' | 'purple' | 'yellow';
}

/**
 * Карточка статистики
 * 
 * Компактная карточка с неоновым свечением
 */
export function StatCard({ icon, label, value, color = 'cyan' }: StatCardProps) {
  const colorMap = {
    cyan: { 
      main: 'primary.main', 
      shadow: 'rgba(0, 247, 255, 0.3)', 
      textShadow: cyberpunkTokens.effects.neonGlowWeak 
    },
    pink: { 
      main: cyberpunkTokens.colors.neonPink, 
      shadow: 'rgba(255, 42, 109, 0.3)', 
      textShadow: cyberpunkTokens.effects.neonGlowWeak 
    },
    green: { 
      main: 'success.main', 
      shadow: 'rgba(5, 255, 161, 0.3)', 
      textShadow: cyberpunkTokens.effects.neonGlowWeak 
    },
    purple: { 
      main: cyberpunkTokens.colors.neonPurple, 
      shadow: 'rgba(216, 23, 255, 0.3)', 
      textShadow: cyberpunkTokens.effects.neonGlowWeak 
    },
    yellow: { 
      main: cyberpunkTokens.colors.neonYellow, 
      shadow: 'rgba(254, 248, 108, 0.3)', 
      textShadow: cyberpunkTokens.effects.neonGlowWeak 
    },
  };

  const colorConfig = colorMap[color];

  return (
    <Box
      sx={{
        bgcolor: cyberpunkTokens.colors.cardBgAlpha,
        border: '2px solid',
        borderColor: colorConfig.main,
        p: 1.5,
        position: 'relative',
        overflow: 'hidden',
        transition: cyberpunkTokens.transitions.normal,
        background: cyberpunkTokens.gradients.cardBg,
        boxShadow: `0 4px 12px rgba(0, 0, 0, 0.5), 0 2px 6px rgba(0, 0, 0, 0.4), 0 0 15px ${colorConfig.shadow}, inset 0 1px 2px rgba(255, 255, 255, 0.05), inset 0 -1px 2px rgba(0, 0, 0, 0.3)`,
        clipPath: cyberpunkTokens.clipPath.corner,
        '&:hover': {
          transform: 'translateY(-2px)',
          boxShadow: `0 6px 20px rgba(0, 0, 0, 0.6), 0 3px 10px rgba(0, 0, 0, 0.5), 0 0 25px ${colorConfig.shadow}, inset 0 1px 2px rgba(255, 255, 255, 0.08), inset 0 -1px 2px rgba(0, 0, 0, 0.4)`,
        },
      }}
    >
      {/* Градиент для глубины */}
      <Box
        sx={{
          position: 'absolute',
          inset: 0,
          background: `linear-gradient(135deg, ${colorConfig.shadow} 0%, transparent 50%, rgba(0, 0, 0, 0.2) 100%)`,
          opacity: 0.25,
        }}
      />
      
      {/* Светящаяся полоска слева */}
      <Box
        sx={{
          position: 'absolute',
          left: 0,
          top: '20%',
          bottom: '20%',
          width: '3px',
          bgcolor: colorConfig.main,
          boxShadow: `0 0 8px ${colorConfig.main}, 0 0 15px ${colorConfig.main}`,
          borderRadius: '0 2px 2px 0',
        }}
      />
      
      <Box sx={{ position: 'relative', display: 'flex', alignItems: 'center', gap: 1.5, zIndex: 10 }}>
        {icon && (
          <Box
            component="span"
            sx={{
              fontSize: '1.25rem',
              color: colorConfig.main,
              filter: `drop-shadow(${colorConfig.textShadow})`,
            }}
          >
            {icon}
          </Box>
        )}
        <Box sx={{ flex: 1 }}>
          <Box 
            sx={{ 
              color: 'text.secondary', 
              fontSize: cyberpunkTokens.fonts.xs, // 0.65rem - очень мелкий!
              textTransform: 'uppercase', 
              letterSpacing: '0.08em',
              fontWeight: 600,
              mb: 0.25,
            }}
          >
            {label}
          </Box>
          <Box 
            sx={{ 
              fontSize: '1.25rem', 
              fontWeight: 'bold', 
              fontFamily: cyberpunkTokens.fonts.mono, 
              color: colorConfig.main,
              textShadow: colorConfig.textShadow,
            }}
          >
            {value}
          </Box>
        </Box>
      </Box>
    </Box>
  );
}

