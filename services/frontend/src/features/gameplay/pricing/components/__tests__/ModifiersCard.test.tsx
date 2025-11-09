import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import { ModifiersCard } from '../ModifiersCard';

describe('ModifiersCard', () => {
  it('renders active modifiers data', () => {
    render(
      <ModifiersCard
        region="Night City"
        regionalModifiers={{ Downtown: 1.1 }}
        factionModifiers={{ Arasaka: 0.9 }}
        eventModifiers={[{ name: 'Night Market', description: '+10% prices', value: 1.1 }]}
      />,
    );

    expect(screen.getByText(/Night City/i)).toBeInTheDocument();
    expect(screen.getByText(/Downtown/i)).toBeInTheDocument();
    expect(screen.getByText(/Night Market/i)).toBeInTheDocument();
  });
});



