import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ConvoyStatusCard } from '../ConvoyStatusCard'

describe('ConvoyStatusCard', () => {
  it('shows escort status', () => {
    render(
      <ConvoyStatusCard
        convoyStrength="82%"
        escorts=[
          {
            escortId: 'Alpha',
            members: 5,
            firepower: 'HIGH',
            status: 'READY',
          },
        ]
      />,
    )

    expect(screen.getByText(/Alpha/i)).toBeInTheDocument()
    expect(screen.getByText(/READY/i)).toBeInTheDocument()
  })
})


