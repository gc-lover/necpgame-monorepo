/**
 * MenuPanel - панель меню для левой стороны
 * 
 * Контейнер для элементов меню
 */

import { ReactNode } from 'react';
import { Box } from '@mui/material';

export interface MenuPanelProps {
  children: ReactNode;
}

/**
 * Панель меню
 * Используется в левой панели для группировки MenuItem
 */
export function MenuPanel({ children }: MenuPanelProps) {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
      {children}
    </Box>
  );
}

