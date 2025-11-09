import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { OrderEconomyCard } from '../OrderEconomyCard'

describe('OrderEconomyCard', () => {
  it('renders economy metrics', () => {
    render(
      <OrderEconomyCard
        economy={{
          totalVolume: 128000,
          escrowLocked: 42000,
          averageFee: 7.5,
          premiumOrders: 24,
          recurringOrders: 18,
          marketDemand: 72,
        }}
      />,
    )

    expect(screen.getByText(/Order Economy/i)).toBeInTheDocument()
    expect(screen.getByText(/128,000¥/i)).toBeInTheDocument()
  })
})


