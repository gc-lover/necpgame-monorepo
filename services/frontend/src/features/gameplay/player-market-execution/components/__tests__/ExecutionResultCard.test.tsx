import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { ExecutionResultCard } from '../ExecutionResultCard';

describe('ExecutionResultCard', () => {
  it('renders execution summary with trades', () => {
    render(
      <ExecutionResultCard
        result={{
          orderId: 'ORD-42',
          status: 'filled',
          filledQuantity: 8,
          remainingQuantity: 0,
          averagePrice: 5100,
          totalCost: 40800,
          commission: 204,
          trades: [
            { tradeId: 'T-1', price: 5000, quantity: 3 },
            { tradeId: 'T-2', price: 5200, quantity: 5 },
          ],
        }}
      />,
    );

    expect(screen.getByText(/Execution #ORD-42/i)).toBeInTheDocument();
    expect(screen.getByText(/Filled: 8/i)).toBeInTheDocument();
    expect(screen.getByText(/T-1/i)).toBeInTheDocument();
    expect(screen.getByText(/3 @ 5000¥/i)).toBeInTheDocument();
  });
});



