import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TelemetryCard } from '../../components/TelemetryCard'

describe('TelemetryCard', () => {
  it('renders telemetry values', () => {
    render(
      <TelemetryCard
        telemetry={[
          { label: 'P50', value: 30, percentile: 50 },
        ]}
      />,
    )

    expect(screen.getByText(/P50/i)).toBeInTheDocument()
    expect(screen.getByText(/30 ms/i)).toBeInTheDocument()
  })
})


