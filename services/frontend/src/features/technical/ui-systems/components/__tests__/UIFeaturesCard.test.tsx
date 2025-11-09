import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { UIFeaturesCard } from '../../components/UIFeaturesCard'

describe('UIFeaturesCard', () => {
  it('renders feature list', () => {
    render(
      <UIFeaturesCard
        features={[
          {
            id: 'feature-1',
            name: 'Raid Planner',
            description: 'Coordinate squads',
            unlocked: true,
            module: 'hud',
          },
        ]}
      />,
    )

    expect(screen.getByText(/Raid Planner/i)).toBeInTheDocument()
  })
})


