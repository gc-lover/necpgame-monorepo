import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { AuctionLotCard } from '../AuctionLotCard';

describe('AuctionLotCard', () => {
  it('renders lot info', () => {
    const lot = {
      lot_id: 'lot_001',
      item_name: 'Legendary Cyberware',
      quantity: 1,
      current_bid: 10000,
      buyout_price: 15000,
      seller_name: 'V',
      time_left: '12h',
      rarity: 'LEGENDARY',
    };
    render(<AuctionLotCard lot={lot} />);
    expect(screen.getByText('Legendary Cyberware')).toBeInTheDocument();
    expect(screen.getByText('LEGENDARY')).toBeInTheDocument();
  });
});

