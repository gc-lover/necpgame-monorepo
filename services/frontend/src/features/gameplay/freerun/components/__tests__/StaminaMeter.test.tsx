import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { StaminaMeter } from '../StaminaMeter'

describe('StaminaMeter', () => {
  it('renders stamina info', () => {
    render(<StaminaMeter current={80} max={100} regenRate={5} />)
    expect(screen.getByText(/80/)).toBeInTheDocument()
    expect(screen.getByText(/100/)).toBeInTheDocument()
  })
})

