import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TimelineCard } from '../../components/TimelineCard'

describe('TimelineCard', () => {
  it('renders timeline events', () => {
    render(
      <TimelineCard
        events={[
          { timestamp: '19:58', actor: 'Grafana', description: 'Detected latency spike', category: 'detection' },
        ]}
      />,
    )

    expect(screen.getByText(/19:58/i)).toBeInTheDocument()
    expect(screen.getByText(/Grafana/i)).toBeInTheDocument()
  })
})


