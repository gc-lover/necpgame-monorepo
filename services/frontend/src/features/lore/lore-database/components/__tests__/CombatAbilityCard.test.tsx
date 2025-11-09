import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CombatAbilityCard } from '../../components/CombatAbilityCard'

describe('CombatAbilityCard', () => {
  it('renders ability info', () => {
    render(
      <CombatAbilityCard
        ability={{
          name: 'Synapse Burst',
          category: 'ACTIVE',
          description: 'EMP stun',
          cooldown: '30s',
          synergy: 'Combos with breach',
        }}
      />,
    )

    expect(screen.getByText(/Synapse Burst/i)).toBeInTheDocument()
    expect(screen.getByText(/EMP stun/i)).toBeInTheDocument()
  })
})


