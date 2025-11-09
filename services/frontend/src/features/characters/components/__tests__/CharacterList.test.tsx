import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { CharacterList } from '../CharacterList'
import * as charactersApi from '../../../api/generated/auth/characters/characters'

/**
 * Тесты для компонента CharacterList
 */
describe('CharacterList', () => {
  let queryClient: QueryClient
  
  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: {
        queries: {
          retry: false,
        },
      },
    })
    vi.clearAllMocks()
  })
  
  /**
   * Вспомогательная функция для рендеринга с провайдером
   */
  const renderWithProvider = (component: React.ReactElement) => {
    return render(
      <QueryClientProvider client={queryClient}>
        {component}
      </QueryClientProvider>
    )
  }
  
  it('должен отображать загрузку при загрузке данных', () => {
    // Mock useListCharacters для возврата состояния загрузки
    vi.spyOn(charactersApi, 'useListCharacters').mockReturnValue({
      data: undefined,
      isLoading: true,
      error: null,
      refetch: vi.fn(),
    } as any)
    
    renderWithProvider(<CharacterList />)
    
    expect(screen.getByText(/Загрузка персонажей/i)).toBeInTheDocument()
  })
  
  it('должен отображать ошибку при ошибке загрузки', () => {
    // Mock useListCharacters для возврата ошибки
    vi.spyOn(charactersApi, 'useListCharacters').mockReturnValue({
      data: undefined,
      isLoading: false,
      error: new Error('Network error'),
      refetch: vi.fn(),
    } as any)
    
    renderWithProvider(<CharacterList />)
    
    expect(screen.getByText(/Ошибка загрузки персонажей/i)).toBeInTheDocument()
    expect(screen.getByText(/Network error/i)).toBeInTheDocument()
  })
  
  it('должен отображать сообщение когда нет персонажей', () => {
    // Mock useListCharacters для возврата пустого списка
    vi.spyOn(charactersApi, 'useListCharacters').mockReturnValue({
      data: {
        characters: [],
        max_characters: 5,
        current_count: 0,
      },
      isLoading: false,
      error: null,
      refetch: vi.fn(),
    } as any)
    
    renderWithProvider(<CharacterList />)
    
    expect(screen.getByText(/У вас пока нет персонажей/i)).toBeInTheDocument()
  })
  
  it('должен отображать список персонажей', () => {
    // Mock useListCharacters для возврата списка персонажей
    vi.spyOn(charactersApi, 'useListCharacters').mockReturnValue({
      data: {
        characters: [
          {
            id: '1',
            name: 'John Doe',
            class: 'Solo',
            level: 5,
            city_name: 'Night City',
            faction_name: 'Arasaka',
            last_login: '2025-01-27T10:00:00Z',
          },
          {
            id: '2',
            name: 'Jane Smith',
            class: 'Netrunner',
            level: 3,
            city_name: 'Night City',
            faction_name: null,
            last_login: null,
          },
        ],
        max_characters: 5,
        current_count: 2,
      },
      isLoading: false,
      error: null,
      refetch: vi.fn(),
    } as any)
    
    // Mock useDeleteCharacter
    vi.spyOn(charactersApi, 'useDeleteCharacter').mockReturnValue({
      mutate: vi.fn(),
      isPending: false,
    } as any)
    
    renderWithProvider(<CharacterList />)
    
    // Проверяем отображение персонажей
    expect(screen.getByText('John Doe')).toBeInTheDocument()
    expect(screen.getByText('Jane Smith')).toBeInTheDocument()
    expect(screen.getByText(/Ур\. 5/)).toBeInTheDocument()
    expect(screen.getByText(/Ур\. 3/)).toBeInTheDocument()
    expect(screen.getByText('Solo')).toBeInTheDocument()
    expect(screen.getByText('Netrunner')).toBeInTheDocument()
  })
  
  it('должен отображать счетчик персонажей', () => {
    vi.spyOn(charactersApi, 'useListCharacters').mockReturnValue({
      data: {
        characters: [],
        max_characters: 5,
        current_count: 2,
      },
      isLoading: false,
      error: null,
      refetch: vi.fn(),
    } as any)
    
    renderWithProvider(<CharacterList />)
    
    expect(screen.getByText(/Персонажи \(2\/5\)/i)).toBeInTheDocument()
  })
  
  it('должен отображать предупреждение при достижении лимита', () => {
    vi.spyOn(charactersApi, 'useListCharacters').mockReturnValue({
      data: {
        characters: Array(5).fill({}).map((_, i) => ({
          id: `${i}`,
          name: `Character ${i}`,
          class: 'Solo',
          level: 1,
          city_name: 'Night City',
        })),
        max_characters: 5,
        current_count: 5,
      },
      isLoading: false,
      error: null,
      refetch: vi.fn(),
    } as any)
    
    vi.spyOn(charactersApi, 'useDeleteCharacter').mockReturnValue({
      mutate: vi.fn(),
      isPending: false,
    } as any)
    
    renderWithProvider(<CharacterList />)
    
    expect(screen.getByText(/Достигнут лимит персонажей/i)).toBeInTheDocument()
  })
})

