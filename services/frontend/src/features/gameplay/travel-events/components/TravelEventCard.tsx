/**
 * TravelEventCard - карточка travel события
 * 
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Chip, Box } from '@mui/material';
import DirectionsWalkIcon from '@mui/icons-material/DirectionsWalk';
import { CompactCard } from '@/shared/ui/cards';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';
import { TravelEvent } from '../types';

interface TravelEventCardProps {
  event: TravelEvent;
}

const periodColor: Record<string, 'default' | 'pink' | 'purple' | 'yellow' | 'cyan'> = {
  '2030-2045': 'default',
  '2045-2060': 'pink',
  '2060-2077': 'purple',
  '2077': 'yellow',
  '2078-2093': 'cyan',
};

export const TravelEventCard: React.FC<TravelEventCardProps> = ({ event }) => {
  const color = periodColor[event.period] ?? 'default';

  return (
    <CompactCard color={color} glowIntensity="weak" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <DirectionsWalkIcon sx={{ fontSize: '1rem', color: 'info.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {event.name}
            </Typography>
          </Box>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          {event.description}
        </Typography>
        <Box display="flex" gap={0.3} flexWrap="wrap">
          <Chip label={event.period} size="small" color={color} sx={{ height: 14, fontSize: '0.55rem' }} />
          {event.locationTypes.map((type) => (
            <Chip key={type} label={type} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />
          ))}
          <Chip label={`${Math.round(event.triggerChance * 100)}%`} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />
        </Box>
      </Stack>
    </CompactCard>
  );
};

