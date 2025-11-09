import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { DrStatusCard } from '../../components/DrStatusCard'

describe('DrStatusCard', () => {
  it('renders DR readiness data', () => {
    render(
      <DrStatusCard
        status={{
          ready: true,
          lastBackup: '2025-11-08 02:45',
          backupFrequency: 'every 30 min',
          failoverReady: true,
          rpoMinutes: 20,
          rtoMinutes: 45,
        }}
      />,
    )

    expect(screen.getByText(/DR Readiness/i)).toBeInTheDocument()
    expect(screen.getByText(/Last backup/i)).toBeInTheDocument()
  })
})


