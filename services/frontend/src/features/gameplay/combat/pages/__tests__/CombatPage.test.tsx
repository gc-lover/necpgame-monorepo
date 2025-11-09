import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { CombatPage } from '../CombatPage'

const navigateMock = vi.fn()

const combatStateMock = {
  id: 'combat-1',
  status: 'active',
  participants: [
    { id: 'player-1', name: 'Игрок', type: 'player', health: 100, maxHealth: 120, isAlive: true },
  ],
  currentTurn: 'player-1',
  round: 2,
  log: ['Раунд 2 начался.'],
}

const availableActionsMock = {
  actions: [
    {
      id: 'attack',
      name: 'Атака',
      type: 'attack',
      available: true,
    },
  ],
}

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual<typeof import('react-router-dom')>('react-router-dom')
  return {
    ...actual,
    useNavigate: () => navigateMock,
  }
})

vi.mock('@/features/game/hooks/useGameState', () => ({
  useGameState: (selector: (state: { selectedCharacterId: string; characterState: { money: number } }) => unknown) =>
    selector({ selectedCharacterId: 'char-1', characterState: { money: 1000 } }),
}))

const initiateMock = vi.fn()
const performMock = vi.fn()
const fleeMock = vi.fn()

vi.mock('@/api/generated/combat-system/combat/combat', () => ({
  useInitiateCombat: () => ({ mutate: initiateMock, isPending: false }),
  useGetCombatState: () => ({
    data: combatStateMock,
    isFetching: false,
    refetch: vi.fn(),
  }),
  useGetAvailableActions: () => ({
    data: availableActionsMock,
    isFetching: false,
    refetch: vi.fn(),
  }),
  usePerformCombatAction: () => ({ mutate: performMock, isPending: false }),
  useFleeCombat: () => ({ mutate: fleeMock, isPending: false }),
  useGetCombatResult: () => ({
    data: undefined,
    refetch: vi.fn(),
  }),
}))

describe('CombatPage', () => {
  beforeEach(() => {
    navigateMock.mockReset()
    initiateMock.mockReset()
    performMock.mockReset()
    fleeMock.mockReset()
  })

  it('отображает данные боя и доступные действия', () => {
    render(<CombatPage />)

    expect(screen.getByText(/Боевая система/)).toBeInTheDocument()
    expect(screen.getByText('Игрок')).toBeInTheDocument()
    expect(screen.getByText('Атака')).toBeInTheDocument()
  })
})

