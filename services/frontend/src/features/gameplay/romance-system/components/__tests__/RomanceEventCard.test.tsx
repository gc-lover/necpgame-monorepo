import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RomanceEventCard } from '../RomanceEventCard'

describe('RomanceEventCard', () => {
  it('shows event data', () => {
    render(
      <RomanceEventCard
        event={{
          eventId: 'event-1',
          name: 'Neon Rooftop Date',
          stage: 'DATING',
          description: 'Romantic dinner with Night City skyline.',
          location: 'Japantown Rooftop',
          durationMinutes: 90,
          affectionImpact: { min: 10, max: 25 },
          requiredAffection: 60,
        }}
      />,
    )

    expect(screen.getByText(/Neon Rooftop Date/i)).toBeInTheDocument()
    expect(screen.getByText(/Japantown Rooftop/i)).toBeInTheDocument()
  })
})


