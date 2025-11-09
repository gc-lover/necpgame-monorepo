import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SynergiesCard } from '../SynergiesCard'

describe('SynergiesCard', () => {
  it('renders synergies list', () => {
    render(
      <SynergiesCard
        synergies={[
          { name: 'Blade Dancer', description: 'Blades + Reflexes boost crit chance.', bonus: '+8% crit' },
        ]}
      />,
    )

    expect(screen.getByText(/Blade Dancer/i)).toBeInTheDocument()
    expect(screen.getByText(/Blades \+ Reflexes boost crit chance\./i)).toBeInTheDocument()
  })
})

