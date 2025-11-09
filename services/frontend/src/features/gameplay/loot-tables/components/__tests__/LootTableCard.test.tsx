import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { LootTableCard } from '../LootTableCard'

describe('LootTableCard', () => {
  it('renders loot table info', () => {
    const table = {
      table_id: 'table_001',
      name: 'Boss Loot',
      source_type: 'boss' as const,
      tier: 't5',
      description: 'High tier boss loot',
    }
    render(<LootTableCard table={table} />)
    expect(screen.getByText('Boss Loot')).toBeInTheDocument()
    expect(screen.getByText('t5')).toBeInTheDocument()
  })
})

