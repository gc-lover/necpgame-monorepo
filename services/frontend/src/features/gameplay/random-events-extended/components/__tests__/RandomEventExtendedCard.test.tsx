import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { RandomEventExtendedCard } from '../RandomEventExtendedCard';

describe('RandomEventExtendedCard', () => {
  it('renders event info', () => {
    const event = {
      event_id: 'evt_001',
      title: 'Corpo Raid',
      period: '2077-2090',
      category: 'COMBAT',
      location_type: 'CORPO_ZONE',
      description: 'A raid on a corpo facility',
      risk_level: 'HIGH',
    };
    render(<RandomEventExtendedCard event={event} />);
    expect(screen.getByText('Corpo Raid')).toBeInTheDocument();
    expect(screen.getByText('COMBAT')).toBeInTheDocument();
  });
});

