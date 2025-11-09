/**
 * Тесты для компонента EnergyPoolDisplay
 */
import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { EnergyPoolDisplay } from '../EnergyPoolDisplay'
import type { EnergyPoolInfo } from '@/api/generated/gameplay/combat/models'

describe('EnergyPoolDisplay', () => {
  const mockEnergy: EnergyPoolInfo = {
    total_pool: 1000,
    used: 400,
    available: 600,
    regen_rate: 10,
    current_level: 80,
    max_level: 100,
  }

  it('должен отображать энергетический пул из OpenAPI', () => {
    render(<EnergyPoolDisplay energy={mockEnergy} />)

    expect(screen.getByText(/Энергия/i)).toBeInTheDocument()
    expect(screen.getByText(/Уровень:/i)).toBeInTheDocument()
    expect(screen.getByText(/80 \/ 100/i)).toBeInTheDocument()
  })

  it('должен отображать пул энергии', () => {
    render(<EnergyPoolDisplay energy={mockEnergy} />)

    expect(screen.getByText(/Пул:/i)).toBeInTheDocument()
    expect(screen.getByText(/400 \/ 1000/i)).toBeInTheDocument()
  })

  it('должен отображать регенерацию', () => {
    render(<EnergyPoolDisplay energy={mockEnergy} />)

    expect(screen.getByText(/Регенерация: 10\/сек/i)).toBeInTheDocument()
  })

  it('должен работать без max_level', () => {
    const energyNoMax: EnergyPoolInfo = {
      total_pool: 1000,
      used: 400,
      available: 600,
      regen_rate: 10,
      current_level: 80,
    }

    render(<EnergyPoolDisplay energy={energyNoMax} />)

    expect(screen.getByText('80')).toBeInTheDocument()
  })
})

