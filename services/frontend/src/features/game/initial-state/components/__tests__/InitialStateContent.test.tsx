import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { InitialStateContent } from '../InitialStateContent'
import type { GameLocation, GameNPC } from '@/api/generated/game/models'

describe('InitialStateContent', () => {
  const location: GameLocation = {
    id: 'loc-001',
    name: 'Downtown',
    description: 'Корпоративный район Night City',
    dangerLevel: 'low',
    city: 'Night City',
    district: 'Downtown',
    type: 'corporate',
    minLevel: 1,
    connectedLocations: ['loc-002'],
  }

  const npcs: GameNPC[] = [
    {
      id: 'npc-1',
      name: 'Сара Миллер',
      description: 'Офицер NCPD',
      type: 'quest_giver',
      greeting: 'Привет, чомбата.',
      faction: 'ncpd',
      availableQuests: ['quest-1'],
    },
  ]

  it('показывает заглушку, если нет данных о локации', () => {
    render(<InitialStateContent npcs={npcs} />)

    expect(screen.getByText(/данные о локации пока недоступны/i)).toBeInTheDocument()
  })

  it('отображает локацию и список NPC', () => {
    render(<InitialStateContent location={location} npcs={npcs} />)

    expect(screen.getByText('Downtown')).toBeInTheDocument()
    expect(screen.getByText('Сара Миллер')).toBeInTheDocument()
  })
})

