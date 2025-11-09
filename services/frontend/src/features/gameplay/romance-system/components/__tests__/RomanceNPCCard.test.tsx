import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RomanceNPCCard } from '../RomanceNPCCard'

describe('RomanceNPCCard', () => {
  it('renders NPC info', () => {
    render(
      <RomanceNPCCard
        npc={{
          npcId: 'npc-1',
          name: 'Judy Alvarez',
          region: 'Night City',
          orientation: 'BI',
          romanceDifficulty: 'MEDIUM',
          compatibilityScore: 78,
          personalityTraits: ['Creative', 'Loyal'],
          interests: ['Braindance', 'Tech'],
          currentStatus: 'DATING',
        }}
      />,
    )

    expect(screen.getByText(/Judy Alvarez/i)).toBeInTheDocument()
    expect(screen.getByText(/Night City/i)).toBeInTheDocument()
  })
})


