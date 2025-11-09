import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { StarterProgressionCard } from '../../components/StarterProgressionCard'

describe('StarterProgressionCard', () => {
  it('renders progression steps', () => {
    render(
      <StarterProgressionCard
        progression={[
          { step: 1, questId: 'q1', questName: 'Tutorial', estimatedLevel: 1 },
          { step: 2, questId: 'q2', questName: 'First Run', estimatedLevel: 2 },
        ]}
      />,
    )

    expect(screen.getByText(/Tutorial/i)).toBeInTheDocument()
    expect(screen.getByText(/Lv 1/i)).toBeInTheDocument()
  })
})


