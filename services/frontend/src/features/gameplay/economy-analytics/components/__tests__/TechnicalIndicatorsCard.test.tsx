import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TechnicalIndicatorsCard } from '../TechnicalIndicatorsCard'

describe('TechnicalIndicatorsCard', () => {
  it('renders indicators list', () => {
    render(
      <TechnicalIndicatorsCard
        timeframe="1d"
        indicators=[
          { label: 'RSI', value: 68.5, signal: 'overbought' },
          { label: 'MACD', value: '+2.2', signal: 'bullish' },
        ]
      />,
    )

    expect(screen.getByText(/Technical Indicators/i)).toBeInTheDocument()
    expect(screen.getByText(/RSI/i)).toBeInTheDocument()
  })
})

