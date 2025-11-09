import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { PriceBreakdownCard } from '../PriceBreakdownCard';

describe('PriceBreakdownCard', () => {
  it('renders breakdown entries and modifiers', () => {
    render(
      <PriceBreakdownCard
        total={5800}
        quantityTotal={11600}
        breakdown={[
          { label: 'Base', value: 4000, type: 'base' },
          { label: 'Quality Bonus', value: 800, type: 'bonus' },
          { label: 'Durability Penalty', value: -200, type: 'penalty' },
        ]}
        modifiersApplied={[{ name: 'Night Market Event', value: 1.1 }]}
      />,
    );

    expect(screen.getByText(/Price Breakdown/i)).toBeInTheDocument();
    expect(screen.getByText(/Base/i)).toBeInTheDocument();
    expect(screen.getByText(/Night Market Event/i)).toBeInTheDocument();
  });
});



