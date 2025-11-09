import { render, screen } from '@testing-library/react'
import { describe, it, expect } from 'vitest'
import { BackupPlanCard } from '../../components/BackupPlanCard'

describe('BackupPlanCard', () => {
  it('renders backup plan details', () => {
    render(
      <BackupPlanCard
        plan={{
          name: 'Night City Full',
          cadence: 'Daily 02:00',
          retention: '30 versions',
          lastRun: '2025-11-08 02:05',
          nextRun: '2025-11-09 02:00',
        }}
      />,
    )

    expect(screen.getByText(/Night City Full/i)).toBeInTheDocument()
    expect(screen.getByText(/Retention/i)).toBeInTheDocument()
  })
})


