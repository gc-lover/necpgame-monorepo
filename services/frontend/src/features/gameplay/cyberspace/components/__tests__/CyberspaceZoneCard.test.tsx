import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CyberspaceZoneCard } from '../CyberspaceZoneCard'

describe('CyberspaceZoneCard', () => {
  it('renders zone info', () => {
    const zone = {
      zone_id: 'zone-1',
      name: 'Night City Hub',
      type: 'hub' as const,
      access_level: 'basic' as const,
      is_pvp: false,
      player_count: 42,
      description: 'Main city hub',
    }
    render(<CyberspaceZoneCard zone={zone} />)
    expect(screen.getByText('Night City Hub')).toBeInTheDocument()
    expect(screen.getByText('hub')).toBeInTheDocument()
  })
})

