import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { PortfolioAnalyticsCard } from '../PortfolioAnalyticsCard'

describe('PortfolioAnalyticsCard', () => {
  it('renders portfolio stats', () => {
    render(
      <PortfolioAnalyticsCard
        characterId="char_123"
        totalValue={500000}
        profitLoss={100000}
        roiPercent={25}
        diversification={{ weapons: 40, implants: 30 }}
        topPerformers={[{ itemName: 'Legendary Rifle', roi: 150 }]}
      />,
    )

    expect(screen.getByText(/Portfolio Analytics/i)).toBeInTheDocument()
    expect(screen.getByText(/Legendary Rifle/i)).toBeInTheDocument()
  })
})

