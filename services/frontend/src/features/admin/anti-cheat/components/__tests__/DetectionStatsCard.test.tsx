import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { DetectionStatsCard } from '../DetectionStatsCard'

describe('DetectionStatsCard', () => {
  it('renders detection metrics', () => {
    render(
      <DetectionStatsCard
        metrics={{
          autoBansLast24h: 18,
          suspiciousPatterns: 6,
          manualQueue: 9,
          falsePositivesRate: 1.75,
        }}
      />,
    )

    expect(screen.getByText(/Auto bans/)).toBeInTheDocument()
    expect(screen.getByText(/1.75%/)).toBeInTheDocument()
  })
})


