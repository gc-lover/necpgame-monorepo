import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ServerListCard } from '../../components/ServerListCard'

describe('ServerListCard', () => {
  it('shows server entries', () => {
    render(
      <ServerListCard
        servers={[
          {
            serverId: 'srv-eu-1',
            name: 'EU Nightfall',
            region: 'EU',
            population: 'HIGH',
            ping: 42,
            status: 'ONLINE',
          },
        ]}
      />,
    )

    expect(screen.getByText(/EU Nightfall/i)).toBeInTheDocument()
  })
})


