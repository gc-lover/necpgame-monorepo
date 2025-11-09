import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EconomyImpactCard } from '../EconomyImpactCard'

describe('EconomyImpactCard', () => {
  it('shows impact metrics', () => {
    render(
      <EconomyImpactCard
        impact={{
          timestamp: '2077-11-07T18:40:00Z',
          activeEventsCount: 4,
          overallMarketHealth: 'WEAK',
          priceIndexChange: -2.35,
          sectorImpacts: { weapons: 0.12, cyberware: -0.08 },
        }}
      />,
    )

    expect(screen.getByText(/Active events/i)).toBeInTheDocument()
    expect(screen.getByText(/-2.35%/i)).toBeInTheDocument()
  })
})


