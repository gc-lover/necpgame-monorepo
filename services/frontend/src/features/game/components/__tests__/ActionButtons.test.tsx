/**
 * Тесты для компонента ActionButtons
 */
import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { ActionButtons } from '../ActionButtons'
import type { GameAction } from '@/api/generated/game/models'

describe('ActionButtons', () => {
  const mockActions: GameAction[] = [
    { id: 'look-around', label: 'Осмотреть окрестности', enabled: true },
    { id: 'talk-to-npc', label: 'Поговорить с NPC', enabled: true },
    { id: 'move', label: 'Переместиться', enabled: true },
    { id: 'rest', label: 'Отдохнуть', enabled: false },
  ]

  it('должен отображать все доступные действия', () => {
    render(<ActionButtons actions={mockActions} />)

    expect(screen.getByText('Доступные действия')).toBeInTheDocument()
    expect(screen.getByText('Осмотреть окрестности')).toBeInTheDocument()
    expect(screen.getByText('Поговорить с NPC')).toBeInTheDocument()
    expect(screen.getByText('Переместиться')).toBeInTheDocument()
    expect(screen.getByText('Отдохнуть')).toBeInTheDocument()
  })

  it('должен вызывать onActionClick при клике на кнопку', () => {
    const handleActionClick = vi.fn()
    render(<ActionButtons actions={mockActions} onActionClick={handleActionClick} />)

    const button = screen.getByText('Осмотреть окрестности')
    fireEvent.click(button)

    expect(handleActionClick).toHaveBeenCalledWith(mockActions[0])
  })

  it('должен отключать кнопки с enabled: false', () => {
    render(<ActionButtons actions={mockActions} />)

    const restButton = screen.getByText('Отдохнуть')
    expect(restButton).toBeDisabled()
  })

  it('должен возвращать null если actions пустой', () => {
    const { container } = render(<ActionButtons actions={[]} />)

    expect(container.firstChild).toBeNull()
  })
})

