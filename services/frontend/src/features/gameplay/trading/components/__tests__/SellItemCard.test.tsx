import { describe, it, expect, beforeEach, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { SellItemCard } from '../SellItemCard'
import type { InventoryItem } from '@/api/generated/inventory/models'

const useGetItemPriceMock = vi.fn()

vi.mock('@/api/generated/trading/trading/trading', () => ({
  useGetItemPrice: (...args: unknown[]) => useGetItemPriceMock(...args),
}))

const baseItem: InventoryItem = {
  id: 'item-1',
  name: 'Тестовый предмет',
  description: 'Описание',
  category: 'weapons',
  weight: 1,
  quantity: 2,
  stackable: false,
}

describe('SellItemCard', () => {
  beforeEach(() => {
    useGetItemPriceMock.mockReset()
  })

  it('отображает цены из Trading API', () => {
    useGetItemPriceMock.mockReturnValue({
      data: { itemId: 'item-1', buyPrice: 150, sellPrice: 90 },
      isLoading: false,
    })

    render(
      <SellItemCard
        vendorId="vendor-1"
        characterId="char-1"
        item={baseItem}
        onSell={() => {}}
      />
    )

    expect(screen.getByText('Тестовый предмет')).toBeInTheDocument()
    expect(screen.getByText('90€$')).toBeInTheDocument()
  })

  it('блокирует продажу во время загрузки цены', () => {
    useGetItemPriceMock.mockReturnValue({
      data: undefined,
      isLoading: true,
    })

    render(
      <SellItemCard
        vendorId="vendor-1"
        characterId="char-1"
        item={baseItem}
        onSell={() => {}}
      />
    )

    expect(screen.getByRole('button', { name: /Продать/i })).toBeDisabled()
    expect(screen.getByText('...')).toBeInTheDocument()
  })
})

