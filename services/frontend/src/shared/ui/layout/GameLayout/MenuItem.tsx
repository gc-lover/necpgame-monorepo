/**
 * MenuItem - элемент меню с киберпанк стилем
 * 
 * Кнопка меню с неоновыми эффектами и скошенными углами
 * 
 * КРИТИЧНО:
 * - Маленький шрифт (0.75rem)
 * - MMORPG стиль (скос углов)
 * - Неоновое свечение при активности
 */

import { ReactNode } from 'react';
import { Box } from '@mui/material';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface MenuItemProps {
  /** Иконка (React элемент) */
  icon?: ReactNode;
  /** Текст */
  label: string;
  /** Активен ли элемент */
  active?: boolean;
  /** Бейдж (например, количество уведомлений) */
  badge?: string;
  /** Обработчик клика */
  onClick?: () => void;
}

/**
 * Элемент меню
 * 
 * Компактная кнопка с киберпанк стилем
 */
export function MenuItem({ icon, label, active = false, badge, onClick }: MenuItemProps) {
  return (
    <Box
      component="button"
      onClick={onClick}
      sx={{
        width: '100%',
        px: 1.5,
        py: 1.25,
        display: 'flex',
        alignItems: 'center',
        gap: 1,
        border: '2px solid',
        borderColor: active ? 'primary.main' : 'rgba(255, 255, 255, 0.1)',
        bgcolor: active 
          ? 'rgba(0, 247, 255, 0.15)' 
          : cyberpunkTokens.colors.cardBgAlpha,
        background: active
          ? cyberpunkTokens.gradients.activeButton
          : cyberpunkTokens.gradients.normalButton,
        color: active ? 'primary.main' : 'text.primary',
        fontWeight: 'bold',
        textTransform: 'uppercase',
        letterSpacing: '0.05em',
        fontSize: cyberpunkTokens.fonts.sm, // 0.75rem - маленький!
        position: 'relative',
        overflow: 'hidden',
        cursor: 'pointer',
        transition: cyberpunkTokens.transitions.normal,
        boxShadow: active
          ? cyberpunkTokens.effects.boxShadowActive
          : cyberpunkTokens.effects.boxShadowCard,
        '&:hover': {
          borderColor: active ? 'primary.main' : 'rgba(0, 247, 255, 0.4)',
          bgcolor: active 
            ? 'rgba(0, 247, 255, 0.2)' 
            : 'rgba(26, 31, 58, 0.8)',
          boxShadow: active
            ? cyberpunkTokens.effects.boxShadowActiveHover
            : cyberpunkTokens.effects.boxShadowCardHover,
          transform: 'translateY(-1px)',
        },
        '&:active': {
          transform: 'translateY(0)',
        },
        // MMORPG стиль - скос углов
        clipPath: cyberpunkTokens.clipPath.corner,
      }}
    >
      {/* Градиентный фон для глубины */}
      <Box
        sx={{
          position: 'absolute',
          inset: 0,
          background: active
            ? cyberpunkTokens.gradients.depthActive
            : cyberpunkTokens.gradients.depth,
          opacity: 0.6,
          transition: cyberpunkTokens.transitions.normal,
        }}
      />
      
      {/* Светящаяся полоска слева для активного элемента */}
      {active && (
        <Box
          sx={{
            position: 'absolute',
            left: 0,
            top: '25%',
            bottom: '25%',
            width: '3px',
            bgcolor: 'primary.main',
            boxShadow: cyberpunkTokens.effects.neonGlowStrong,
            borderRadius: '0 2px 2px 0',
          }}
        />
      )}
      
      {/* Контент */}
      <Box sx={{ position: 'relative', display: 'flex', alignItems: 'center', gap: 1, flex: 1, zIndex: 10 }}>
        {icon && (
          <Box
            component="span"
            sx={{
              fontSize: '1.1rem',
              filter: active ? `drop-shadow(${cyberpunkTokens.effects.neonGlowWeak})` : 'none',
              transition: cyberpunkTokens.transitions.normal,
            }}
          >
            {icon}
          </Box>
        )}
        <Box 
          component="span" 
          sx={{ 
            flex: 1, 
            textAlign: 'left', 
            textShadow: active ? cyberpunkTokens.effects.neonGlowWeak : 'none' 
          }}
        >
          {label}
        </Box>
        {badge && (
          <Box
            component="span"
            sx={{
              fontSize: cyberpunkTokens.fonts.xs, // 0.65rem - очень мелкий!
              px: 1,
              py: 0.25,
              bgcolor: 'primary.main',
              color: 'black',
              fontWeight: 'bold',
              textTransform: 'uppercase',
              borderRadius: cyberpunkTokens.borderRadius.sm,
              boxShadow: '0 0 8px rgba(0, 247, 255, 0.5)',
            }}
          >
            {badge}
          </Box>
        )}
      </Box>
    </Box>
  );
}

