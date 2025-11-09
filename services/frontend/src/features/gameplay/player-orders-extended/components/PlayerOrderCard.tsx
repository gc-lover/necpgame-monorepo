/**
 * PlayerOrderCard - карточка заказа игрока
 * 
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Chip, Box } from '@mui/material';
import AssignmentIcon from '@mui/icons-material/Assignment';
import { CompactCard } from '@/shared/ui/cards';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

interface PlayerOrderCardProps {
  order: {
    order_id?: string;
    title?: string;
    description?: string;
    type?: string;
    difficulty?: string;
    payment?: number;
    currency?: string;
    status?: string;
    deadline?: string;
  };
  onAccept?: () => void;
}

export const PlayerOrderCard: React.FC<PlayerOrderCardProps> = ({ order, onAccept }) => {
  const getDifficultyColor = (diff?: string) => {
    switch (diff) {
      case 'EASY': return 'success';
      case 'MEDIUM': return 'info';
      case 'HARD': return 'warning';
      case 'EXPERT': return 'error';
      default: return 'default';
    }
  };

  return (
    <CompactCard color="cyan" glowIntensity="weak" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <AssignmentIcon sx={{ fontSize: '1rem', color: 'primary.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {order.title}
            </Typography>
          </Box>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {order.description}
        </Typography>
        <Box display="flex" gap={0.3} flexWrap="wrap">
          {order.type && <Chip label={order.type} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />}
          {order.difficulty && (
            <Chip 
              label={order.difficulty} 
              size="small" 
              color={getDifficultyColor(order.difficulty)}
              sx={{ height: 14, fontSize: '0.55rem' }} 
            />
          )}
          {order.payment && (
            <Chip 
              label={`${order.payment} ${order.currency || 'ЭД'}`} 
              size="small" 
              color="success"
              sx={{ height: 14, fontSize: '0.55rem' }} 
            />
          )}
        </Box>
        {onAccept && (
          <CyberpunkButton variant="primary" size="small" fullWidth onClick={onAccept}>
            Принять заказ
          </CyberpunkButton>
        )}
      </Stack>
    </CompactCard>
  );
};

