import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { PriceTrendCard } from '../PriceTrendCard';

describe('PriceTrendCard', () => {
  it('renders trend info', () => {
    render(
      <PriceTrendCard
        itemName="Cyberdeck Mk.III"
        trend="increasing"
        priceChange7d={5.5}
        priceChange30d={12.3}
        volatility={8.2}
      />,
    );
    expect(screen.getByText('Cyberdeck Mk.III')).toBeInTheDocument();
    expect(screen.getByText(/Δ7d/)).toBeInTheDocument();
  });
});


