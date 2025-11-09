import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TravelEncounterCard } from '../../TravelEncounterCard'

describe('TravelEncounterCard', () => {
  it('renders encounter summary', () => {
    render(
      <TravelEncounterCard
        encounter={{
          period: '2078-2093',
          mode: 'FAST_TRAVEL',
          riskLevel: 0.2,
          modifiers: ['Security high'],
          rewards: ['Hidden market'],
        }}
      />,
    )

    expect(screen.getByText(/FAST_TRAVEL/i)).toBeInTheDocument()
    expect(screen.getByText(/Hidden market/i)).toBeInTheDocument()
  })
})



