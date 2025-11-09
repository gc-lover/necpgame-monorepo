import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { WorldEventCard } from '../WorldEventCard';

describe('WorldEventCard', () => {
  it('renders event info', () => {
    const event = {
      event_id: 'evt_001',
      title: 'Fourth Corporate War',
      era: '2000-2020',
      event_type: 'GLOBAL',
      description: 'The devastating war that shaped the world',
      impact_level: 'CRITICAL',
      status: 'ACTIVE',
    };
    render(<WorldEventCard event={event} />);
    expect(screen.getByText('Fourth Corporate War')).toBeInTheDocument();
    expect(screen.getByText('GLOBAL')).toBeInTheDocument();
  });
});

