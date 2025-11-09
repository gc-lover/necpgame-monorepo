import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { TradingPage } from '../TradingPage'

const navigateMock = vi.fn()

const useGetVendorsMock = vi.fn()
const useGetVendorInventoryMock = vi.fn()
const useBuyItemMock = vi.fn()
const useSellItemMock = vi.fn()

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom')
  return {
    ...actual,
    useNavigate: () => navigateMock,
    useSearchParams: () => [new URLSearchParams(), vi.fn()],
  }
})

vi.mock('@/features/game/hooks/useGameState', () => ({
  useGameState: (selector: (state: any) => unknown) =>
    selector({
      selectedCharacterId: 'char-1',
      characterState: { money: 2500 },
    }),
}))

vi.mock('@/api/generated/trading/trading/trading', () => ({
  useGetVendors: (...args: unknown[]) => useGetVendorsMock(...args),
  useGetVendorInventory: (...args: unknown[]) => useGetVendorInventoryMock(...args),
  useBuyItem: () => ({ mutate: useBuyItemMock, isPending: false }),
  useSellItem: () => ({ mutate: useSellItemMock, isPending: false }),
  useGetItemPrice: () => ({ data: { itemId: 'item-1', buyPrice: 100, sellPrice: 60 }, isLoading: false }),
}))

vi.mock('@/api/generated/inventory/inventory/inventory', () => ({
  useGetInventory: () => ({
    data: { items: [] },
    isLoading: false,
    refetch: vi.fn(),
  }),
}))

describe('TradingPage', () => {
  beforeEach(() => {
    navigateMock.mockReset()
    useGetVendorsMock.mockReset()
    useGetVendorInventoryMock.mockReset()
    useBuyItemMock.mockReset()
    useSellItemMock.mockReset()

    useGetVendorsMock.mockReturnValue({
      data: {
        vendors: [
          {
            id: 'vendor-1',
            name: 'Мистер Фингерс',
            locationId: 'loc-1',
            specialization: 'tech',
          },
        ],
      },
      isLoading: false,
      error: undefined,
    })

    useGetVendorInventoryMock.mockReturnValue({
      data: {
        vendorId: 'vendor-1',
        items: [
          {
            itemId: 'item-1',
            name: 'Нейрошунт',
            buyPrice: 1200,
            sellPrice: 800,
            quantity: 3,
            category: 'cyberware',
          },
        ],
        nextRefresh: '2025-11-09T10:00:00Z',
      },
      isLoading: false,
      refetch: vi.fn(),
    })
  })

  it('отображает торговцев и ассортимент по данным Trading API', async () => {
    render(<TradingPage />)

    expect(await screen.findByText('Мистер Фингерс')).toBeInTheDocument()
    expect(await screen.findByText('Нейрошунт')).toBeInTheDocument()
    expect(screen.getByText(/Торговля/)).toBeInTheDocument()
  })
})

