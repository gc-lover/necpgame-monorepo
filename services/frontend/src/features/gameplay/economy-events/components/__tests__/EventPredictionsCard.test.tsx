import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EventPredictionsCard } from '../EventPredictionsCard'

describe('EventPredictionsCard', () => {
  it('shows predictions info', () => {
    render(
      <EventPredictionsCard
        predictions={[
          {
            predictedType: 'TRADE_WAR',
            probability: 62,
            timeframe: '7-10 days',
            notes: 'Sanctions between two factions likely',
          },
        ]}
      />,
    )

    expect(screen.getByText(/TRADE_WAR/i)).toBeInTheDocument()
    expect(screen.getByText(/7-10 days/i)).toBeInTheDocument()
  })
})


