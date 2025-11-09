import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AbilityCard } from '../AbilityCard'
import { Ability } from '@/api/generated/abilities/models'

describe('AbilityCard', () => {
  const mockAbility: Ability = {
    id: 'ability-1',
    name: 'Тестовая способность',
    description: 'Описание',
    type: 'offensive',
    source: { type: 'implants', item_id: 'implant-1' },
    cooldown: { base: 10 },
    cost: { resource: 'energy', amount: 50 },
  }

  it('рендерит название способности', () => {
    render(<AbilityCard ability={mockAbility} />)
    expect(screen.getByText('Тестовая способность')).toBeInTheDocument()
  })

  it('отображает тип', () => {
    render(<AbilityCard ability={mockAbility} />)
    expect(screen.getByText('offensive')).toBeInTheDocument()
  })

  it('показывает кулдаун', () => {
    render(<AbilityCard ability={mockAbility} />)
    expect(screen.getByText(/10с/)).toBeInTheDocument()
  })
})

