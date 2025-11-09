import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CharacterCreationFlowCard } from '../../components/CharacterCreationFlowCard'

describe('CharacterCreationFlowCard', () => {
  it('renders creation flow steps', () => {
    render(
      <CharacterCreationFlowCard
        flow={{
          totalSteps: 6,
          estimatedMinutes: 12,
          tutorialEnabled: true,
          steps: [
            { id: 'step-1', name: 'Origin', description: 'Pick origin', mandatory: true },
          ],
        }}
      />,
    )

    expect(screen.getByText(/Character Creation Flow/i)).toBeInTheDocument()
    expect(screen.getByText(/Origin/i)).toBeInTheDocument()
  })
})


