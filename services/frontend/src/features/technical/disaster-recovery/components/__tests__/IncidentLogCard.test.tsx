import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { IncidentLogCard } from '../../components/IncidentLogCard'

describe('IncidentLogCard', () => {
  it('renders incident entries', () => {
    render(
      <IncidentLogCard
        incidents={[
          { timestamp: '2025-11-07', type: 'Drill', summary: 'Completed failover', status: 'passed' },
        ]}
      />,
    )

    expect(screen.getByText(/Incident Timeline/i)).toBeInTheDocument()
    expect(screen.getByText(/Completed failover/i)).toBeInTheDocument()
  })
})


