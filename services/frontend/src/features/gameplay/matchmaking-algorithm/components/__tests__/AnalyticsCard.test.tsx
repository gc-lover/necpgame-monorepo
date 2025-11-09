import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AnalyticsCard } from '../../components/AnalyticsCard'

describe('AnalyticsCard', () => {
  it('renders analytics snapshot', () => {
    render(
      <AnalyticsCard
        snapshot={{
          matchesToday: 1000,
          averageWait: '00:50',
          cancellations: 20,
          dodges: 15,
        }}
      />,
    )

    expect(screen.getByText(/Matches/i)).toBeInTheDocument()
    expect(screen.getByText(/cancellations/i)).toBeInTheDocument()
  })
})


