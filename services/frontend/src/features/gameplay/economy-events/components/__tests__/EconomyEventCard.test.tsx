import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EconomyEventCard } from '../EconomyEventCard'

describe('EconomyEventCard', () => {
  it('renders event summary', () => {
    render(
      <EconomyEventCard
        event={{
          eventId: 'evt-1234',
          name: 'Stock Market Crash',
          type: 'CRISIS',
          severity: 'MAJOR',
          startDate: '2077-11-01',
          endDate: null,
          isActive: true,
          affectedRegions: ['Night City', 'Badlands'],
          affectedSectors: ['Finance', 'Cyberware'],
        }}
      />,
    )

    expect(screen.getByText(/Stock Market Crash/i)).toBeInTheDocument()
    expect(screen.getByText(/Night City/i)).toBeInTheDocument()
  })
})


