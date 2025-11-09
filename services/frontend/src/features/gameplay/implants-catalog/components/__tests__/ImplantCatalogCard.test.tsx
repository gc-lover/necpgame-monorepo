import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ImplantCatalogCard } from '../ImplantCatalogCard'

describe('ImplantCatalogCard', () => {
  it('renders', () => {
    render(<ImplantCatalogCard implant={{ id: '1', name: 'Sandevistan', type: 'combat', rarity: 'legendary' }} />)
    expect(screen.getByText('Sandevistan')).toBeInTheDocument()
  })
})

