/**
 * Тесты для QuestListItem
 */
import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { QuestListItem } from '../QuestListItem'
import type { Quest } from '@/api/generated/quests/models'

describe('QuestListItem', () => {
  const mockQuest: Quest = {
    id: 'quest-001',
    name: 'Тестовый квест',
    description: 'Описание',
    type: 'side',
    level: 5,
    rewards: { experience: 100, currency: 200 },
    objectives: [],
  }

  it('должен отображать квест из OpenAPI', () => {
    render(<QuestListItem quest={mockQuest} onClick={() => {}} />)
    expect(screen.getByText('Тестовый квест')).toBeInTheDocument()
  })

  it('должен вызывать onClick', () => {
    const onClick = vi.fn()
    render(<QuestListItem quest={mockQuest} onClick={onClick} />)
    fireEvent.click(screen.getByText('Тестовый квест'))
    expect(onClick).toHaveBeenCalled()
  })
})

