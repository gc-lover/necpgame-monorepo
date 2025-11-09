import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { TransferPlanCard } from '../../components/TransferPlanCard'

describe('TransferPlanCard', () => {
  it('renders transfer plan details', () => {
    render(
      <TransferPlanCard
        plan={{
          targetInstanceId: 'rt-nyc-03',
          drainStrategy: 'gradual',
          priority: 'high',
          reason: 'CPU load 92%',
          scheduledFor: '2025-11-07 18:20',
        }}
      />,
    )

    expect(screen.getByText(/rt-nyc-03/i)).toBeInTheDocument()
    expect(screen.getByText(/CPU load/i)).toBeInTheDocument()
  })
})


