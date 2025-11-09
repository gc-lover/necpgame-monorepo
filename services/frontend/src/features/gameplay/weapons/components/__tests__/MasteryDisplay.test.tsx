import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { MasteryDisplay } from '../MasteryDisplay'
import { WeaponMasteryProgress } from '@/api/generated/weapons/models'

describe('MasteryDisplay', () => {
  const mockMastery: WeaponMasteryProgress = {
    character_id: 'char-1',
    weapon_class: 'pistol',
    rank: 'expert',
    total_kills: 750,
    kills_to_next_rank: 1250,
    bonuses: [
      {
        name: '+10% Урон',
        value: 10,
        description: 'Увеличение урона от пистолетов',
      },
    ],
  }

  it('рендерит ранг мастерства (компактный вид)', () => {
    render(<MasteryDisplay mastery={mockMastery} compact={true} />)
    expect(screen.getByText('Эксперт')).toBeInTheDocument()
  })

  it('отображает количество убийств (компактный вид)', () => {
    render(<MasteryDisplay mastery={mockMastery} compact={true} />)
    expect(screen.getByText('750 убийств')).toBeInTheDocument()
  })

  it('показывает прогресс до следующего ранга', () => {
    render(<MasteryDisplay mastery={mockMastery} compact={true} />)
    expect(screen.getByText('До следующего ранга: 1250')).toBeInTheDocument()
  })

  it('рендерит полный вид с бонусами', () => {
    render(<MasteryDisplay mastery={mockMastery} compact={false} />)
    expect(screen.getByText('Активные бонусы')).toBeInTheDocument()
    expect(screen.getByText('+10% Урон')).toBeInTheDocument()
  })

  it('работает с рангом Novice', () => {
    const noviceMastery: WeaponMasteryProgress = {
      ...mockMastery,
      rank: 'novice',
      total_kills: 50,
    }
    render(<MasteryDisplay mastery={noviceMastery} compact={true} />)
    expect(screen.getByText('Новичок')).toBeInTheDocument()
  })

  it('работает с рангом Legend', () => {
    const legendMastery: WeaponMasteryProgress = {
      ...mockMastery,
      rank: 'legend',
      total_kills: 10000,
      kills_to_next_rank: 0,
    }
    render(<MasteryDisplay mastery={legendMastery} compact={true} />)
    expect(screen.getByText('Легенда')).toBeInTheDocument()
  })
})

