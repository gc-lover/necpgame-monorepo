import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { RecipeCard } from '../RecipeCard'

describe('RecipeCard', () => {
  it('renders', () => {
    render(<RecipeCard recipe={{ name: 'Test Recipe' }} />)
    expect(screen.getByText('Test Recipe')).toBeInTheDocument()
  })
})

