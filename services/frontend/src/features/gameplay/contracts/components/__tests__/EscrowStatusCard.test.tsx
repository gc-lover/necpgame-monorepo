import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EscrowStatusCard } from '../EscrowStatusCard'

describe('EscrowStatusCard', () => {
  it('renders escrow metrics', () => {
    render(
      <EscrowStatusCard
        status={{
          escrowId: 'escrow-1234',
          totalHeld: 20000,
          released: 8000,
          disputed: 2000,
          releaseCondition: 'Delivery confirmation',
        }}
      />,
    )

    expect(screen.getByText(/Escrow/i)).toBeInTheDocument()
    expect(screen.getByText(/20000¥/i)).toBeInTheDocument()
  })
})


