import { describe, it, expect, vi, beforeEach } from 'vitest'
import { fireEvent, render, screen } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { CharacterCreationCard } from '../components/CharacterCreationCard'

const mockUseGetCharacterClasses = vi.fn()
const mockUseGetCharacterOrigins = vi.fn()
const mockUseCreateCharacter = vi.fn()
const mockUseGetFactions = vi.fn()
const mockUseGetCities = vi.fn()

vi.mock('@/api/generated/auth/characters/characters', () => ({
  useGetCharacterClasses: (...args: unknown[]) => mockUseGetCharacterClasses(...args),
  useGetCharacterOrigins: (...args: unknown[]) => mockUseGetCharacterOrigins(...args),
  useCreateCharacter: (...args: unknown[]) => mockUseCreateCharacter(...args),
  getListCharactersQueryKey: () => ['/characters'],
}))

vi.mock('@/api/generated/auth/reference-data/reference-data', () => ({
  useGetFactions: (...args: unknown[]) => mockUseGetFactions(...args),
  useGetCities: (...args: unknown[]) => mockUseGetCities(...args),
}))

describe('CharacterCreationCard', () => {
  let queryClient: QueryClient
  const mutate = vi.fn()

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: { retry: false },
      },
    })

    mockUseGetCharacterClasses.mockReturnValue({
      data: {
        classes: [
          {
            id: 'solo',
            name: 'Solo',
            description: 'Воин',
            subclasses: [],
          },
        ],
      },
      isLoading: false,
    })
    mockUseGetCharacterOrigins.mockReturnValue({
      data: {
        origins: [
          {
            id: 'street_kid',
            name: 'Уличный',
            description: '',
            starting_skills: [],
            available_factions: [],
            starting_resources: { currency: 0, items: [] },
          },
        ],
      },
      isLoading: false,
    })
    mockUseGetFactions.mockReturnValue({
      data: { factions: [] },
      isLoading: false,
    })
    mockUseGetCities.mockReturnValue({
      data: {
        cities: [
          {
            id: 'city-1',
            name: 'Night City',
            region: 'US',
            description: '',
            available_for_factions: [],
          },
        ],
      },
      isLoading: false,
    })
    mockUseCreateCharacter.mockReturnValue({ mutate, isPending: false })
    mutate.mockReset()
  })

  const renderComponent = () =>
    render(
      <QueryClientProvider client={queryClient}>
        <CharacterCreationCard />
      </QueryClientProvider>
    )

  it('отправляет запрос создания персонажа', () => {
    mutate.mockImplementation((_payload, options) =>
      options?.onSuccess?.({ character: { id: 'char-1', name: 'Vee' } })
    )
    renderComponent()

    fireEvent.change(screen.getByLabelText(/имя персонажа/i), { target: { value: 'Vee' } })
    fireEvent.click(screen.getByRole('button', { name: /создать персонажа/i }))

    expect(mutate).toHaveBeenCalled()
    expect(screen.getByText(/персонаж vee создан/i)).toBeInTheDocument()
  })

  it('не отправляет форму без имени', () => {
    renderComponent()
    fireEvent.click(screen.getByRole('button', { name: /создать персонажа/i }))
    expect(mutate).not.toHaveBeenCalled()
    expect(screen.getByText(/заполните обязательные поля/i)).toBeInTheDocument()
  })
})






