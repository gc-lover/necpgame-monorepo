import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MatchTicketCard } from '../../components/MatchTicketCard'

describe('MatchTicketCard', () => {
  it('shows ticket details', () => {
    render(
      <MatchTicketCard
        ticket={{
          ticketId: 'MM-1234',
          mode: 'SCRIM',
          players: 10,
          latencyMs: 42,
          status: 'READY_CHECK',
          createdAt: '01:12 ago',
        }}
      />,
    )

    expect(screen.getByText(/MM-1234/i)).toBeInTheDocument()
    expect(screen.getByText(/Latency/i)).toBeInTheDocument()
  })
})


