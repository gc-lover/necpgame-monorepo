import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { OrderHistoryCard } from '../OrderHistoryCard';

describe('OrderHistoryCard', () => {
  it('отображает информацию об истории ордера', () => {
    render(
      <OrderHistoryCard
        order={{
          orderId: 'PM-200',
          itemName: 'Nano Armor',
          side: 'sell',
          orderType: 'market',
          executedPrice: 8800,
          quantity: 3,
          filledAt: '2077-10-30 21:10',
          pnl: 350,
          fees: 120,
        }}
      />,
    );

    expect(screen.getByText('Nano Armor')).toBeInTheDocument();
    expect(screen.getByText('SELL')).toBeInTheDocument();
    expect(screen.getByText(/8800/)).toBeInTheDocument();
    expect(screen.getByText(/PnL/)).toBeInTheDocument();
  });
});


