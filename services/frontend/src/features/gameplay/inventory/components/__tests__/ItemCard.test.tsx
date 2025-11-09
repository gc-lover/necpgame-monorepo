/**
 * Тесты для ItemCard
 */
import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { ItemCard } from '../ItemCard'
import type { InventoryItem } from '@/api/generated/inventory/models'

describe('ItemCard', () => {
  const mockItem: InventoryItem = {
    id: 'item-001',
    name: 'Тестовый предмет',
    description: 'Описание',
    category: 'weapon',
    weight: 2.5,
    quantity: 1,
    stackable: false,
    rarity: 'rare',
    value: 1000,
    equippable: true,
    usable: false,
  }

  it('должен отображать предмет из OpenAPI', () => {
    render(<ItemCard item={mockItem} />)
    expect(screen.getByText('Тестовый предмет')).toBeInTheDocument()
    expect(screen.getByText('weapon')).toBeInTheDocument()
  })

  it('должен вызывать onEquip', () => {
    const onEquip = vi.fn()
    render(<ItemCard item={mockItem} onEquip={onEquip} />)
    const equipBtn = screen.getByRole('button')
    fireEvent.click(equipBtn)
    expect(onEquip).toHaveBeenCalled()
  })
})

