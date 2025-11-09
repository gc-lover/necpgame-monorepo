import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ContentOverviewCard } from '../ContentOverviewCard'

describe('ContentOverviewCard', () => {
  it('renders overview data', () => {
    render(
      <ContentOverviewCard
        overview={{
          period: '2020-2030',
          totalQuests: 45,
          questsByType: { main: 5, side: 30, faction: 10 },
          totalLocations: 12,
          totalNPCs: 80,
          keyEvents: ['Corporate takeover'],
          implementedPercentage: 72,
        }}
      />,
    )

    expect(screen.getByText(/2020-2030/i)).toBeInTheDocument()
    expect(screen.getByText(/Quests: 45/i)).toBeInTheDocument()
  })
})


