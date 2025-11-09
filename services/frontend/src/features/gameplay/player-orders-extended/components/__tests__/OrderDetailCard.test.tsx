import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { OrderDetailCard } from '../OrderDetailCard'

describe('OrderDetailCard', () => {
  it('renders detailed order info', () => {
    render(
      <OrderDetailCard
        order={{
          orderId: 'order-4488',
          customer: 'V',
          status: 'ESCROW',
          escrow: 15000,
          reputationImpact: 12,
          deliverables: ['Crafted katana', 'Delivery to Afterlife'],
          requirements: [
            { label: 'Skill', value: 'Blacksmith 8+' },
            { label: 'Materials', value: 'Neon alloy x5' },
          ],
        }}
      />,
    )

    expect(screen.getByText(/Order #order-4488/i)).toBeInTheDocument()
    expect(screen.getByText(/Blacksmith 8+/i)).toBeInTheDocument()
  })
})


