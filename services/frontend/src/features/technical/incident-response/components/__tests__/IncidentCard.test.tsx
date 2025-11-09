import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { IncidentCard } from '../../components/IncidentCard'

describe('IncidentCard', () => {
  it('renders incident summary', () => {
    render(
      <IncidentCard
        incident={{
          id: 'inc-1',
          title: 'Latency spike',
          severity: 'critical',
          status: 'acknowledged',
          detectedAt: '2025-11-07 19:58',
          commander: 'oncall-engineer',
          affectedServices: ['matchmaking-service'],
        }}
      />,
    )

    expect(screen.getByText(/Latency spike/i)).toBeInTheDocument()
    expect(screen.getByText(/Commander/i)).toBeInTheDocument()
  })
})

