import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { CombatSummary } from '../CombatSummary'
import type { CombatState, CombatResult } from '@/api/generated/combat-system/models'

const combatState: CombatState = {
  id: 'combat-1',
  status: 'ended',
  participants: [],
  currentTurn: 'player-1',
  round: 3,
  log: ['Бой завершён.'],
}

const combatResult: CombatResult = {
  victory: true,
  rewards: {
    experience: 120,
    currency: 300,
    items: ['item-1'],
  },
  penalties: null,
}

describe('CombatSummary', () => {
  it('отображает сводку по бою и результаты', () => {
    render(<CombatSummary combatState={combatState} combatResult={combatResult} />)

    expect(screen.getByText(/Состояние боя/)).toBeInTheDocument()
    expect(screen.getByText(/Раунд: 3/)).toBeInTheDocument()
    expect(screen.getByText(/Победа/)).toBeInTheDocument()
    expect(screen.getByText(/Опыт: 120/)).toBeInTheDocument()
  })
})

