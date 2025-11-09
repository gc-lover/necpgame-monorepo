import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { HiringSummaryCard } from '../HiringSummaryCard'

describe('HiringSummaryCard', () => {
  it('renders summary metrics', () => {
    render(
      <HiringSummaryCard
        activeHires={5}
        pendingContracts={2}
        weeklyUpkeep={19800}
        squadStrength={86}
        missionCoverage={72}
      />,
    )

    expect(screen.getByText(/Active hires/i)).toBeInTheDocument()
    expect(screen.getByText(/week/i)).not.toBeInTheDocument()
    expect(screen.getByText(/Weekly upkeep/i)).toBeInTheDocument()
  })
})


