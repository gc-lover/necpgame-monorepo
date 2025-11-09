import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ArcStatusCard } from '../ArcStatusCard'

describe('ArcStatusCard', () => {
  it('renders arc status info', () => {
    render(
      <ArcStatusCard
        arc={{
          arcName: 'Corporate War 2.0',
          phase: 'PHASE_2',
          episodesReleased: 5,
          totalEpisodes: 9,
          branchingPoints: 3,
          coherenceDelta: -6,
        }}
      />,
    )

    expect(screen.getByText(/Corporate War 2.0/i)).toBeInTheDocument()
    expect(screen.getByText(/Branching points/i)).toBeInTheDocument()
  })
})


