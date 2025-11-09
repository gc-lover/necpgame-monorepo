import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { QueueStatusCard } from '../../components/QueueStatusCard'

describe('QueueStatusCard', () => {
  it('renders queue data', () => {
    render(
      <QueueStatusCard
        status={{
          mode: 'RANKED_PVP',
          population: 820,
          estimatedWait: '01:10',
          inReadyCheck: 40,
          activeTickets: 110,
        }}
      />,
    )

    expect(screen.getByText(/RANKED_PVP/i)).toBeInTheDocument()
    expect(screen.getByText(/Estimated wait/i)).toBeInTheDocument()
  })
})


