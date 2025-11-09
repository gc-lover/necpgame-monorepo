import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CharacterSelectCard } from '../../components/CharacterSelectCard'

describe('CharacterSelectCard', () => {
  it('shows character slots', () => {
    render(
      <CharacterSelectCard
        data={{
          maxSlots: 4,
          characters: [
            {
              characterId: 'char-1',
              name: 'V',
              className: 'Netrunner',
              level: 32,
              location: 'Night City',
              lastPlayed: '2h ago',
            },
          ],
        }}
      />,
    )

    expect(screen.getByText(/Character Select/i)).toBeInTheDocument()
    expect(screen.getByText(/Netrunner/i)).toBeInTheDocument()
  })
})


