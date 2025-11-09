import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TextVersionStateCard } from '../TextVersionStateCard'

describe('TextVersionStateCard', () => {
  it('renders text version info', () => {
    render(
      <TextVersionStateCard
        state={{
          character: { name: 'V', level: 5, location: 'Watson', hp: 48, hpMax: 60 },
          availableActions: [
            { action: 'move', description: 'Move to new district', command: '/move' },
          ],
          currentQuest: { questName: 'Prologue', objectives: ['Meet Jackie'] },
          inventorySummary: { itemsCount: 12, weight: 24.5 },
          nearbyNPCs: [{ name: 'Jackie', canInteract: true }],
        }}
      />,
    )

    expect(screen.getByText(/V · Lvl 5/i)).toBeInTheDocument()
    expect(screen.getByText(/Meet Jackie/i)).toBeInTheDocument()
  })
})

