import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ClassQuestCard } from '../../components/ClassQuestCard'

describe('ClassQuestCard', () => {
  it('renders class quest info', () => {
    render(
      <ClassQuestCard
        quest={{
          questId: 'quest-1',
          name: 'Breaking the Deal',
          questType: 'CLASS',
          description: 'Sabotage corpo shipment.',
          rewards: ['Street cred'],
        }}
      />,
    )

    expect(screen.getByText(/Breaking the Deal/i)).toBeInTheDocument()
    expect(screen.getByText(/Sabotage corpo shipment/i)).toBeInTheDocument()
  })
})


