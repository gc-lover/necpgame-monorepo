import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { DialogueGeneratorCard } from '../../components/DialogueGeneratorCard'

describe('DialogueGeneratorCard', () => {
  it('renders dialogue summary', () => {
    render(
      <DialogueGeneratorCard
        dialogue={{
          npcName: 'Judy',
          tone: 'INTIMATE',
          stage: 'Act II',
          dialogueText: 'Ready for another dive?',
          choices: [{ id: '1', text: 'Yes', impact: '+affection' }],
        }}
      />,
    )

    expect(screen.getByText(/Dialogue Generator/i)).toBeInTheDocument()
    expect(screen.getByText(/Ready for another dive/i)).toBeInTheDocument()
  })
})


