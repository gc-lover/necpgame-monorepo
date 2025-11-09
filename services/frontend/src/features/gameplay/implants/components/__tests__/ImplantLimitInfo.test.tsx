/**
 * Тесты для компонента ImplantLimitInfo
 */
import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { ImplantLimitInfo } from '../ImplantLimitInfo'
import type { ImplantLimits } from '@/api/generated/gameplay/combat/models'

describe('ImplantLimitInfo', () => {
  const mockLimits: ImplantLimits = {
    base_limit: 10,
    bonus_from_class: 2,
    bonus_from_progression: 1,
    humanity_penalty: -1,
    current_limit: 12,
    used_slots: 5,
    available_slots: 7,
  }

  it('должен отображать лимиты имплантов из OpenAPI', () => {
    render(<ImplantLimitInfo limits={mockLimits} />)

    expect(screen.getByText(/Лимит имплантов/i)).toBeInTheDocument()
    expect(screen.getByText(/Использовано: 5 \/ 12/i)).toBeInTheDocument()
    expect(screen.getByText(/Свободно: 7/i)).toBeInTheDocument()
  })

  it('должен отображать базовый лимит', () => {
    render(<ImplantLimitInfo limits={mockLimits} />)

    expect(screen.getByText(/Базовый:/i)).toBeInTheDocument()
    expect(screen.getByText('10')).toBeInTheDocument()
  })

  it('должен отображать бонусы', () => {
    render(<ImplantLimitInfo limits={mockLimits} />)

    expect(screen.getByText(/Бонус \(класс\):/i)).toBeInTheDocument()
    expect(screen.getByText(/\+2/)).toBeInTheDocument()
    expect(screen.getByText(/Бонус \(прокачка\):/i)).toBeInTheDocument()
    expect(screen.getByText(/\+1/)).toBeInTheDocument()
  })

  it('должен отображать штрафы', () => {
    render(<ImplantLimitInfo limits={mockLimits} />)

    expect(screen.getByText(/Штраф \(человечность\):/i)).toBeInTheDocument()
    expect(screen.getByText('-1')).toBeInTheDocument()
  })

  it('должен работать без бонусов/штрафов', () => {
    const minLimits: ImplantLimits = {
      base_limit: 10,
      current_limit: 10,
      used_slots: 0,
      available_slots: 10,
    }

    render(<ImplantLimitInfo limits={minLimits} />)

    expect(screen.getByText('10')).toBeInTheDocument()
    expect(screen.queryByText(/Бонус/)).not.toBeInTheDocument()
    expect(screen.queryByText(/Штраф/)).not.toBeInTheDocument()
  })
})

