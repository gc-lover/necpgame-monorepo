import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { DisputeCard } from '../DisputeCard'

describe('DisputeCard', () => {
  it('shows dispute details', () => {
    render(
      <DisputeCard
        dispute={{
          disputeId: 'disp-1111',
          contractId: 'contract-2222',
          status: 'IN_REVIEW',
          filedBy: 'char-AAA111',
          assignedArbiter: 'arb-5555',
          evidenceCount: 3,
        }}
      />,
    )

    expect(screen.getByText(/Dispute/i)).toBeInTheDocument()
    expect(screen.getByText(/IN_REVIEW/i)).toBeInTheDocument()
  })
})


