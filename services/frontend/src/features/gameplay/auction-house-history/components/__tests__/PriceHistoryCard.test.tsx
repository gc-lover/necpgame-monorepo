import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { PriceHistoryCard } from '../PriceHistoryCard';

describe('PriceHistoryCard', () => {
  it('renders history info', () => {
    render(
      <PriceHistoryCard
        itemName="Legendary Katana"
        period="30d"
        interval="1d"
        dataPoints={[
          { timestamp: '2077-01-01T00:00:00Z', average_price: 1000, min_price: 900, max_price: 1100 },
        ]}
      />,
    );
    expect(screen.getByText('Legendary Katana')).toBeInTheDocument();
    expect(screen.getByText(/Period: 30d/)).toBeInTheDocument();
  });
});


