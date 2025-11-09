import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { DiceRollDisplay } from '../DiceRollDisplay'

describe('DiceRollDisplay', () => {
  it('renders roll result', () => {
    const result = {
      roll: 15,
      attribute_modifier: 3,
      skill_bonus: 2,
      situation_modifiers: -2,
      total: 18,
      dc: 15,
      success: true,
      critical: false,
      margin: 3,
    }
    render(<DiceRollDisplay result={result} />)
    expect(screen.getByText('15')).toBeInTheDocument()
    expect(screen.getByText(/Итого: 18/)).toBeInTheDocument()
  })
})

