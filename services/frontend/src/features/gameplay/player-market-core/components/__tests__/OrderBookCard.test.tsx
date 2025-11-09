import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { OrderBookCard } from '../OrderBookCard';

describe('OrderBookCard', () => {
  it('renders order book info', () => {
    render(
      <OrderBookCard
        itemName="Rare Weapon"
        spread={500}
        lastTradePrice={4800}
        buyOrders={[{ price: 4500, quantity: 10, total_orders: 3 }]}
        sellOrders={[{ price: 5000, quantity: 5, total_orders: 2 }]}
      />,
    );
    expect(screen.getByText('Rare Weapon')).toBeInTheDocument();
    expect(screen.getByText(/Spread: 500/)).toBeInTheDocument();
    expect(screen.getByText(/Last: 4800/)).toBeInTheDocument();
  });
});


