import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { HUDOverviewCard } from '../../components/HUDOverviewCard'

describe('HUDOverviewCard', () => {
  it('renders hud summary', () => {
    render(
      <HUDOverviewCard
        hud={{
          latencyMs: 32,
          widgetCount: 6,
          widgets: [
            { widget: 'Minimap', enabled: true, position: 'top-right', priority: 1 },
          ],
        }}
      />,
    )

    expect(screen.getByText(/HUD Overview/i)).toBeInTheDocument()
    expect(screen.getByText(/Minimap/i)).toBeInTheDocument()
  })
})


