import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { OrderBoardCard } from '../OrderBoardCard'

describe('OrderBoardCard', () => {
  it('renders order board entries', () => {
    render(
      <OrderBoardCard
        title="Highlighted Orders"
        orders={[
          {
            orderId: 'order-1',
            title: 'Craft legendary katana',
            type: 'CRAFTING',
            difficulty: 'EXPERT',
            payment: 48000,
            reputationRequired: 120,
            expiresInHours: 12,
            successRate: 64,
          },
        ]}
      />,
    )

    expect(screen.getByText(/Highlighted Orders/i)).toBeInTheDocument()
    expect(screen.getByText(/legendary katana/i)).toBeInTheDocument()
  })
})


