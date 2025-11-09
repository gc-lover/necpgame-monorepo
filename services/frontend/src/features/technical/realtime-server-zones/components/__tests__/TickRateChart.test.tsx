import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TickRateChart } from '../../components/TickRateChart'

describe('TickRateChart', () => {
  it('renders tick metrics', () => {
    render(
      <TickRateChart
        metrics={[
          { timestamp: '18:00', tickDurationMs: 40, warnings: [] },
        ]}
      />,
    )

    expect(screen.getByText(/18:00/i)).toBeInTheDocument()
    expect(screen.getByText(/40ms/i)).toBeInTheDocument()
  })
})


