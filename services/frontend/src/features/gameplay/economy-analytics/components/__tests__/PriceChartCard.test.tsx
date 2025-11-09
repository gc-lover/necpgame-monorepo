import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { PriceChartCard } from '../PriceChartCard'

describe('PriceChartCard', () => {
  it('renders chart info', () => {
    const chart = {
      item_id: 'item_001',
      chart_type: 'candlestick',
      timeframe: '1d',
      data_points: [
        {
          timestamp: '2025-11-06T00:00:00Z',
          open: 4500,
          high: 4800,
          low: 4400,
          close: 4700,
          volume: 125,
        },
      ],
    }
    render(<PriceChartCard chart={chart} />)
    expect(screen.getByText('item_001')).toBeInTheDocument()
    expect(screen.getByText('CANDLESTICK')).toBeInTheDocument()
  })
})

