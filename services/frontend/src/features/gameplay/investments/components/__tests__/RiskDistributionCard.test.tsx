import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RiskDistributionCard } from '../RiskDistributionCard'

describe('RiskDistributionCard', () => {
  it('renders risk levels', () => {
    render(
      <RiskDistributionCard
        distribution={[
          { level: 'LOW', percent: 40 },
          { level: 'HIGH', percent: 20 },
        ]}
      />,
    )

    expect(screen.getByText(/LOW/i)).toBeInTheDocument()
    expect(screen.getByText(/20%/i)).toBeInTheDocument()
  })
})


