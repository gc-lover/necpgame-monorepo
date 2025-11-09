/**
 * StatsPanel - панель статистики для правой стороны
 * 
 * Контейнер для карточек статистики
 */

import { ReactNode } from 'react';
import { Box } from '@mui/material';

export interface StatsPanelProps {
  children: ReactNode;
}

/**
 * Панель статистики
 * Используется в правой панели для группировки StatCard
 */
export function StatsPanel({ children }: StatsPanelProps) {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, height: '100%', pt: 0 }}>
      {children}
    </Box>
  );
}

