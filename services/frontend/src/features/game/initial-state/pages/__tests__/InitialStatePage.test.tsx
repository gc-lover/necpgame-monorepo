import { describe, it, expect, beforeEach, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { InitialStatePage } from '../InitialStatePage'

const navigateMock = vi.fn()
const useInitialStateMock = vi.fn()

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom')
  return {
    ...actual,
    useNavigate: () => navigateMock,
  }
})

vi.mock('../../hooks/useInitialState', () => ({
  useInitialState: () => useInitialStateMock(),
}))

vi.mock('../../hooks/useGameState', () => {
  const completeTutorial = vi.fn()
  const state = {
    completeTutorial,
  }

  const useGameState = (selector: (state: typeof state) => any) => selector(state)
  useGameState.getState = () => state

  return {
    useGameState,
  }
})

describe('InitialStatePage', () => {
  beforeEach(() => {
    useInitialStateMock.mockReset()
    navigateMock.mockReset()
  })

  it('возвращает null если персонаж не выбран', () => {
    useInitialStateMock.mockReturnValue({
      characterId: null,
      initialStateQuery: { isLoading: false, isError: false, data: null },
      tutorialQuery: { data: undefined },
    })

    const { container } = render(<InitialStatePage />)

    expect(container.innerHTML).toBe('')
    expect(navigateMock).toHaveBeenCalledWith('/characters', { replace: true })
  })

  it('отображает индикатор загрузки', () => {
    useInitialStateMock.mockReturnValue({
      characterId: 'char-1',
      initialStateQuery: { isLoading: true },
      tutorialQuery: { data: undefined },
    })

    render(<InitialStatePage />)

    expect(screen.getByRole('progressbar')).toBeInTheDocument()
  })

  it('показывает сообщение об ошибке', () => {
    useInitialStateMock.mockReturnValue({
      characterId: 'char-1',
      initialStateQuery: { isLoading: false, isError: true, data: null },
      tutorialQuery: { data: undefined },
    })

    render(<InitialStatePage />)

    expect(screen.getByText(/не удалось загрузить начальное состояние/i)).toBeInTheDocument()
  })

  it('отображает данные начального состояния', () => {
    useInitialStateMock.mockReturnValue({
      characterId: 'char-1',
      initialStateQuery: {
        isLoading: false,
        isError: false,
        data: {
          location: {
            id: 'loc-1',
            name: 'Downtown',
            description: 'Корпоративный центр',
            dangerLevel: 'low',
            city: 'Night City',
            district: 'Downtown',
            type: 'corporate',
            minLevel: 1,
            connectedLocations: ['loc-2'],
          },
          availableNPCs: [
            {
              id: 'npc-1',
              name: 'Сара Миллер',
              description: 'Офицер NCPD',
              type: 'quest_giver',
              greeting: 'Привет, чомбата.',
              faction: 'ncpd',
              availableQuests: [],
            },
          ],
          availableActions: [
            {
              id: 'look-around',
              label: 'Осмотреться',
              description: 'Изучить окружение',
              enabled: true,
            },
          ],
          firstQuest: {
            id: 'quest-1',
            name: 'Доставка груза',
            description: 'Нужно доставить посылку.',
            type: 'side',
            level: 1,
            giverNpcId: 'npc-1',
            rewards: {
              experience: 100,
              money: 200,
              items: [],
              reputation: null,
            },
          },
        },
      },
      tutorialQuery: {
        data: {
          steps: [
            {
              id: 'step-1',
              title: 'Добро пожаловать',
              description: 'Осмотритесь',
              hint: 'Используйте действия',
            },
          ],
          currentStep: 0,
          totalSteps: 1,
          canSkip: true,
        },
      },
    })

    render(<InitialStatePage />)

    expect(screen.getByText('Стартовая локация')).toBeInTheDocument()
    expect(screen.getByText('Downtown')).toBeInTheDocument()
    expect(screen.getByText('Сара Миллер')).toBeInTheDocument()
    expect(screen.getByText('Доставка груза')).toBeInTheDocument()
  })
})

