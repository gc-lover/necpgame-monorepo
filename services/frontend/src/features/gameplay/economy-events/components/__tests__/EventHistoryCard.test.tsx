import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EventHistoryCard } from '../EventHistoryCard'

describe('EventHistoryCard', () => {
  it('renders history entries', () => {
    render(
      <EventHistoryCard
        history={[
          {
            eventId: 'evt-2001',
            name: 'Fuel Shortage',
            type: 'COMMODITY',
            severity: 'MAJOR',
            startDate: '2077-10-01',
            endDate: '2077-10-12',
            durationDays: 11,
            impactSummary: 'Logistics costs +35%',
          },
        ]}
      />,
    )

    expect(screen.getByText(/Fuel Shortage/i)).toBeInTheDocument()
    expect(screen.getByText(/Logistics costs/i)).toBeInTheDocument()
  })
})


