import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AttributeDefinitionCard } from '../AttributeDefinitionCard'

describe('AttributeDefinitionCard', () => {
  it('renders attribute info', () => {
    render(
      <AttributeDefinitionCard
        attribute={{
          code: 'STR',
          name: 'Strength',
          description: 'Physical power and melee damage.',
          growthType: 'LINEAR',
          softCap: 60,
          hardCap: 90,
          synergySkills: ['Melee Weapons', 'Heavy Armor'],
        }}
      />,
    )

    expect(screen.getByText(/Strength/i)).toBeInTheDocument()
    expect(screen.getByText(/Physical power/i)).toBeInTheDocument()
  })
})


