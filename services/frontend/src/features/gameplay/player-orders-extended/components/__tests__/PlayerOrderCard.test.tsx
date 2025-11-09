import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { PlayerOrderCard } from '../PlayerOrderCard';

describe('PlayerOrderCard', () => {
  it('renders order info', () => {
    const order = {
      order_id: 'ord_001',
      title: 'Craft legendary weapon',
      description: 'Need a legendary katana',
      type: 'CRAFTING',
      difficulty: 'EXPERT',
      payment: 50000,
      currency: 'ЭД',
      status: 'ACTIVE',
    };
    render(<PlayerOrderCard order={order} />);
    expect(screen.getByText('Craft legendary weapon')).toBeInTheDocument();
    expect(screen.getByText('CRAFTING')).toBeInTheDocument();
  });
});

