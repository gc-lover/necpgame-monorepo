/**
 * WorldEventCard - карточка мирового события
 * 
 * ⭐ ИСПОЛЬЗУЕТ НОВУЮ БИБЛИОТЕКУ КОМПОНЕНТОВ из shared/!
 */

import React from 'react';
import { Typography, Stack, Chip, Box } from '@mui/material';
import PublicIcon from '@mui/icons-material/Public';
import { CompactCard } from '@/shared/ui/cards';
import { CyberpunkButton } from '@/shared/ui/buttons';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

interface WorldEventCardProps {
  event: {
    event_id?: string;
    title?: string;
    era?: string;
    event_type?: string;
    description?: string;
    impact_level?: string;
    status?: string;
  };
  onView?: () => void;
}

export const WorldEventCard: React.FC<WorldEventCardProps> = ({ event, onView }) => {
  const getTypeColor = (type?: string) => {
    switch (type) {
      case 'GLOBAL': return 'pink';
      case 'REGIONAL': return 'cyan';
      case 'LOCAL': return 'green';
      case 'FACTION': return 'purple';
      case 'ECONOMIC': return 'yellow';
      case 'TECHNOLOGICAL': return 'info';
      default: return 'default';
    }
  };

  const getImpactColor = (impact?: string) => {
    switch (impact) {
      case 'CRITICAL': return 'error';
      case 'HIGH': return 'warning';
      case 'MEDIUM': return 'info';
      case 'LOW': return 'success';
      default: return 'default';
    }
  };

  return (
    <CompactCard color={getTypeColor(event.event_type) as any} glowIntensity="normal" compact>
      <Stack spacing={0.5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box display="flex" alignItems="center" gap={0.5}>
            <PublicIcon sx={{ fontSize: '1rem', color: 'primary.main' }} />
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
          {event.era && <Chip label={event.era} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />}
          {event.event_type && <Chip label={event.event_type} size="small" color={getTypeColor(event.event_type) as any} sx={{ height: 14, fontSize: '0.55rem' }} />}
          {event.impact_level && <Chip label={`Impact: ${event.impact_level}`} size="small" color={getImpactColor(event.impact_level) as any} sx={{ height: 14, fontSize: '0.55rem' }} />}
          {event.status && <Chip label={event.status} size="small" sx={{ height: 14, fontSize: '0.55rem' }} />}
        </Box>
        {onView && (
          <CyberpunkButton variant="primary" size="small" fullWidth onClick={onView}>
            Просмотреть детали
          </CyberpunkButton>
        )}
      </Stack>
    </CompactCard>
  );
};

