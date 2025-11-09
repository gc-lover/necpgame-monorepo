import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { PersonalityCard } from '../../components/PersonalityCard'

describe('PersonalityCard', () => {
  it('renders npc personality', () => {
    render(
      <PersonalityCard
        personality={{
          npcName: 'Fixer',
          template: 'Mentor',
          faction: 'Afterlife',
          region: 'NC',
          role: 'Questgiver',
          traits: [{ trait: 'Loyal', score: 0.8 }],
          quirks: ['Collects chrome'],
        }}
      />,
    )

    expect(screen.getByText(/NPC Personality/i)).toBeInTheDocument()
    expect(screen.getByText(/Mentor/i)).toBeInTheDocument()
  })
})


