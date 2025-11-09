import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TradeHistoryCard } from '../TradeHistoryCard'

describe('TradeHistoryCard', () => {
  it('renders trade statistics', () => {
    render(
      <TradeHistoryCard
        statistics={{ totalTrades: 100, winRate: 62.5, averageProfit: 5400 }}
        trades=[
          { tradeId: 'trade-1', result: 'win', profit: 1200, timestamp: '2077-11-07 15:40' },
        ]
      />,
    )

    expect(screen.getByText(/Trade History/i)).toBeInTheDocument()
    expect(screen.getByText(/2077-11-07 15:40/i)).toBeInTheDocument()
  })
})

