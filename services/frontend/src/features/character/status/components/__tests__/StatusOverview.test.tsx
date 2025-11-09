/**
 * Тесты для StatusOverview
 */
import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { StatusOverview } from '../StatusOverview'
import type { CharacterStatus } from '@/api/generated/character-status/models'

describe('StatusOverview', () => {
  const mockStatus: CharacterStatus = {
    characterId: 'char-001',
    health: 80,
    maxHealth: 100,
    energy: 60,
    maxEnergy: 100,
    humanity: 70,
    maxHumanity: 100,
    level: 5,
    experience: 500,
    nextLevelExperience: 1000,
  }

  it('должен отображать статус из OpenAPI', () => {
    render(<StatusOverview status={mockStatus} />)
    expect(screen.getByText('Статус персонажа')).toBeInTheDocument()
    expect(screen.getByText('80 / 100')).toBeInTheDocument()
    expect(screen.getByText('60 / 100')).toBeInTheDocument()
  })
})

