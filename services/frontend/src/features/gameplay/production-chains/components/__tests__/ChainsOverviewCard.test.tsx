import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ChainsOverviewCard } from '../ChainsOverviewCard'

describe('ChainsOverviewCard', () => {
  it('renders production chains overview', () => {
    render(
      <ChainsOverviewCard
        chains=[
          {
            chainId: 'chain-1',
            name: 'Legendary Weapons',
            category: 'WEAPONS',
            stages: 5,
            cycleTime: '4h',
            status: 'optimal',
          },
        ]
      />,
    )

    expect(screen.getByText(/Legendary Weapons/i)).toBeInTheDocument()
    expect(screen.getByText(/WEAPONS/i)).toBeInTheDocument()
  })
})


