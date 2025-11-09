import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { InsurancePlansCard } from '../InsurancePlansCard'

describe('InsurancePlansCard', () => {
  it('lists insurance plans', () => {
    render(
      <InsurancePlansCard
        plans=[
          {
            name: 'Premium',
            coverage: '100%',
            premium: '550¥',
            perks: ['Instant payout'],
          },
        ]
      />,
    )

    expect(screen.getByText(/Premium/i)).toBeInTheDocument()
    expect(screen.getByText(/Instant payout/i)).toBeInTheDocument()
  })
})


