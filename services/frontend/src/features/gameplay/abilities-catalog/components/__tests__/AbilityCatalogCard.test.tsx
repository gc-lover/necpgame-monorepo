import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AbilityCatalogCard } from '../AbilityCatalogCard'

describe('AbilityCatalogCard', () => {
  const mockAbility = {
    id: '1',
    name: 'Test Ability',
    description: 'Test Description',
    category: 'combat',
  }

  it('renders ability name', () => {
    render(<AbilityCatalogCard ability={mockAbility} />)
    expect(screen.getByText('Test Ability')).toBeInTheDocument()
  })

  it('shows category', () => {
    render(<AbilityCatalogCard ability={mockAbility} />)
    expect(screen.getByText('combat')).toBeInTheDocument()
  })
})

