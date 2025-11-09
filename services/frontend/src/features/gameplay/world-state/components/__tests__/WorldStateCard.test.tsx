import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { WorldStateCard } from '../WorldStateCard'

describe('WorldStateCard', () => {
  it('renders', () => {
    render(<WorldStateCard state={{ category: 'Territory', level: 'Regional' }} />)
    expect(screen.getByText('Territory')).toBeInTheDocument()
  })
})

