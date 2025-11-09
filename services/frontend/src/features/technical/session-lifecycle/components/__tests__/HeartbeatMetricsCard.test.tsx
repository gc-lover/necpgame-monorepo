import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { HeartbeatMetricsCard } from '../../components/HeartbeatMetricsCard'

describe('HeartbeatMetricsCard', () => {
  it('renders heartbeat metrics', () => {
    render(
      <HeartbeatMetricsCard
        metrics={[
          { timestamp: '04:10', latencyMs: 42, activity: 'active' },
        ]}
      />,
    )

    expect(screen.getByText(/04:10/i)).toBeInTheDocument()
    expect(screen.getByText(/42 ms/i)).toBeInTheDocument()
  })
})


