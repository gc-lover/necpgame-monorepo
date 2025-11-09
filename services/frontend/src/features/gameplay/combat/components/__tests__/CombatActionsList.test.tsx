import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { CombatActionsList } from '../CombatActionsList'
import type { CombatAction } from '@/api/generated/combat-system/models'

const actions: CombatAction[] = [
  {
    id: 'attack',
    name: 'Атака',
    type: 'attack',
    description: 'Нанести базовый урон.',
    available: true,
    cost: 5,
    damage: 20,
  },
  {
    id: 'defend',
    name: 'Защита',
    type: 'defend',
    description: 'Уменьшить входящий урон.',
    available: false,
  },
]

describe('CombatActionsList', () => {
  it('отображает список доступных действий и позволяет выбрать действие', () => {
    const onSelect = vi.fn()
    render(<CombatActionsList actions={actions} selectedActionType="attack" onSelect={onSelect} />)

    expect(screen.getByText('Атака')).toBeInTheDocument()
    expect(screen.getByText('Защита')).toBeInTheDocument()

    fireEvent.click(screen.getByText('Атака'))
    expect(onSelect).toHaveBeenCalledWith('attack')
  })
})

