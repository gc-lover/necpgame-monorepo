import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { NarrativeRiskCard } from '../NarrativeRiskCard'

describe('NarrativeRiskCard', () => {
  it('renders narrative risk details', () => {
    render(
      <NarrativeRiskCard
        risk={{
          riskId: 'risk-1',
          description: 'Major continuity break detected between arcs.',
          severity: 'CRITICAL',
          impactedThreads: ['Phantom Rebellion', 'Corporate War 2.0'],
          mitigation: 'Trigger retcon patch questline.',
        }}
      />,
    )

    expect(screen.getByText(/continuity break/i)).toBeInTheDocument()
    expect(screen.getByText(/retcon patch/i)).toBeInTheDocument()
  })
})


