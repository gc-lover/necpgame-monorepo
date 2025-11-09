import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RomanceChoiceCard } from '../RomanceChoiceCard'

describe('RomanceChoiceCard', () => {
  it('lists choices for event instance', () => {
    render(
      <RomanceChoiceCard
        instance={{
          instanceId: 'inst-1',
          eventName: 'Night Market Walk',
          stage: 'FRIENDSHIP',
          choices: [
            { choiceId: 'c1', text: 'Buy street food', affectionChange: 5, skillCheck: null },
            { choiceId: 'c2', text: 'Gift custom cyberware', affectionChange: 12, skillCheck: 'TECH 14' },
          ],
        }}
      />,
    )

    expect(screen.getByText(/Night Market Walk/i)).toBeInTheDocument()
    expect(screen.getByText(/Gift custom cyberware/i)).toBeInTheDocument()
  })
})


