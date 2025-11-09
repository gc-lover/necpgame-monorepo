import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TravelEventsPeriodCard } from '../../TravelEventsPeriodCard'

describe('TravelEventsPeriodCard', () => {
  it('renders period summary', () => {
    render(
      <TravelEventsPeriodCard
        period={{
          period: '2077',
          eraCharacteristics: { turmoil: 'Corporate war spillover' },
          events: [
            {
              eventId: 'evt-1',
              name: 'Ambush',
              period: '2077',
              locationTypes: ['BADLANDS'],
              triggerChance: 0.3,
              description: 'Scav ambush',
              choices: [],
              outcomes: [],
            },
          ],
        }}
      />,
    )

    expect(screen.getByText(/Era Overview/i)).toBeInTheDocument()
    expect(screen.getByText(/Corporate war spillover/i)).toBeInTheDocument()
  })
})



