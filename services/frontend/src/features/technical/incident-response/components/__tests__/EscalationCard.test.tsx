import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { EscalationCard } from '../../components/EscalationCard'

describe('EscalationCard', () => {
  it('renders escalation info', () => {
    render(
      <EscalationCard
        escalation={{
          level: 'L2 SRE',
          target: 'sre@oncall',
          triggeredAt: '2025-11-07 20:01',
          channel: 'PagerDuty',
          status: 'engaged',
        }}
      />,
    )

    expect(screen.getByText(/L2 SRE/i)).toBeInTheDocument()
    expect(screen.getByText(/PagerDuty/i)).toBeInTheDocument()
  })
})


