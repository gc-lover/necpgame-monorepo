import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RoutesCard } from '../RoutesCard'

describe('RoutesCard', () => {
  it('shows routes data', () => {
    render(
      <RoutesCard
        routes=[
          {
            routeId: 'route-1',
            origin: 'Night City',
            destination: 'Watson',
            distance: '45km',
            risk: 'MEDIUM',
            recommendedVehicle: 'CAR',
          },
        ]
      />,
    )

    expect(screen.getByText(/Night City/i)).toBeInTheDocument()
    expect(screen.getByText(/CAR/i)).toBeInTheDocument()
  })
})


