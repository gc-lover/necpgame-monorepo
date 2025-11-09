import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ConflictResolutionCard } from '../ConflictResolutionCard'

describe('ConflictResolutionCard', () => {
  it('renders conflict information', () => {
    render(
      <ConflictResolutionCard
        conflicts={[
          {
            conflictId: 'conf-1',
            component: 'ECONOMY',
            detectedAt: '2077-11-07 22:30',
            status: 'RESOLVING',
            strategy: 'Replaying last 5 ops with CAS',
          },
        ]}
      />,
    )

    expect(screen.getByText(/ECONOMY/i)).toBeInTheDocument()
    expect(screen.getByText(/CAS/i)).toBeInTheDocument()
  })
})


