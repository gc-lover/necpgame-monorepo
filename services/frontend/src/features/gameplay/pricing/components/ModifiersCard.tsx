import React from 'react';
import { Typography, Stack, Box } from '@mui/material';
import TuneIcon from '@mui/icons-material/Tune';
import { CompactCard } from '@/shared/ui/cards';
import { cyberpunkTokens } from '@/shared/theme/cyberpunk';

export interface ModifiersCardProps {
  region?: string;
  regionalModifiers?: Record<string, number>;
  factionModifiers?: Record<string, number>;
  eventModifiers?: { name?: string; description?: string; value?: number }[];
}

export const ModifiersCard: React.FC<ModifiersCardProps> = ({
  region,
  regionalModifiers,
  factionModifiers,
  eventModifiers,
}) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.75}>
          <TuneIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Active Modifiers
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Region: {region ?? 'GLOBAL'}
        </Typography>
      </Box>
      {regionalModifiers && (
        <Stack spacing={0.2}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Regional Modifiers
          </Typography>
          {Object.entries(regionalModifiers).map(([key, value]) => (
            <Typography key={key} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • {key}: {value}
            </Typography>
          ))}
        </Stack>
      )}
      {factionModifiers && (
        <Stack spacing={0.2}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Faction Modifiers
          </Typography>
          {Object.entries(factionModifiers).map(([key, value]) => (
            <Typography key={key} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • {key}: {value}
            </Typography>
          ))}
        </Stack>
      )}
      {eventModifiers && eventModifiers.length > 0 && (
        <Stack spacing={0.2}>
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Event Modifiers
          </Typography>
          {eventModifiers.map((event, index) => (
            <Typography key={`${event.name}-${index}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • {event.name ?? 'Event'} ({event.value ?? 0}): {event.description ?? '—'}
            </Typography>
          ))}
        </Stack>
      )}
    </Stack>
  </CompactCard>
);

export default ModifiersCard;


