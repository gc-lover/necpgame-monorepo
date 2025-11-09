import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { SessionPoliciesCard } from '../../components/SessionPoliciesCard'

describe('SessionPoliciesCard', () => {
  it('renders session policies', () => {
    render(
      <SessionPoliciesCard
        policies={[
          { name: 'Heartbeat', value: '30s', description: 'Interval' },
        ]}
      />,
    )

    expect(screen.getByText(/Heartbeat/i)).toBeInTheDocument()
    expect(screen.getByText(/Interval/i)).toBeInTheDocument()
  })
})


