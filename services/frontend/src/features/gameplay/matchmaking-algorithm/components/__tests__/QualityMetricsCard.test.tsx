import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { QualityMetricsCard } from '../../components/QualityMetricsCard'

describe('QualityMetricsCard', () => {
  it('lists quality metrics', () => {
    render(
      <QualityMetricsCard
        metrics={[
          { name: 'Latency spread', target: '< 45 ms', current: '38 ms', status: 'OK' },
        ]}
      />,
    )

    expect(screen.getByText(/Latency spread/i)).toBeInTheDocument()
    expect(screen.getByText(/38 ms/i)).toBeInTheDocument()
  })
})


