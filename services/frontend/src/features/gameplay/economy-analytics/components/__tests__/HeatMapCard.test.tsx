import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { HeatMapCard } from '../HeatMapCard'

describe('HeatMapCard', () => {
  it('renders heat map items', () => {
    render(
      <HeatMapCard
        category="weapons"
        timeframe="1d"
        items=[
          { itemName: 'Neon Katana', priceChangePercent: 12, volumeChangePercent: 25 },
        ]
      />,
    )

    expect(screen.getByText(/Heat Map/i)).toBeInTheDocument()
    expect(screen.getByText(/Neon Katana/i)).toBeInTheDocument()
  })
})

