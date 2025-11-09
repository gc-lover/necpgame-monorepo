/**
 * Тесты для LocationCard
 */
import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { LocationCard } from '../LocationCard'
import type { GameLocation } from '@/api/generated/locations/models'

describe('LocationCard', () => {
  const mockLocation: GameLocation = {
    id: 'loc-001',
    name: 'Тестовая локация',
    description: 'Описание',
    city: 'Night City',
    district: 'Downtown',
    region: 'night_city',
    dangerLevel: 'low',
    minLevel: 1,
    type: 'corporate',
    accessible: true,
  }

  it('должен отображать локацию из OpenAPI', () => {
    render(<LocationCard location={mockLocation} onClick={() => {}} />)
    expect(screen.getByText('Тестовая локация')).toBeInTheDocument()
    expect(screen.getByText('Downtown, Night City')).toBeInTheDocument()
  })

  it('должен вызывать onClick для доступной локации', () => {
    const onClick = vi.fn()
    render(<LocationCard location={mockLocation} onClick={onClick} />)
    fireEvent.click(screen.getByText('Тестовая локация'))
    expect(onClick).toHaveBeenCalled()
  })

  it('не должен вызывать onClick для недоступной локации', () => {
    const onClick = vi.fn()
    const lockedLocation = { ...mockLocation, accessible: false }
    render(<LocationCard location={lockedLocation} onClick={onClick} />)
    fireEvent.click(screen.getByText('Тестовая локация'))
    expect(onClick).not.toHaveBeenCalled()
  })
})

