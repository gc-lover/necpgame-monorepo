import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { WorldQuestCard } from '../../components/WorldQuestCard'

describe('WorldQuestCard', () => {
  it('renders world quest info', () => {
    render(
      <WorldQuestCard
        quest={{
          questId: 'world-1',
          name: 'Debt Collection',
          faction: 'Arasaka',
          description: 'Collect debts worldwide',
          regionImpact: 'Arasaka control +5%',
        }}
      />,
    )

    expect(screen.getByText(/Debt Collection/i)).toBeInTheDocument()
    expect(screen.getByText(/Arasaka control/i)).toBeInTheDocument()
  })
})


