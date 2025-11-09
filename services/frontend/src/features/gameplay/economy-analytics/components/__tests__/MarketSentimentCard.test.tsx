import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MarketSentimentCard } from '../MarketSentimentCard'

describe('MarketSentimentCard', () => {
  it('renders sentiment data', () => {
    render(
      <MarketSentimentCard
        market="weapons"
        bullBearRatio={0.62}
        volumeTrend="rising"
        momentum="bullish"
        notes={['Strong buying activity']}
      />,
    )

    expect(screen.getByText(/Market Sentiment/i)).toBeInTheDocument()
    expect(screen.getByText(/Strong buying activity/i)).toBeInTheDocument()
  })
})

