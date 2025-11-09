import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EnemyAICard } from '../../components/EnemyAICard'

describe('EnemyAICard', () => {
  it('renders enemy AI details', () => {
    render(
      <EnemyAICard
        enemy={{
          name: 'Ronin AI',
          tier: 'ADVANCED',
          aggression: 80,
          tactics: ['Blade rush'],
          weaknesses: ['EMP'],
        }}
      />,
    )

    expect(screen.getByText(/Ronin AI/i)).toBeInTheDocument()
    expect(screen.getByText(/ADVANCED/i)).toBeInTheDocument()
  })
})


