/**
 * Тесты для NPCCard
 */
import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { NPCCard } from '../NPCCard'
import type { Npc } from '@/api/generated/npcs/models'

describe('NPCCard', () => {
  const mockNPC: Npc = {
    id: 'npc-001',
    name: 'Тестовый NPC',
    type: 'trader',
    locationId: 'loc-001',
    level: 10,
    description: 'Торговец оружием',
    faction: 'ncpd',
    isHostile: false,
  }

  it('должен отображать NPC из OpenAPI', () => {
    render(<NPCCard npc={mockNPC} onClick={() => {}} />)
    expect(screen.getByText('Тестовый NPC')).toBeInTheDocument()
    expect(screen.getByText('Торговец оружием')).toBeInTheDocument()
  })

  it('должен вызывать onClick', () => {
    const onClick = vi.fn()
    render(<NPCCard npc={mockNPC} onClick={onClick} />)
    fireEvent.click(screen.getByText('Тестовый NPC'))
    expect(onClick).toHaveBeenCalled()
  })
})

