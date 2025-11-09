import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { DaemonCard } from '../DaemonCard'

describe('DaemonCard', () => {
  it('renders daemon info', () => {
    const daemon = {
      daemon_id: 'daemon_overheat',
      name: 'Overheat',
      type: 'enemy' as const,
      tier: 3,
      ram_cost: 4,
      heat_generation: 25,
      cooldown: 8,
    }
    render(<DaemonCard daemon={daemon} />)
    expect(screen.getByText('Overheat')).toBeInTheDocument()
    expect(screen.getByText('T3')).toBeInTheDocument()
  })
})

