import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { TravelEventCard } from '../TravelEventCard';

describe('TravelEventCard', () => {
  it('renders travel event info', () => {
    const event = {
      eventId: 'evt_001',
      name: 'Corpo Checkpoint',
      description: 'Random corpo security check',
      period: '2077',
      locationTypes: ['CHECKPOINT'],
      triggerChance: 0.25,
      choices: [],
      outcomes: [],
    };
    render(<TravelEventCard event={event} />);
    expect(screen.getByText('Corpo Checkpoint')).toBeInTheDocument();
    expect(screen.getByText('2077')).toBeInTheDocument();
  });
});

