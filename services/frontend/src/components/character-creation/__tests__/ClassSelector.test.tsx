import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { ClassSelector } from '../ClassSelector'
import * as charactersApi from '../../../api/generated/auth/characters/characters'

/**
 * Тесты для компонента ClassSelector
 */
describe('ClassSelector', () => {
  let queryClient: QueryClient
  const mockOnClassSelect = vi.fn()
  const mockOnSubclassSelect = vi.fn()
  
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
  
  const mockClasses = {
    classes: [
      {
        id: 'solo',
        name: 'Solo',
        description: 'Боевой класс',
        subclasses: [
          {
            id: 'solo_assassin',
            name: 'Assassin',
            description: 'Специализация на скрытности',
          },
        ],
      },
      {
        id: 'netrunner',
        name: 'Netrunner',
        description: 'Хакер',
        subclasses: [],
      },
    ],
  }
  
  it('должен отображать загрузку при загрузке данных', () => {
    vi.spyOn(charactersApi, 'useGetCharacterClasses').mockReturnValue({
      data: undefined,
      isLoading: true,
      error: null,
    } as any)
    
    renderWithProvider(
      <ClassSelector
        selectedClass={null}
        selectedSubclass={null}
        onClassSelect={mockOnClassSelect}
        onSubclassSelect={mockOnSubclassSelect}
      />
    )
    
    expect(screen.getByText(/Загрузка классов/i)).toBeInTheDocument()
  })
  
  it('должен отображать ошибку при ошибке загрузки', () => {
    vi.spyOn(charactersApi, 'useGetCharacterClasses').mockReturnValue({
      data: undefined,
      isLoading: false,
      error: new Error('Network error'),
    } as any)
    
    renderWithProvider(
      <ClassSelector
        selectedClass={null}
        selectedSubclass={null}
        onClassSelect={mockOnClassSelect}
        onSubclassSelect={mockOnSubclassSelect}
      />
    )
    
    expect(screen.getByText(/Ошибка загрузки классов/i)).toBeInTheDocument()
  })
  
  it('должен отображать список классов', () => {
    vi.spyOn(charactersApi, 'useGetCharacterClasses').mockReturnValue({
      data: mockClasses,
      isLoading: false,
      error: null,
    } as any)
    
    renderWithProvider(
      <ClassSelector
        selectedClass={null}
        selectedSubclass={null}
        onClassSelect={mockOnClassSelect}
        onSubclassSelect={mockOnSubclassSelect}
      />
    )
    
    expect(screen.getByText('Solo')).toBeInTheDocument()
    expect(screen.getByText('Netrunner')).toBeInTheDocument()
    expect(screen.getByText('Боевой класс')).toBeInTheDocument()
    expect(screen.getByText('Хакер')).toBeInTheDocument()
  })
  
  it('должен вызывать onClassSelect при клике на класс', () => {
    vi.spyOn(charactersApi, 'useGetCharacterClasses').mockReturnValue({
      data: mockClasses,
      isLoading: false,
      error: null,
    } as any)
    
    renderWithProvider(
      <ClassSelector
        selectedClass={null}
        selectedSubclass={null}
        onClassSelect={mockOnClassSelect}
        onSubclassSelect={mockOnSubclassSelect}
      />
    )
    
    const soloCard = screen.getByText('Solo').closest('.class-card')
    fireEvent.click(soloCard!)
    
    expect(mockOnClassSelect).toHaveBeenCalledWith('solo')
    expect(mockOnSubclassSelect).toHaveBeenCalledWith(null)
  })
  
  it('должен отображать подклассы для выбранного класса', () => {
    vi.spyOn(charactersApi, 'useGetCharacterClasses').mockReturnValue({
      data: mockClasses,
      isLoading: false,
      error: null,
    } as any)
    
    renderWithProvider(
      <ClassSelector
        selectedClass="solo"
        selectedSubclass={null}
        onClassSelect={mockOnClassSelect}
        onSubclassSelect={mockOnSubclassSelect}
      />
    )
    
    expect(screen.getByText('Выберите подкласс (опционально)')).toBeInTheDocument()
    expect(screen.getByText('Assassin')).toBeInTheDocument()
    expect(screen.getByText('Без подкласса')).toBeInTheDocument()
  })
  
  it('должен вызывать onSubclassSelect при клике на подкласс', () => {
    vi.spyOn(charactersApi, 'useGetCharacterClasses').mockReturnValue({
      data: mockClasses,
      isLoading: false,
      error: null,
    } as any)
    
    renderWithProvider(
      <ClassSelector
        selectedClass="solo"
        selectedSubclass={null}
        onClassSelect={mockOnClassSelect}
        onSubclassSelect={mockOnSubclassSelect}
      />
    )
    
    const assassinCard = screen.getByText('Assassin').closest('.subclass-card')
    fireEvent.click(assassinCard!)
    
    expect(mockOnSubclassSelect).toHaveBeenCalledWith('solo_assassin')
  })
  
  it('должен отображать выбранный класс как selected', () => {
    vi.spyOn(charactersApi, 'useGetCharacterClasses').mockReturnValue({
      data: mockClasses,
      isLoading: false,
      error: null,
    } as any)
    
    renderWithProvider(
      <ClassSelector
        selectedClass="solo"
        selectedSubclass={null}
        onClassSelect={mockOnClassSelect}
        onSubclassSelect={mockOnSubclassSelect}
      />
    )
    
    const classesGrid = screen.getByText('Выберите класс').nextElementSibling
    const soloCard = classesGrid?.querySelector('.class-card.selected')
    expect(soloCard).toBeInTheDocument()
    expect(soloCard).toHaveClass('selected')
  })
})

