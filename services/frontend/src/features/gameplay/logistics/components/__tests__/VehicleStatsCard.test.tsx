import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { VehicleStatsCard } from '../VehicleStatsCard'

describe('VehicleStatsCard', () => {
  it('renders vehicle stats', () => {
    render(
      <VehicleStatsCard
        vehicles=[
          {
            type: 'TRUCK',
            speed: '60 km/h',
            capacity: '4000 kg',
            risk: 'MEDIUM',
            cost: '1200¥',
          },
        ]
      />,
    )

    expect(screen.getByText(/TRUCK/i)).toBeInTheDocument()
    expect(screen.getByText(/4000 kg/i)).toBeInTheDocument()
  })
})


