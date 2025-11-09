import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { CurrencyPairCard } from '../CurrencyPairCard'

describe('CurrencyPairCard', () => {
  it('renders currency pair info', () => {
    const pair = {
      pair_name: 'NCRD/EURO',
      base_currency: 'NCRD',
      quote_currency: 'EURO',
      rate: 0.8523,
      spread: 0.15,
      change_24h: 2.5,
      volume_24h: 1500000,
      pair_type: 'major',
    }
    render(<CurrencyPairCard pair={pair} />)
    expect(screen.getByText('NCRD/EURO')).toBeInTheDocument()
    expect(screen.getByText('major')).toBeInTheDocument()
    expect(screen.getByText('+2.50%')).toBeInTheDocument()
  })
})

