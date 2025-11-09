import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { PortfolioOverviewCard } from '../PortfolioOverviewCard'

describe('PortfolioOverviewCard', () => {
  it('shows portfolio metrics', () => {
    render(
      <PortfolioOverviewCard
        portfolio={{
          totalValue: 1250000,
          investedCapital: 900000,
          unrealizedProfit: 350000,
          roiPercent: 21.7,
        }}
      />,
    )

    expect(screen.getByText(/Total value/i)).toBeInTheDocument()
    expect(screen.getByText(/1,250,000¥/i)).toBeInTheDocument()
  })
})


