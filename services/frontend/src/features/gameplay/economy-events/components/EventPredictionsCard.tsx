import React from 'react'
import { Typography, Stack, Box, Chip } from '@mui/material'
import AutoFixHighIcon from '@mui/icons-material/AutoFixHigh'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface EventPrediction {
  predictedType: string
  probability: number
  timeframe: string
  notes?: string
}

export interface EventPredictionsCardProps {
  predictions: EventPrediction[]
}

export const EventPredictionsCard: React.FC<EventPredictionsCardProps> = ({ predictions }) => (
  <CompactCard color="yellow" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <AutoFixHighIcon sx={{ fontSize: '1rem', color: 'warning.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            AI Predictions
          </Typography>
        </Box>
        <Chip
          label={predictions.length}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Stack spacing={0.3}>
        {predictions.map((prediction, index) => (
          <Box key={`${prediction.predictedType}-${index}`} display="flex" flexDirection="column" gap={0.1}>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} fontWeight="bold">
              {prediction.predictedType} · {prediction.probability}%
            </Typography>
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              Timeframe: {prediction.timeframe}
            </Typography>
            {prediction.notes && (
              <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
                {prediction.notes}
              </Typography>
            )}
          </Box>
        ))}
        {predictions.length === 0 && (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Предсказаний нет
          </Typography>
        )}
      </Stack>
    </Stack>
  </CompactCard>
)

export default EventPredictionsCard


