/**
 * Тесты для компонента HumanityDisplay
 */
import { describe, it, expect } from 'vitest'
import { render, screen } from '@testing-library/react'
import { HumanityDisplay } from '../HumanityDisplay'
import type { HumanityInfo } from '@/api/generated/gameplay/cyberpsychosis/models'

describe('HumanityDisplay', () => {
  const mockHumanity: HumanityInfo = {
    current: 75,
    max: 100,
    loss_percentage: 25,
    stage: 'stable',
  }

  it('должен отображать человечность из OpenAPI', () => {
    render(<HumanityDisplay humanity={mockHumanity} />)

    expect(screen.getByText(/Человечность/i)).toBeInTheDocument()
    expect(screen.getByText(/75 \/ 100/i)).toBeInTheDocument()
  })

  it('должен отображать процент потери', () => {
    render(<HumanityDisplay humanity={mockHumanity} />)

    expect(screen.getByText(/25\.0%/)).toBeInTheDocument()
  })

  it('должен отображать стадию', () => {
    render(<HumanityDisplay humanity={mockHumanity} />)

    expect(screen.getByText(/Стабильно/i)).toBeInTheDocument()
  })

  it('должен показывать warning при высокой потере', () => {
    const dangerHumanity: HumanityInfo = {
      current: 20,
      max: 100,
      loss_percentage: 80,
      stage: 'cyberpsycho',
    }

    render(<HumanityDisplay humanity={dangerHumanity} />)

    expect(screen.getByText(/80\.0%/)).toBeInTheDocument()
    expect(screen.getByText(/Киберпсихоз!/i)).toBeInTheDocument()
  })
})

