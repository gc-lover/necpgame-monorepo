import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { AuctionSearchResultCard } from '../AuctionSearchResultCard';

describe('AuctionSearchResultCard', () => {
  it('renders lot info', () => {
    const lot = {
      lot_id: 'lot_001',
      item_name: 'Legendary Katana',
      rarity: 'legendary',
      current_price: 125000,
      buyout_price: 150000,
      time_remaining: 7200,
      seller_name: 'ArasakaTrader',
      category: 'weapon',
    };
    render(<AuctionSearchResultCard lot={lot} />);
    expect(screen.getByText('Legendary Katana')).toBeInTheDocument();
    expect(screen.getByText('weapon')).toBeInTheDocument();
  });
});


