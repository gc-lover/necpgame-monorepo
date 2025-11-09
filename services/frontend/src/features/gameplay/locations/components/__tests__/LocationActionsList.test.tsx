/**
 * Тесты для LocationActionsList
 */
import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { LocationActionsList } from '../LocationActionsList'
import type { LocationAction } from '@/api/generated/locations/models'

describe('LocationActionsList', () => {
  const actions: LocationAction[] = [
    {
      id: 'explore_market',
      label: 'Исследовать рынок',
      description: 'Посмотреть товары у местных торговцев.',
      enabled: true,
      actionType: 'exploration',
    },
    {
      id: 'fight_arena',
      label: 'Аренный бой',
      description: 'Принять участие в боёвке для зрителей.',
      enabled: false,
      actionType: 'combat',
      disabledReason: 'Недостаточный уровень',
      requirements: {
        minLevel: 10,
      },
    },
  ]

  it('отображает список доступных действий', () => {
    render(<LocationActionsList actions={actions} />)

    expect(screen.getByText('Исследовать рынок')).toBeInTheDocument()
    expect(screen.getByText('Аренный бой')).toBeInTheDocument()
    expect(screen.getByText(/Недостаточный уровень/)).toBeInTheDocument()
  })
})

