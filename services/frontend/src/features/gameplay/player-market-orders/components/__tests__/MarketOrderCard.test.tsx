import { render, screen } from '@testing-library/react';
import { describe, it, expect, vi } from 'vitest';
import { MarketOrderCard } from '../MarketOrderCard';

describe('MarketOrderCard', () => {
  it('отображает данные ордера и кнопку отмены', () => {
    const handleCancel = vi.fn();

    render(
      <MarketOrderCard
        order={{
          orderId: 'PM-100',
          itemName: 'Cyber Katana',
          side: 'buy',
          orderType: 'limit',
          status: 'pending',
          limitPrice: 4200,
          quantity: 5,
          filledQuantity: 2,
          timeInForce: 'GTC',
        }}
        onCancel={handleCancel}
      />,
    );

    expect(screen.getByText('Cyber Katana')).toBeInTheDocument();
    expect(screen.getByText('BUY')).toBeInTheDocument();
    expect(screen.getByText(/4200/)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Отменить ордер/i })).toBeInTheDocument();
  });
});


