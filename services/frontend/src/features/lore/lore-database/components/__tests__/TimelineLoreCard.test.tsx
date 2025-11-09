import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TimelineLoreCard } from '../../components/TimelineLoreCard'

describe('TimelineLoreCard', () => {
  it('shows timeline info', () => {
    render(
      <TimelineLoreCard
        timeline={{
          arc: 'Corporate War',
          eraRange: '2069-2077',
          highlightEvents: [{ year: 2077, title: 'Tower Detonation', impact: 'City reset' }],
        }}
      />,
    )

    expect(screen.getByText(/Corporate War/i)).toBeInTheDocument()
    expect(screen.getByText(/2077/i)).toBeInTheDocument()
  })
})


