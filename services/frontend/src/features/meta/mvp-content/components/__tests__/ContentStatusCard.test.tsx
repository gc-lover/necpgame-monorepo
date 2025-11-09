import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ContentStatusCard } from '../ContentStatusCard'

describe('ContentStatusCard', () => {
  it('renders status summary', () => {
    render(
      <ContentStatusCard
        ready={false}
        totalQuests={30}
        totalLocations={10}
        totalNPCs={60}
        systemsReady={{ quest_engine: true, combat: false, progression: true, social: true, economy: false }}
      />,
    )

    expect(screen.getByText(/Quests: 30/i)).toBeInTheDocument()
    expect(screen.getByText(/combat: pending/i)).toBeInTheDocument()
  })
})


