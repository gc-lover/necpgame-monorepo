import React from 'react'
import { Typography, Stack, Box, Chip, LinearProgress } from '@mui/material'
import PsychologyIcon from '@mui/icons-material/Psychology'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface MarketSentimentCardProps {
  market?: string
  bullBearRatio?: number
  volumeTrend?: 'rising' | 'falling' | 'flat'
  momentum?: 'bullish' | 'bearish' | 'neutral'
  notes?: string[]
}

const momentumColor: Record<string, string> = {
  bullish: '#05ffa1',
  bearish: '#ff2a6d',
  neutral: '#00f7ff',
}

export const MarketSentimentCard: React.FC<MarketSentimentCardProps> = ({
  market = 'all',
  bullBearRatio = 0.5,
  volumeTrend = 'flat',
  momentum = 'neutral',
  notes = [],
}) => (
  <CompactCard color="green" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <PsychologyIcon sx={{ fontSize: '1rem', color: 'success.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Market Sentiment
          </Typography>
        </Box>
        <Chip
          label={market.toUpperCase()}
          size="small"
          sx={{ height: 16, fontSize: cyberpunkTokens.fonts.xs }}
        />
      </Box>
      <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
        Bull / Bear Ratio: {(bullBearRatio * 100).toFixed(0)}%
      </Typography>
      <LinearProgress
        variant="determinate"
        value={Math.min(Math.max(bullBearRatio * 100, 0), 100)}
        sx={{ height: 4, borderRadius: 1, bgcolor: 'rgba(255,255,255,0.08)' }}
      />
      <Box display="flex" justifyContent="space-between">
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Volume: {volumeTrend}
        </Typography>
        <Typography
          variant="caption"
          fontSize={cyberpunkTokens.fonts.xs}
          sx={{ color: momentumColor[momentum] ?? 'text.secondary' }}
        >
          Momentum: {momentum}
        </Typography>
      </Box>
      {notes.length > 0 && (
        <Stack spacing={0.2}>
          {notes.slice(0, 3).map((note, index) => (
            <Typography key={`${note}-${index}`} variant="caption" fontSize={cyberpunkTokens.fonts.xs}>
              • {note}
            </Typography>
          ))}
        </Stack>
      )}
    </Stack>
  </CompactCard>
)

export default MarketSentimentCard

