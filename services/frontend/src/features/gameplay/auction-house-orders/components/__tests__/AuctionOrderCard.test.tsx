import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { AuctionOrderCard } from '../AuctionOrderCard';

describe('AuctionOrderCard', () => {
  it('renders order info', () => {
    const order = {
      order_id: 'ord_001',
      item_name: 'Rare Weapon',
      quantity: 10,
      price: 5000,
      filled_quantity: 3,
      status: 'partially_filled',
      expires_at: '24h',
      order_type: 'BUY' as const,
    };
    render(<AuctionOrderCard order={order} />);
    expect(screen.getByText('Rare Weapon')).toBeInTheDocument();
    expect(screen.getByText('BUY')).toBeInTheDocument();
  });
});

