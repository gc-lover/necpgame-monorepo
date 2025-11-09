/**
 * Тесты для компонента CharacterState
 */
import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { CharacterState } from '../CharacterState'
import type { GameCharacterState } from '@/api/generated/game/models'

describe('CharacterState', () => {
  const mockState: GameCharacterState = {
    health: 80,
    energy: 60,
    humanity: 90,
    money: 500,
    level: 1,
    experience: 50,
  }

  it('должен отображать состояние персонажа', () => {
    render(<CharacterState state={mockState} />)

    expect(screen.getByText(/Статус персонажа/i)).toBeInTheDocument()
    expect(screen.getByText(/Здоровье: 80\/100/i)).toBeInTheDocument()
    expect(screen.getByText(/Энергия: 60\/100/i)).toBeInTheDocument()
    expect(screen.getByText(/Человечность: 90\/100/i)).toBeInTheDocument()
    expect(screen.getByText(/500 eddies/i)).toBeInTheDocument()
    expect(screen.getByText(/Уровень:/i)).toBeInTheDocument()
    expect(screen.getByText(/1/)).toBeInTheDocument()
  })

  it('должен отображать опыт если он есть', () => {
    render(<CharacterState state={mockState} />)

    expect(screen.getByText(/Опыт: 50/i)).toBeInTheDocument()
  })

  it('должен работать с минимальными значениями', () => {
    const minState: GameCharacterState = {
      health: 0,
      energy: 0,
      humanity: 0,
      money: 0,
      level: 1,
    }

    render(<CharacterState state={minState} />)

    expect(screen.getByText(/Здоровье: 0\/100/i)).toBeInTheDocument()
    expect(screen.getByText(/0 eddies/i)).toBeInTheDocument()
  })

  it('должен работать с максимальными значениями', () => {
    const maxState: GameCharacterState = {
      health: 100,
      energy: 100,
      humanity: 100,
      money: 999999,
      level: 50,
    }

    render(<CharacterState state={maxState} />)

    expect(screen.getByText(/Здоровье: 100\/100/i)).toBeInTheDocument()
    expect(screen.getByText(/999999 eddies/i)).toBeInTheDocument()
    expect(screen.getByText(/50/)).toBeInTheDocument()
  })
})

