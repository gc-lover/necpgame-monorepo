import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { ItemPriceCard } from '../ItemPriceCard';

describe('ItemPriceCard', () => {
  it('renders item pricing info', () => {
    render(
      <ItemPriceCard
        item={{
          itemId: 'item-123',
          itemName: 'Neon Katana',
          basePrice: 3200,
          currentPrice: 4600,
          vendorSellPrice: 4800,
          vendorBuyPrice: 3000,
        }}
        multipliers={[
          { name: 'Quality', value: 1.2 },
          { name: 'Rarity', value: 1.5 },
        ]}
      />,
    );

    expect(screen.getByText(/Neon Katana/i)).toBeInTheDocument();
    expect(screen.getByText(/Base: 3200¥/i)).toBeInTheDocument();
    expect(screen.getByText(/Quality: 1.2/i)).toBeInTheDocument();
  });
});



