import { describe, it, expect, vi, beforeEach } from 'vitest'
import { fireEvent, render, screen } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { CharacterRosterCard } from '../components/CharacterRosterCard'

const mockUseListCharacters = vi.fn()
const mockUseDeleteCharacter = vi.fn()

vi.mock('@/api/generated/auth/characters/characters', () => ({
  useListCharacters: (...args: unknown[]) => mockUseListCharacters(...args),
  useDeleteCharacter: (...args: unknown[]) => mockUseDeleteCharacter(...args),
  getListCharactersQueryKey: () => ['/characters'],
}))

describe('CharacterRosterCard', () => {
  let queryClient: QueryClient
  const deleteMutate = vi.fn()
  const refetch = vi.fn()

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: { retry: false },
      },
    })
    mockUseListCharacters.mockReturnValue({
      data: {
        characters: [
          {
            id: 'char-1',
            name: 'Vee',
            class: 'Solo',
            level: 3,
            city_name: 'Night City',
          },
        ],
      },
      isLoading: false,
      error: null,
      isFetching: false,
      refetch,
    })
    mockUseDeleteCharacter.mockReturnValue({ mutate: deleteMutate, isPending: false })
    deleteMutate.mockReset()
    refetch.mockReset()
  })

  const renderComponent = () =>
    render(
      <QueryClientProvider client={queryClient}>
        <CharacterRosterCard />
      </QueryClientProvider>
    )

  it('отображает список персонажей', () => {
    renderComponent()
    expect(screen.getByText('Vee')).toBeInTheDocument()
    expect(screen.getByText(/solo • уровень 3 • night city/i)).toBeInTheDocument()
  })

  it('вызывает удаление персонажа', () => {
    renderComponent()
    fireEvent.click(screen.getByLabelText(/delete character/i))
    expect(deleteMutate).toHaveBeenCalledWith(
      { characterId: 'char-1' },
      expect.any(Object)
    )
  })

  it('выполняет ручное обновление', () => {
    renderComponent()
    fireEvent.click(screen.getByRole('button', { name: /обновить/i }))
    expect(refetch).toHaveBeenCalled()
  })
})






