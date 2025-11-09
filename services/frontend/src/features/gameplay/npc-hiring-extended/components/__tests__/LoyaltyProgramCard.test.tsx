import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { LoyaltyProgramCard } from '../LoyaltyProgramCard'

describe('LoyaltyProgramCard', () => {
  it('renders loyalty tiers', () => {
    render(
      <LoyaltyProgramCard
        programName="Night Market"
        currentPoints={320}
        nextTierPoints={400}
        tiers={[
          { tier: 'Bronze', benefits: '+5% loyalty gain', requiredPoints: 100, unlocked: true },
          { tier: 'Silver', benefits: 'Unique contracts', requiredPoints: 250, unlocked: true },
          { tier: 'Gold', benefits: 'Exclusive missions', requiredPoints: 400, unlocked: false },
        ]}
      />,
    )

    expect(screen.getByText(/Night Market/i)).toBeInTheDocument()
    expect(screen.getByText(/Exclusive missions/i)).toBeInTheDocument()
  })
})


