import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { PlayerOrderCard } from '../PlayerOrderCard';

describe('PlayerOrderCard', () => {
  it('renders player order info', () => {
    render(
      <PlayerOrderCard
        order={{
          order_id: 'ord_001',
          item_name: 'Cyberdeck Mk.III',
          side: 'BUY',
          type: 'LIMIT',
          status: 'PENDING',
          price: 12000,
          quantity: 5,
          filled_quantity: 2,
        }}
      />,
    );
    expect(screen.getByText('Cyberdeck Mk.III')).toBeInTheDocument();
    expect(screen.getByText('BUY')).toBeInTheDocument();
    expect(screen.getByText(/Filled/)).toBeInTheDocument();
  });
});


