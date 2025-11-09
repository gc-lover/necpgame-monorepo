import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SanctionStatsCard } from '../SanctionStatsCard'

describe('SanctionStatsCard', () => {
  it('shows sanction metrics', () => {
    render(
      <SanctionStatsCard
        metrics={{ warnings: 32, temporaryBans: 14, permanentBans: 3, reinstated: 5 }}
      />,
    )

    expect(screen.getByText(/Warnings/i)).toBeInTheDocument()
    expect(screen.getByText(/32/)).toBeInTheDocument()
  })
})


