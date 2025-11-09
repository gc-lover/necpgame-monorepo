/**
 * GameLayout - основной layout для игры
 * 
 * 3-колоночная сетка MMORPG:
 * - Левая панель (380px) - меню, действия
 * - Центр (flex) - основной контент
 * - Правая панель (320px) - персонаж, статы
 * 
 * КРИТИЧНО:
 * - Всё помещается на 1 экран (height: 100%)
 * - Используются маленькие шрифты (0.65rem - 0.875rem)
 * - Киберпанк стиль (неон, свечение, скосы)
 */

import { ReactNode } from 'react';
import { Box } from '@mui/material';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

/**
 * Пропсы для GameLayout
 */
export interface GameLayoutProps {
  /** Основной контент (центр) */
  children: ReactNode;
  /** Содержимое левой панели (меню, навигация) */
  leftPanel?: ReactNode;
  /** Содержимое правой панели (статистика, персонаж) */
  rightPanel?: ReactNode;
}

/**
 * Layout для игры с боковыми панелями
 * 
 * Использует Design Tokens для всех размеров и стилей
 */
export function GameLayout({ children, leftPanel, rightPanel }: GameLayoutProps) {
  return (
    <Box sx={{ display: 'flex', height: '100%', overflow: 'hidden' }}>
      {/* Левая панель */}
      {leftPanel && (
        <Box
          component="aside"
          sx={{
            width: cyberpunkTokens.sizes.leftPanel,
            borderRight: '2px solid',
            borderColor: 'divider',
            bgcolor: cyberpunkTokens.colors.darkBgAlpha,
            background: cyberpunkTokens.gradients.panelBg,
            boxShadow: cyberpunkTokens.effects.boxShadowPanelLeft,
            backdropFilter: cyberpunkTokens.effects.backdropBlur,
            display: 'flex',
            flexDirection: 'column',
            height: '100%',
            overflow: 'hidden',
          }}
        >
          <Box sx={{ p: 3, display: 'flex', flexDirection: 'column', height: '100%', overflow: 'hidden' }}>
            {leftPanel}
          </Box>
        </Box>
      )}

      {/* Основной контент по центру */}
      <Box 
        component="main" 
        sx={{ 
          flex: 1, 
          display: 'flex', 
          flexDirection: 'column', 
          minHeight: 0, 
          overflow: 'hidden' 
        }}
      >
        <Box 
          sx={{ 
            flex: 1, 
            display: 'flex', 
            flexDirection: 'column', 
            maxWidth: cyberpunkTokens.sizes.maxWidth, 
            mx: 'auto', 
            width: '100%', 
            p: 3, 
            overflowY: 'auto', 
            overflowX: 'hidden' 
          }}
        >
          {children}
        </Box>
      </Box>

      {/* Правая панель */}
      {rightPanel && (
        <Box
          component="aside"
          sx={{
            width: cyberpunkTokens.sizes.rightPanel,
            borderLeft: '2px solid',
            borderColor: 'divider',
            bgcolor: cyberpunkTokens.colors.darkBgAlpha,
            background: cyberpunkTokens.gradients.panelBg,
            boxShadow: cyberpunkTokens.effects.boxShadowPanelRight,
            backdropFilter: cyberpunkTokens.effects.backdropBlur,
            display: 'flex',
            flexDirection: 'column',
            height: '100%',
            overflow: 'hidden',
          }}
        >
          <Box sx={{ p: 3, display: 'flex', flexDirection: 'column', height: '100%', overflow: 'hidden' }}>
            {rightPanel}
          </Box>
        </Box>
      )}
    </Box>
  );
}

