import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { StockCompanyCard } from '../StockCompanyCard'

describe('StockCompanyCard', () => {
  it('renders company info', () => {
    const company = {
      ticker: 'ARSK',
      name: 'Arasaka Corporation',
      sector: 'security_tech' as const,
      current_price: 1250.5,
      price_change_24h: -2.5,
      market_cap: 5000000000,
      dividend_yield: 3.2,
    }
    render(<StockCompanyCard company={company} />)
    expect(screen.getByText('ARSK')).toBeInTheDocument()
    expect(screen.getByText('Arasaka Corporation')).toBeInTheDocument()
  })
})

