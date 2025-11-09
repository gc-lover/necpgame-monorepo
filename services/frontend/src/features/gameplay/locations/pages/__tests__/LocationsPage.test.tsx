import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { LocationsPage } from '../LocationsPage'

const navigateMock = vi.fn()

let selectedCharacterIdMock: string | null = 'char-1'

const defaultLocation = {
  id: 'loc-1',
  name: 'Downtown',
  description: 'Городской центр',
  city: 'Night City',
  district: 'Downtown',
  region: 'night_city' as const,
  dangerLevel: 'low' as const,
  minLevel: 1,
  type: 'corporate' as const,
  accessible: true,
  atmosphere: 'Неон и небоскрёбы.',
  availableActions: [],
} as const

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom')
  return {
    ...actual,
    useNavigate: () => navigateMock,
  }
})

vi.mock('@/features/game/hooks/useGameState', () => ({
  useGameState: (selector: (state: { selectedCharacterId: string | null }) => unknown) =>
    selector({ selectedCharacterId: selectedCharacterIdMock }),
}))

const useGetLocationsMock = vi.fn()
const useGetCurrentLocationMock = vi.fn()
const useGetLocationDetailsMock = vi.fn()
const useGetConnectedLocationsMock = vi.fn()
const travelMutateMock = vi.fn()

vi.mock('@/api/generated/locations/locations/locations', () => ({
  useGetLocations: (...args: unknown[]) => useGetLocationsMock(...args),
  useGetCurrentLocation: (...args: unknown[]) => useGetCurrentLocationMock(...args),
  useGetLocationDetails: (...args: unknown[]) => useGetLocationDetailsMock(...args),
  useGetConnectedLocations: (...args: unknown[]) => useGetConnectedLocationsMock(...args),
  useTravelToLocation: () => ({
    mutate: travelMutateMock,
    isPending: false,
  }),
}))

describe('LocationsPage', () => {
  beforeEach(() => {
    navigateMock.mockReset()
    travelMutateMock.mockReset()
    selectedCharacterIdMock = 'char-1'

    useGetLocationsMock.mockReturnValue({
      data: {
        locations: [defaultLocation],
        total: 1,
      },
      isLoading: false,
      error: undefined,
    })

    useGetCurrentLocationMock.mockReturnValue({
      data: {
        ...defaultLocation,
      },
      isLoading: false,
    })

    useGetLocationDetailsMock.mockReturnValue({
      data: {
        ...defaultLocation,
      },
      isLoading: false,
    })

    useGetConnectedLocationsMock.mockReturnValue({
      data: {
        connectedLocations: [],
      },
      isLoading: false,
    })
  })

  it('редиректит на выбор персонажа, если персонаж не выбран', () => {
    selectedCharacterIdMock = null

    const { container } = render(<LocationsPage />)

    expect(container.innerHTML).toBe('')
    expect(navigateMock).toHaveBeenCalledWith('/characters')
  })

  it('отображает список локаций из OpenAPI', () => {
    render(<LocationsPage />)

    expect(screen.getByText('Downtown')).toBeInTheDocument()
    expect(screen.getByText(/Карта локаций/)).toBeInTheDocument()
  })
})

