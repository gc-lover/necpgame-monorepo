import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { TradeDetailsCard } from '../TradeDetailsCard';

describe('TradeDetailsCard', () => {
  it('renders trade details info', () => {
    render(
      <TradeDetailsCard
        trade={{
          tradeId: 'TRADE-7',
          itemId: 'cyberdeck_mk3',
          price: 7200,
          quantity: 2,
          buyerId: 'Runner01',
          sellerId: 'Dealer77',
          buyOrderId: 'BUY-10',
          sellOrderId: 'SELL-33',
          executedAt: '2077-11-07 16:20',
        }}
      />,
    );

    expect(screen.getByText(/Trade #TRADE-7/i)).toBeInTheDocument();
    expect(screen.getByText(/cyberdeck_mk3/i)).toBeInTheDocument();
    expect(screen.getByText(/Price: 7200¥ • Quantity: 2/i)).toBeInTheDocument();
    expect(screen.getByText(/Runner01/i)).toBeInTheDocument();
  });
});



