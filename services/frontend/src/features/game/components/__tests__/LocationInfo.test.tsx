/**
 * Тесты для компонента LocationInfo
 */
import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { LocationInfo } from '../LocationInfo'
import type { GameLocation } from '@/api/generated/game/models'

describe('LocationInfo', () => {
  const mockLocation: GameLocation = {
    id: 'loc-001',
    name: 'Downtown - Корпоративный центр',
    description: 'Вы находитесь в центре корпоративного района',
    dangerLevel: 'low',
    city: 'Night City',
    district: 'Downtown',
    type: 'corporate',
    minLevel: 1,
    connectedLocations: ['loc-002', 'loc-003'],
  }

  it('должен отображать информацию о локации', () => {
    render(<LocationInfo location={mockLocation} />)

    expect(screen.getByText('Downtown - Корпоративный центр')).toBeInTheDocument()
    expect(screen.getByText('Вы находитесь в центре корпоративного района')).toBeInTheDocument()
  })

  it('должен отображать теги локации', () => {
    render(<LocationInfo location={mockLocation} />)

    expect(screen.getByText('Night City')).toBeInTheDocument()
    expect(screen.getByText('Downtown')).toBeInTheDocument()
    expect(screen.getByText('corporate')).toBeInTheDocument()
    expect(screen.getByText('Низкая опасность')).toBeInTheDocument()
    expect(screen.getByText('Min. Ур. 1')).toBeInTheDocument()
  })

  it('должен отображать количество связанных локаций', () => {
    render(<LocationInfo location={mockLocation} />)

    expect(screen.getByText(/Доступные переходы: 2/i)).toBeInTheDocument()
  })

  it('должен корректно отображать уровень опасности medium', () => {
    const mediumLocation: GameLocation = { ...mockLocation, dangerLevel: 'medium' }
    render(<LocationInfo location={mediumLocation} />)

    expect(screen.getByText('Средняя опасность')).toBeInTheDocument()
  })

  it('должен корректно отображать уровень опасности high', () => {
    const highLocation: GameLocation = { ...mockLocation, dangerLevel: 'high' }
    render(<LocationInfo location={highLocation} />)

    expect(screen.getByText('Высокая опасность')).toBeInTheDocument()
  })
})

