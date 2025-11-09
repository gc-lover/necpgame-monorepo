import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { NarrativeSummaryCard } from '../NarrativeSummaryCard'

describe('NarrativeSummaryCard', () => {
  it('renders summary metrics', () => {
    render(
      <NarrativeSummaryCard
        activeThreads={12}
        arcsTracked={5}
        unresolvedBeats={9}
        totalCoherence={86}
        lastSync="2077-11-07 23:45"
      />,
    )

    expect(screen.getByText(/Active threads/i)).toBeInTheDocument()
    expect(screen.getByText(/Global coherence/i)).toBeInTheDocument()
  })
})


