import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TradeRouteCard } from '../TradeRouteCard'

describe('TradeRouteCard', () => {
  it('renders route info', () => {
    const route = {
      route_id: 'route_001',
      name: 'Trans-Pacific',
      from_hub: 'Night City',
      to_hub: 'Tokyo',
      distance_km: 8500,
      delivery_time_hours: 72,
      base_profit_margin: 25,
      risk_level: 'medium',
    }
    render(<TradeRouteCard route={route} />)
    expect(screen.getByText('Trans-Pacific')).toBeInTheDocument()
    expect(screen.getByText(/Night City.*Tokyo/)).toBeInTheDocument()
    expect(screen.getByText('MEDIUM')).toBeInTheDocument()
  })
})

