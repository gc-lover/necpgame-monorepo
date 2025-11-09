import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ResourceCard } from '../ResourceCard'

describe('ResourceCard', () => {
  it('renders resource info', () => {
    const resource = {
      resource_id: 'res_001',
      name: 'Steel Ingot',
      category: 'processed',
      tier: 2,
      rarity: 'uncommon',
      base_value: 150,
      stack_size: 100,
      weight: 5,
    }
    render(<ResourceCard resource={resource} />)
    expect(screen.getByText('Steel Ingot')).toBeInTheDocument()
    expect(screen.getByText('T2')).toBeInTheDocument()
    expect(screen.getByText('PROCESSED')).toBeInTheDocument()
  })
})

