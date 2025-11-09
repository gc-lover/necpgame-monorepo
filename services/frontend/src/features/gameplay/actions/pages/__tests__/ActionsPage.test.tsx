import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { ActionsPage } from '../ActionsPage'

const navigateMock = vi.fn()
const exploreMutate = vi.fn()
const restMutate = vi.fn()
const useObjectMutate = vi.fn()
const hackMutate = vi.fn()

let selectedCharacterIdMock: string | null = 'char-1'

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

vi.mock('@/api/generated/actions/gameplay/gameplay', () => ({
  useExploreLocation: () => ({
    mutate: exploreMutate,
    isPending: false,
  }),
  useRestAction: () => ({
    mutate: restMutate,
    isPending: false,
  }),
  useUseObject: () => ({
    mutate: useObjectMutate,
    isPending: false,
  }),
  useHackSystem: () => ({
    mutate: hackMutate,
    isPending: false,
  }),
}))

describe('ActionsPage', () => {
  beforeEach(() => {
    selectedCharacterIdMock = 'char-1'
    navigateMock.mockReset()
    exploreMutate.mockReset()
    restMutate.mockReset()
    useObjectMutate.mockReset()
    hackMutate.mockReset()
  })

  it('редиректит на страницу выбора персонажа при отсутствии выбранного героя', () => {
    selectedCharacterIdMock = null

    const { container } = render(<ActionsPage />)

    expect(container.innerHTML).toBe('')
    expect(navigateMock).toHaveBeenCalledWith('/characters')
  })

  it('отправляет запрос осмотра локации с выбранным персонажем', () => {
    render(<ActionsPage />)

    fireEvent.change(screen.getByLabelText('ID локации'), { target: { value: 'loc-downtown-001' } })
    fireEvent.click(screen.getByRole('button', { name: /осмотреть/i }))

    expect(exploreMutate).toHaveBeenCalledWith({
      data: {
        characterId: 'char-1',
        locationId: 'loc-downtown-001',
      },
    })
  })

  it('открывает диалог использования объекта', () => {
    render(<ActionsPage />)

    fireEvent.click(screen.getByRole('button', { name: /использовать объект/i }))

    expect(screen.getByText('Использовать объект')).toBeInTheDocument()
  })
})

