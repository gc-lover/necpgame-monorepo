import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { DividendScheduleCard } from '../DividendScheduleCard'

describe('DividendScheduleCard', () => {
  it('shows dividend entries', () => {
    render(
      <DividendScheduleCard
        schedule={[
          {
            fundName: 'Night City Logistics Fund',
            payoutDate: '2077-11-15',
            expectedAmount: 7200,
            status: 'PLANNED',
          },
        ]}
      />,
    )

    expect(screen.getByText(/Night City Logistics Fund/i)).toBeInTheDocument()
    expect(screen.getByText(/7200¥/i)).toBeInTheDocument()
  })
})


