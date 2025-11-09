import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { StealthMeter } from '../StealthMeter'

describe('StealthMeter', () => {
  it('renders stealth status', () => {
    const status = {
      character_id: 'char_123',
      stealth_level: 'hidden' as const,
      visibility: 10,
      noise_level: 5,
      light_exposure: 15,
      enemies_aware: { total: 0, searching: 0 },
    }
    render(<StealthMeter status={status} />)
    expect(screen.getByText('Скрыт')).toBeInTheDocument()
    expect(screen.getByText('10%')).toBeInTheDocument()
  })
})

