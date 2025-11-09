import { render, screen } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { describe, it, expect, vi } from 'vitest'
import { RandomEventCard } from '../RandomEventCard'
import { RandomEvent } from '@/api/generated/events/models'

describe('RandomEventCard', () => {
  const mockEvent: RandomEvent = {
    id: 'event-1',
    name: 'Тестовое событие',
    description: 'Описание тестового события',
    options: [
      {
        id: 'option-1',
        text: 'Вариант 1',
        available: true,
      },
      {
        id: 'option-2',
        text: 'Вариант 2',
        available: false,
      },
    ],
    dangerLevel: 'MEDIUM',
  }

  it('рендерит название события', () => {
    render(<RandomEventCard event={mockEvent} />)
    expect(screen.getByText('Тестовое событие')).toBeInTheDocument()
  })

  it('рендерит описание события', () => {
    render(<RandomEventCard event={mockEvent} />)
    expect(screen.getByText('Описание тестового события')).toBeInTheDocument()
  })

  it('отображает уровень опасности', () => {
    render(<RandomEventCard event={mockEvent} />)
    expect(screen.getByText('Средняя опасность')).toBeInTheDocument()
  })

  it('отображает количество вариантов действий', () => {
    render(<RandomEventCard event={mockEvent} />)
    expect(screen.getByText('Вариантов действий: 2')).toBeInTheDocument()
  })

  it('отображает ограничение по времени', () => {
    const eventWithTime: RandomEvent = {
      ...mockEvent,
      timeLimit: 60,
    }
    render(<RandomEventCard event={eventWithTime} />)
    expect(screen.getByText(/Ограничение времени: 60 сек/)).toBeInTheDocument()
  })

  it('вызывает onClick при клике', async () => {
    const user = userEvent.setup()
    const handleClick = vi.fn()
    render(<RandomEventCard event={mockEvent} onClick={handleClick} />)

    const card = screen.getByText('Тестовое событие').closest('.MuiCard-root')
    if (card) {
      await user.click(card)
      expect(handleClick).toHaveBeenCalledTimes(1)
    }
  })

  it('отображает низкую опасность', () => {
    const lowDangerEvent: RandomEvent = {
      ...mockEvent,
      dangerLevel: 'LOW',
    }
    render(<RandomEventCard event={lowDangerEvent} />)
    expect(screen.getByText('Низкая опасность')).toBeInTheDocument()
  })

  it('отображает высокую опасность', () => {
    const highDangerEvent: RandomEvent = {
      ...mockEvent,
      dangerLevel: 'HIGH',
    }
    render(<RandomEventCard event={highDangerEvent} />)
    expect(screen.getByText('Высокая опасность')).toBeInTheDocument()
  })

  it('работает без уровня опасности', () => {
    const { dangerLevel, ...eventWithoutDanger } = mockEvent
    render(<RandomEventCard event={eventWithoutDanger} />)
    expect(screen.getByText('Тестовое событие')).toBeInTheDocument()
    expect(screen.queryByText(/опасность/)).not.toBeInTheDocument()
  })

  it('работает без ограничения по времени', () => {
    render(<RandomEventCard event={mockEvent} />)
    expect(screen.queryByText(/Ограничение времени/)).not.toBeInTheDocument()
  })
})

