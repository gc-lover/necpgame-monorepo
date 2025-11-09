import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { MarketDataCard } from '../MarketDataCard';

describe('MarketDataCard', () => {
  it('renders market data summary', () => {
    render(
      <MarketDataCard
        data={{
          category: 'Weapons',
          region: 'NeoTokyo',
          timestamp: '2077-11-07 16:55',
          averagePrices: { 'smart_rifle': 5200 },
          trendingUp: ['smart_rifle'],
          trendingDown: ['laser_dagger'],
          highDemand: ['plasma_grenade'],
          lowSupply: ['nano_armor'],
        }}
      />,
    );

    expect(screen.getByText(/Market Data/i)).toBeInTheDocument();
    expect(screen.getByText(/smart_rifle/i)).toBeInTheDocument();
    expect(screen.getByText(/High demand/i)).toBeInTheDocument();
  });
});



