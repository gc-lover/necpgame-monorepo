import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { BanOverviewCard } from '../BanOverviewCard'

describe('BanOverviewCard', () => {
  it('shows ban metrics', () => {
    render(
      <BanOverviewCard
        metrics={{
          active: 42,
          pendingAppeals: 5,
          autoBans: 28,
          manualBans: 14,
        }}
      />,
    )

    expect(screen.getByText(/Active bans/i)).toBeInTheDocument()
    expect(screen.getByText(/42/)).toBeInTheDocument()
  })
})


