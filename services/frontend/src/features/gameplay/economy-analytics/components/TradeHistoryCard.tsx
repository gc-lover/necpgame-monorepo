import React from 'react'
import { Typography, Stack, Box } from '@mui/material'
import HistoryIcon from '@mui/icons-material/History'
import { CompactCard } from '@/shared/ui/cards'
import { cyberpunkTokens } from '@/shared/theme/cyberpunk'

export interface TradeHistoryCardProps {
  statistics?: {
    totalTrades?: number
    profitableTrades?: number
    winRate?: number
    averageProfit?: number
  }
  trades?: { tradeId?: string; result?: 'win' | 'loss'; profit?: number; timestamp?: string }[]
}

export const TradeHistoryCard: React.FC<TradeHistoryCardProps> = ({ statistics, trades = [] }) => (
  <CompactCard color="purple" glowIntensity="weak" compact>
    <Stack spacing={0.5}>
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Box display="flex" alignItems="center" gap={0.6}>
          <HistoryIcon sx={{ fontSize: '1rem', color: 'secondary.main' }} />
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.sm} fontWeight="bold">
            Trade History
          </Typography>
        </Box>
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Trades: {statistics?.totalTrades ?? trades.length}
        </Typography>
      </Box>
      {statistics && (
        <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
          Win rate: {(statistics.winRate ?? 0).toFixed(1)}% • Avg profit: {(statistics.averageProfit ?? 0).toLocaleString()}¥
        </Typography>
      )}
      <Stack spacing={0.2}>
        {trades.slice(0, 4).map((trade) => (
          <Box key={trade.tradeId ?? trade.timestamp} display="flex" justifyContent="space-between">
            <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
              {trade.timestamp ?? '—'}
            </Typography>
            <Typography
              variant="caption"
              fontSize={cyberpunkTokens.fonts.xs}
              sx={{ color: trade.result === 'loss' ? 'error.main' : 'success.main' }}
            >
              {trade.result === 'loss' ? '-' : '+'}
              {Math.abs(trade.profit ?? 0).toLocaleString()}¥
            </Typography>
          </Box>
        ))}
        {trades.length === 0 && (
          <Typography variant="caption" fontSize={cyberpunkTokens.fonts.xs} color="text.secondary">
            Сделки отсутствуют
          </Typography>
        )}
      </Stack>
    </Stack>
  </CompactCard>
)

export default TradeHistoryCard

