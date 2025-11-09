import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ClassBonusesCard } from '../ClassBonusesCard'

describe('ClassBonusesCard', () => {
  it('renders class bonuses', () => {
    render(
      <ClassBonusesCard
        className="Solo"
        focus="STR / CON"
        bonuses={[
          { bonus: 'Damage', value: '+12%', description: 'Melee weapons' },
        ]}
      />,
    )

    expect(screen.getByText(/Solo/i)).toBeInTheDocument()
    expect(screen.getByText(/Damage/i)).toBeInTheDocument()
  })
})


