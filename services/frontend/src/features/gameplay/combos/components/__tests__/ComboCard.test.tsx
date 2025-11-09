import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ComboCard } from '../ComboCard'

describe('ComboCard', () => {
  it('renders', () => {
    render(<ComboCard combo={{ id: '1', name: 'Test Combo', type: 'solo' }} />)
    expect(screen.getByText('Test Combo')).toBeInTheDocument()
  })
})

