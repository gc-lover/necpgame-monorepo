import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CurrencyCard } from '../CurrencyCard'

describe('CurrencyCard', () => {
  it('renders', () => {
    render(<CurrencyCard currency={{ name: 'Eurodollar' }} />)
    expect(screen.getByText('Eurodollar')).toBeInTheDocument()
  })
})

