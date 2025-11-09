import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { InitialDataCard } from '../InitialDataCard'

describe('InitialDataCard', () => {
  it('shows starter data', () => {
    render(
      <InitialDataCard
        starterItems={[{ itemId: 'item-123456', quantity: 2 }]}
        starterQuests={['First Blood']}
        starterLocations={['Night City']}
        npcs={[{ npcId: 'npc-1', name: 'Fixer Dex', location: 'Watson', role: 'Quest giver' }]}
      />,
    )

    expect(screen.getByText(/item-12/i)).toBeInTheDocument()
    expect(screen.getByText(/Fixer Dex/i)).toBeInTheDocument()
  })
})


