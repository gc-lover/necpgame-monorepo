/**
 * RandomEventExtendedCard - карточка расширенного случайного события
 * 
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Chip, Box } from '@mui/material';
import EventIcon from '@mui/icons-material/Event';
import { CompactCard } from '@/shared/ui/cards';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

interface RandomEventExtendedCardProps {
  event: {
    event_id?: string;
    title?: string;
    period?: string;
    category?: string;
    location_type?: string;
    description?: string;
    risk_level?: string;
  };
  onTrigger?: () => void;
}

export const RandomEventExtendedCard: React.FC<RandomEventExtendedCardProps> = ({ event, onTrigger }) => {
  const getCategoryColor = (category?: string) => {
    switch (category) {
      case 'COMBAT': return 'pink';
      case 'SOCIAL': return 'green';
      case 'ECONOMY': return 'yellow';
      case 'EXPLORATION': return 'cyan';
      case 'FACTION': return 'purple';
      case 'STORY': return 'info';
      default: return 'default';
    }
  };

  const getRiskColor = (risk?: string) => {
    switch (risk) {
      case 'LOW': return 'success';
      case 'MEDIUM': return 'warning';
      case 'HIGH': return 'error';
      default: return 'default';
    }
  };

  return (
    <CompactCard color={getCategoryColor(event.category) as any} glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <EventIcon sx={{ fontSize: '1rem', color: 'primary.main' }} />
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
              {event.title}
            </Typography>
          </Box>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary" sx={{ 
          display: '-webkit-box', 
          WebkitLineClamp: 2, 
          WebkitBoxOrient: 'vertical', 
          overflow: 'hidden' 
        }}>
          {event.description}
        </Typography>
        <Box display="flex" gap={0.3} flexWrap="wrap">
          {event.period && <Chip label={event.period} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />}
          {event.category && <Chip label={event.category} size="small" color={getCategoryColor(event.category) as any} sx={{ height: 14, fontSize: '0.55rem' }} />}
          {event.location_type && <Chip label={event.location_type} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />}
          {event.risk_level && <Chip label={`Risk: ${event.risk_level}`} size="small" color={getRiskColor(event.risk_level) as any} sx={{ height: 14, fontSize: '0.55rem' }} />}
        </Box>
        {onTrigger && (
          <CyberpunkButton variant="primary" size="small" fullWidth onClick={onTrigger}>
            Триггернуть событие
          </CyberpunkButton>
        )}
      </Stack>
    </CompactCard>
  );
};

