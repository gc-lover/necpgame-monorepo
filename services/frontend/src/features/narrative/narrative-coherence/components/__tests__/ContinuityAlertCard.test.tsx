import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { ContinuityAlertCard } from '../ContinuityAlertCard'

describe('ContinuityAlertCard', () => {
  it('renders continuity alert information', () => {
    render(
      <ContinuityAlertCard
        alert={{
          alertId: 'alert-1',
          title: 'Quest timeline discrepancy',
          severity: 'WARNING',
          description: 'Quest 34 resolves before prerequisite event 31.',
          recommendedAction: 'Lock quest 34 until event 31 completed.',
          detectedAt: '2077-11-07 19:45',
        }}
      />,
    )

    expect(screen.getByText(/Quest timeline discrepancy/i)).toBeInTheDocument()
    expect(screen.getByText(/Lock quest 34/i)).toBeInTheDocument()
  })
})


