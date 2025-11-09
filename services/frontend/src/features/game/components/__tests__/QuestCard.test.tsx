/**
 * Тесты для компонента QuestCard
 */
import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { QuestCard } from '../QuestCard'
import type { GameQuest } from '@/api/generated/game/models'

describe('QuestCard', () => {
  const mockQuest: GameQuest = {
    id: 'quest-001',
    name: 'Тестовый квест',
    description: 'Описание тестового квеста',
    type: 'side',
    level: 5,
    giverNpcId: 'npc-001',
    rewards: {
      experience: 100,
      money: 200,
      reputation: {
        faction: 'NCPD',
        amount: 10,
      },
    },
  }

  it('должен отображать информацию о квесте', () => {
    render(<QuestCard quest={mockQuest} />)

    expect(screen.getByText('Тестовый квест')).toBeInTheDocument()
    expect(screen.getByText('Описание тестового квеста')).toBeInTheDocument()
    expect(screen.getByText('Побочный')).toBeInTheDocument()
    expect(screen.getByText('Ур. 5')).toBeInTheDocument()
  })

  it('должен отображать награды', () => {
    render(<QuestCard quest={mockQuest} />)

    expect(screen.getByText(/\+100 опыта/i)).toBeInTheDocument()
    expect(screen.getByText(/\+200 eddies/i)).toBeInTheDocument()
    expect(screen.getByText(/Репутация: NCPD \(\+10\)/i)).toBeInTheDocument()
  })

  it('должен вызывать onSelect при клике', () => {
    const handleSelect = vi.fn()
    render(<QuestCard quest={mockQuest} onSelect={handleSelect} />)

    const card = screen.getByText('Тестовый квест').closest('.MuiCard-root')
    expect(card).toBeInTheDocument()
    
    if (card) {
      fireEvent.click(card)
      expect(handleSelect).toHaveBeenCalledWith(mockQuest)
    }
  })

  it('должен отображать основной квест с правильным цветом', () => {
    const mainQuest: GameQuest = { ...mockQuest, type: 'main' }
    render(<QuestCard quest={mainQuest} />)

    expect(screen.getByText('Основной')).toBeInTheDocument()
  })

  it('должен отображать контракт с правильным цветом', () => {
    const contractQuest: GameQuest = { ...mockQuest, type: 'contract' }
    render(<QuestCard quest={contractQuest} />)

    expect(screen.getByText('Контракт')).toBeInTheDocument()
  })
})

