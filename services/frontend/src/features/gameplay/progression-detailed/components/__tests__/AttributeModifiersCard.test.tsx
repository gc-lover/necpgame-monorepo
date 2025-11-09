import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AttributeModifiersCard } from '../AttributeModifiersCard'

describe('AttributeModifiersCard', () => {
  it('renders modifiers', () => {
    render(
      <AttributeModifiersCard
        modifiers={[
          { attribute: 'INT', total: 78, base: 60, equipment: 10, buffs: 8 },
        ]}
      />,
    )

    expect(screen.getByText(/INT/i)).toBeInTheDocument()
    expect(screen.getByText(/Base 60/i)).toBeInTheDocument()
  })
})


