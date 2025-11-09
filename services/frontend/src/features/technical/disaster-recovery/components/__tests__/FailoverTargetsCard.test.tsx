import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { FailoverTargetsCard } from '../../components/FailoverTargetsCard'

describe('FailoverTargetsCard', () => {
  it('renders failover targets', () => {
    render(
      <FailoverTargetsCard
        targets={[
          { datacenter: 'Tokyo-ARC-02', region: 'Asia', latencyMs: 38, capacityPercent: 82 },
        ]}
      />,
    )

    expect(screen.getByText(/Tokyo-ARC-02/i)).toBeInTheDocument()
    expect(screen.getByText(/Asia/i)).toBeInTheDocument()
  })
})


