import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RealtimeInstanceCard } from '../../components/RealtimeInstanceCard'

describe('RealtimeInstanceCard', () => {
  it('renders basic instance information', () => {
    render(
      <RealtimeInstanceCard
        instance={{
          instanceId: 'rt-nyc-01',
          region: 'night-city/eu-west',
          status: 'ONLINE',
          tickRate: 30,
          maxPlayers: 1200,
          activePlayers: 900,
          maxZones: 6,
          supportedZoneTypes: ['urban'],
          metadata: {},
        }}
      />,
    )

    expect(screen.getByText(/rt-nyc-01/i)).toBeInTheDocument()
    expect(screen.getByText(/Players/i)).toBeInTheDocument()
  })
})
import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RealtimeInstanceCard } from '../../components/RealtimeInstanceCard'

describe('RealtimeInstanceCard', () => {
  it('renders instance info', () => {
    render(
      <RealtimeInstanceCard
        instance={{
          instanceId: 'rt-nyc-01',
          region: 'night-city/eu-west',
          status: 'ONLINE',
          tickRate: 30,
          maxPlayers: 1200,
          activePlayers: 980,
          maxZones: 6,
          supportedZoneTypes: ['urban'],
          metadata: {},
        }}
      />,
    )

    expect(screen.getByText(/rt-nyc-01/i)).toBeInTheDocument()
    expect(screen.getByText(/Players/i)).toBeInTheDocument()
  })
})

