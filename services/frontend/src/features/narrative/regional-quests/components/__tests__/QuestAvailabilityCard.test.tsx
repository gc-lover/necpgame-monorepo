import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { QuestAvailabilityCard } from '../../components/QuestAvailabilityCard'

describe('QuestAvailabilityCard', () => {
  it('renders availability data', () => {
    render(
      <QuestAvailabilityCard
        availability={{
          dailySlotsAvailable: 5,
          dailySlotsUsed: 2,
          weeklySlotsAvailable: 3,
          weeklySlotsUsed: 1,
          resetsAt: '03:00 NCST',
        }}
      />,
    )

    expect(screen.getByText(/Daily/i)).toBeInTheDocument()
    expect(screen.getByText(/03:00 NCST/i)).toBeInTheDocument()
  })
})


