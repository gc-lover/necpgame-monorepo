import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { AlgorithmMetricsCard } from '../../components/AlgorithmMetricsCard'

describe('AlgorithmMetricsCard', () => {
  it('shows metrics summary', () => {
    render(
      <AlgorithmMetricsCard
        metrics={{
          latencyMs: 40,
          throughputPerMin: 200,
          cacheHitRate: 95,
          queueDepth: 3,
          incidents24h: 0,
        }}
      />,
    )

    expect(screen.getByText(/Algorithm Metrics/i)).toBeInTheDocument()
    expect(screen.getByText(/Latency/i)).toBeInTheDocument()
  })
})


