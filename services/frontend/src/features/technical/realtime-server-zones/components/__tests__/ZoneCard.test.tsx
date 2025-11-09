import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ZoneCard } from '../../components/ZoneCard'

describe('ZoneCard', () => {
  it('renders zone summary', () => {
    render(
      <ZoneCard
        zone={{
          zoneId: 'zone-1',
          zoneName: 'Watson',
          status: 'ONLINE',
          assignedServerId: 'rt-nyc-01',
          playerCount: 420,
          npcCount: 320,
          isPvpEnabled: true,
        }}
      />,
    )

    expect(screen.getByText(/Watson/i)).toBeInTheDocument()
    expect(screen.getByText(/PvP/i)).toBeInTheDocument()
  })
})


