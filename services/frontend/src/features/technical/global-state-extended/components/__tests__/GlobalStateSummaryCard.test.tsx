import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { GlobalStateSummaryCard } from '../GlobalStateSummaryCard'

describe('GlobalStateSummaryCard', () => {
  it('renders summary metrics', () => {
    render(
      <GlobalStateSummaryCard
        worldVersion={42}
        factionVersion={18}
        economyVersion={27}
        activeSessions={3200}
        mutationQueue={14}
        globalCoherence={88}
      />,
    )

    expect(screen.getByText(/Global State Overview/i)).toBeInTheDocument()
    expect(screen.getByText(/World version/i)).toBeInTheDocument()
  })
})


