import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { OrderBookDisplay } from '../OrderBookDisplay'

describe('OrderBookDisplay', () => {
  it('renders order book', () => {
    const orderBook = {
      item_id: 'item_001',
      buy_orders: [{ price: 1000, quantity: 10, total_orders: 2 }],
      sell_orders: [{ price: 1100, quantity: 5, total_orders: 1 }],
      spread: 100,
      last_trade_price: 1050,
    }
    render(<OrderBookDisplay orderBook={orderBook} />)
    expect(screen.getByText(/Order Book/)).toBeInTheDocument()
    expect(screen.getByText(/1050/)).toBeInTheDocument()
  })
})

