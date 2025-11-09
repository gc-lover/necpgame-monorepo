import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { OrderReputationCard } from '../OrderReputationCard'

describe('OrderReputationCard', () => {
  it('shows reputation info', () => {
    render(
      <OrderReputationCard
        reputation={{
          executorId: 'exec-77',
          name: 'Mercenary Squad Alpha',
          tier: 'GOLD',
          score: 820,
          completedOrders: 112,
          cancelRate: 3,
          reviewsPositive: 98,
          reviewsNegative: 4,
        }}
      />,
    )

    expect(screen.getByText(/Mercenary Squad Alpha/i)).toBeInTheDocument()
    expect(screen.getByText(/Score: 820/i)).toBeInTheDocument()
  })
})


