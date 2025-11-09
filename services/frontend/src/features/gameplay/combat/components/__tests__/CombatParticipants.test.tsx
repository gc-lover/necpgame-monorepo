import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { CombatParticipants } from '../CombatParticipants'
import type { CombatParticipant } from '@/api/generated/combat-system/models'

const participants: CombatParticipant[] = [
  {
    id: 'player-1',
    name: 'Игрок',
    type: 'player',
    health: 80,
    maxHealth: 100,
    energy: 50,
    armor: 10,
    isAlive: true,
  },
  {
    id: 'enemy-1',
    name: 'Враг',
    type: 'enemy',
    health: 20,
    maxHealth: 120,
    isAlive: true,
  },
]

describe('CombatParticipants', () => {
  it('отображает участников боя', () => {
    render(<CombatParticipants participants={participants} currentTurnId="player-1" />)

    expect(screen.getByText('Игрок')).toBeInTheDocument()
    expect(screen.getByText('Враг')).toBeInTheDocument()
    expect(screen.getByText(/Энергия/)).toBeInTheDocument()
    expect(screen.getByText(/Броня/)).toBeInTheDocument()
  })
})

