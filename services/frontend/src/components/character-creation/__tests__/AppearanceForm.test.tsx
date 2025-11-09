import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { AppearanceForm } from '../AppearanceForm'
import type { GameCharacterAppearance } from '../../../api/generated/auth/models'

/**
 * Тесты для компонента AppearanceForm
 */
describe('AppearanceForm', () => {
  const mockAppearance: GameCharacterAppearance = {
    height: 180,
    body_type: 'normal',
    hair_color: 'black',
    eye_color: 'brown',
    skin_color: 'light',
    distinctive_features: null,
  }
  
  const mockOnAppearanceChange = vi.fn()
  
  it('должен отображать форму внешности', () => {
    render(
      <AppearanceForm
        appearance={mockAppearance}
        onAppearanceChange={mockOnAppearanceChange}
      />
    )
    
    expect(screen.getByText('Внешность персонажа')).toBeInTheDocument()
    expect(screen.getByText(/Рост: 180 см/)).toBeInTheDocument()
    expect(screen.getByText('Телосложение')).toBeInTheDocument()
    expect(screen.getByText('Цвет волос')).toBeInTheDocument()
    expect(screen.getByText('Цвет глаз')).toBeInTheDocument()
    expect(screen.getByText('Цвет кожи')).toBeInTheDocument()
  })
  
  it('должен обновлять рост при изменении слайдера', () => {
    render(
      <AppearanceForm
        appearance={mockAppearance}
        onAppearanceChange={mockOnAppearanceChange}
      />
    )
    
    const heightInput = screen.getByRole('slider')
    fireEvent.change(heightInput, { target: { value: '190' } })
    
    expect(mockOnAppearanceChange).toHaveBeenCalledWith({
      ...mockAppearance,
      height: 190,
    })
  })
  
  it('должен обновлять телосложение при клике', () => {
    render(
      <AppearanceForm
        appearance={mockAppearance}
        onAppearanceChange={mockOnAppearanceChange}
      />
    )
    
    const muscularBtn = screen.getByText('Мускулистое')
    fireEvent.click(muscularBtn)
    
    expect(mockOnAppearanceChange).toHaveBeenCalledWith({
      ...mockAppearance,
      body_type: 'muscular',
    })
  })
  
  it('должен отображать счетчик символов для особых примет', () => {
    const appearanceWithFeatures = {
      ...mockAppearance,
      distinctive_features: 'Scar on left cheek',
    }
    
    render(
      <AppearanceForm
        appearance={appearanceWithFeatures}
        onAppearanceChange={mockOnAppearanceChange}
      />
    )
    
    expect(screen.getByText('18 / 500')).toBeInTheDocument()
  })
  
  it('должен ограничивать особые приметы до 500 символов', () => {
    render(
      <AppearanceForm
        appearance={mockAppearance}
        onAppearanceChange={mockOnAppearanceChange}
      />
    )
    
    const textarea = screen.getByPlaceholderText(/Шрамы, татуировки/i)
    expect(textarea).toHaveAttribute('maxLength', '500')
  })
  
  it('должен отображать цвета как выбранные', () => {
    render(
      <AppearanceForm
        appearance={mockAppearance}
        onAppearanceChange={mockOnAppearanceChange}
      />
    )
    
    // Проверяем что кнопка с выбранным телосложением имеет класс selected
    const normalBtn = screen.getByText('Обычное')
    expect(normalBtn).toHaveClass('selected')
  })
})

